package com.blintec.backend.enfesto.controller;

import com.blintec.backend.auth.model.Usuario;
import com.blintec.backend.enfesto.model.ProgramacaoEnfesto;
import com.blintec.backend.enfesto.service.EnfestoService;
import com.blintec.backend.enfesto.service.SugestaoRolo;
import com.blintec.backend.pedido.model.Pedido;
import com.blintec.backend.pedido.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos/{pedidoId}/enfesto")
public class EnfestoController {

    @Autowired
    private EnfestoService enfestoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/sugestao")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMINISTRADOR')")
    public ResponseEntity<?> sugestao(@PathVariable Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null) {
            return ResponseEntity.status(404).body(Map.of("erro", "Pedido não encontrado"));
        }

        BigDecimal consumoNecessario = enfestoService.calcularConsumoTotal(pedido);
        List<SugestaoRolo> rolos = enfestoService.sugerirRolos(pedido.getModelo().getId());

        return ResponseEntity.ok(Map.of(
                "consumoNecessario", consumoNecessario,
                "rolosDisponiveis", rolos
        ));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMINISTRADOR')")
    public ResponseEntity<?> confirmar(@PathVariable Long pedidoId, @RequestBody Map<String, BigDecimal> rolosSelecionados) {
        Long usuarioId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Map<Long, BigDecimal> rolosConvertidos = rolosSelecionados.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        entrada -> Long.parseLong(entrada.getKey()),
                        Map.Entry::getValue
                ));

        try {
            ProgramacaoEnfesto programacao = enfestoService.confirmar(pedidoId, rolosConvertidos, usuario);
            return ResponseEntity.status(201).body(programacao);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

}
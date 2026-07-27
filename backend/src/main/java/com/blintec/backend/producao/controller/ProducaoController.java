package com.blintec.backend.producao.controller;

import com.blintec.backend.auth.model.Usuario;
import com.blintec.backend.producao.model.MovimentacaoProducao;
import com.blintec.backend.producao.service.ProducaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos/{pedidoId}")
public class ProducaoController {

    @Autowired
    private ProducaoService producaoService;

    @PatchMapping("/avancar-etapa")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR', 'ADMINISTRADOR')")
    public ResponseEntity<?> avancarEtapa(@PathVariable Long pedidoId) {
        Long usuarioId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        try {
            MovimentacaoProducao movimentacao = producaoService.avancarEtapa(pedidoId, usuario);
            return ResponseEntity.ok(movimentacao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("erro", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/historico")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR', 'ADMINISTRADOR')")
    public List<MovimentacaoProducao> historico(@PathVariable Long pedidoId) {
        return producaoService.historico(pedidoId);
    }

}
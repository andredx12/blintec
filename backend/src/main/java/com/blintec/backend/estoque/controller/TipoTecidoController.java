package com.blintec.backend.estoque.controller;

import com.blintec.backend.estoque.model.TipoTecido;
import com.blintec.backend.estoque.service.TipoTecidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tipos-tecido")
public class TipoTecidoController {

    @Autowired
    private TipoTecidoService tipoTecidoService;

    @GetMapping
    public List<TipoTecido> listarTodos() {
        return tipoTecidoService.listarTodos();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<TipoTecido> criar(@Valid @RequestBody TipoTecido tipoTecido) {
        TipoTecido salvo = tipoTecidoService.criar(tipoTecido);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> atualizarEstoqueMinimo(@PathVariable Long id, @RequestBody Map<String, BigDecimal> corpo) {
        BigDecimal novoValor = corpo.get("estoqueMinimo");

        if (novoValor == null || novoValor.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body(Map.of("erro", "estoqueMinimo deve ser um valor não negativo"));
        }

        return tipoTecidoService.atualizarEstoqueMinimo(id, novoValor)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("erro", "Tipo de tecido não encontrado")));
    }

}
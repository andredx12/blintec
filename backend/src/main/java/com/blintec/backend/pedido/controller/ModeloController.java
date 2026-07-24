package com.blintec.backend.pedido.controller;

import com.blintec.backend.pedido.model.Modelo;
import com.blintec.backend.pedido.model.TamanhoModelo;
import com.blintec.backend.pedido.service.ModeloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/modelos")
public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    @GetMapping
    public List<Modelo> listarTodos() {
        return modeloService.listarTodos();
    }

    @GetMapping("/{id}/tamanhos")
    public ResponseEntity<?> listarTamanhos(@PathVariable Long id) {
        return modeloService.buscarPorId(id)
                .<ResponseEntity<?>>map(modelo -> ResponseEntity.ok(modeloService.listarTamanhos(id)))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("erro", "Modelo não encontrado")));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Modelo> criar(@Valid @RequestBody Modelo modelo) {
        Modelo salvo = modeloService.criar(modelo);
        return ResponseEntity.status(201).body(salvo);
    }

}
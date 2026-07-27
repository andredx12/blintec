package com.blintec.backend.estoque.controller;

import com.blintec.backend.auth.model.Usuario;
import com.blintec.backend.estoque.model.Rolo;
import com.blintec.backend.estoque.service.RoloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rolos")
public class RoloController {

    @Autowired
    private RoloService roloService;

    @GetMapping
    public List<Rolo> listarTodos() {
        return roloService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return roloService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("erro", "Rolo não encontrado")));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMINISTRADOR')")
    public ResponseEntity<Rolo> criar(@Valid @RequestBody Rolo rolo) {
        Long usuarioId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Rolo salvo = roloService.registrarEntrada(rolo, usuario);
        return ResponseEntity.status(201).body(salvo);
    }

}
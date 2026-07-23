package com.blintec.backend.pedido.controller;

import com.blintec.backend.pedido.model.Pedido;
import com.blintec.backend.pedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("erro", "Pedido não encontrado")));
    }

    @GetMapping("/{id}/componentes")
    public ResponseEntity<?> verComponentes(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .<ResponseEntity<?>>map(pedido -> ResponseEntity.ok(pedidoService.calcularComponentes(pedido)))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("erro", "Pedido não encontrado")));
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        Pedido salvo = pedidoService.criar(pedido);
        return ResponseEntity.status(201).body(salvo);
    }

}
package com.blintec.backend.pedido.service;

import com.blintec.backend.pedido.model.ItemPedido;
import com.blintec.backend.pedido.model.Pedido;
import com.blintec.backend.pedido.model.StatusPedido;
import com.blintec.backend.pedido.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public ComponentesCapa calcularComponentes(Pedido pedido) {
        int totalCapas = 0;

        for (ItemPedido item : pedido.getItens()) {
            totalCapas += item.getQuantidade();
        }

        int multiplicador = 1 + pedido.getCapaExtra();
        totalCapas *= multiplicador;

        return new ComponentesCapa(
                totalCapas,
                totalCapas,
                totalCapas * 2,
                totalCapas * 2,
                totalCapas * 4
        );
    }

    public Pedido criar(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            item.setPedido(pedido);
        }
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos(StatusPedido status) {
        if (status != null) {
            return pedidoRepository.findByStatus(status);
        }
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

}
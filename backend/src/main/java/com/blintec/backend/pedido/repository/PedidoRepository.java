package com.blintec.backend.pedido.repository;

import com.blintec.backend.pedido.model.Pedido;
import com.blintec.backend.pedido.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    List<Pedido> findByStatus(StatusPedido status);

}
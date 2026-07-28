package com.blintec.backend.dashboard.service;

import java.util.Map;
import com.blintec.backend.pedido.model.StatusPedido;

public record ResumoPedidos(
        Map<StatusPedido, Long> contagemPorStatus,
        long totalPedidos
) {
}
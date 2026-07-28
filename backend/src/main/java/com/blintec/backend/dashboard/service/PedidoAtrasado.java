package com.blintec.backend.dashboard.service;

import java.time.LocalDate;

public record PedidoAtrasado(
        Long pedidoId,
        String numeroPedido,
        LocalDate dataEntrega,
        boolean atrasado,
        boolean proximoVencimento
) {
}
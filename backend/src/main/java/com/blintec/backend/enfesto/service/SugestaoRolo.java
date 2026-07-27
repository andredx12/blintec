package com.blintec.backend.enfesto.service;

import java.math.BigDecimal;

public record SugestaoRolo(
        Long roloId,
        String codigo,
        BigDecimal saldoDisponivel
) {
}
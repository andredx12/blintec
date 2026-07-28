package com.blintec.backend.dashboard.service;

import java.math.BigDecimal;

public record IndicadorEstoque(
        Long tipoTecidoId,
        String nomeTipoTecido,
        BigDecimal saldoTotal,
        BigDecimal estoqueMinimo,
        boolean abaixoDoMinimo
) {
}
package com.blintec.backend.dashboard.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumoPeriodo(
        LocalDate inicio,
        LocalDate fim,
        BigDecimal consumoTotal
) {
}

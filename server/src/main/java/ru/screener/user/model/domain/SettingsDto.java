package ru.screener.user.model.domain;

import java.math.BigDecimal;

public record SettingsDto(Integer longPeriodMin, Integer shortPeriodMin, Integer dumpPeriodMin,
                          BigDecimal longPercent, BigDecimal shortPercent, BigDecimal dumpPercent) {}

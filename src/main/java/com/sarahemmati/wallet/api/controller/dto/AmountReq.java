package com.sarahemmati.wallet.api.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AmountReq(@NotNull @Positive BigDecimal amount) {}

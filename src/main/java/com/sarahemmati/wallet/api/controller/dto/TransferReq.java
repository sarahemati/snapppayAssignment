package com.sarahemmati.wallet.api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferReq(@NotBlank String toUsername, @NotNull @Positive BigDecimal amount) {}

package com.sarahemmati.wallet.api.dto;

import java.math.BigDecimal;

public record CreateConfigDto(String username, BigDecimal dailyLimit, BigDecimal singleTxLimit) {}

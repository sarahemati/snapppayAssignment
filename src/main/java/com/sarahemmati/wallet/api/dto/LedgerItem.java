package com.sarahemmati.wallet.api.dto;

import java.math.BigDecimal;

public record LedgerItem(String type, BigDecimal amount, String ref){}

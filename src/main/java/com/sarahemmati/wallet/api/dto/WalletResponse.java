package com.sarahemmati.wallet.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record WalletResponse(String username, BigDecimal balance, List<LedgerItem> lastLedger){}


package com.sarahemmati.wallet.application.services;

import com.sarahemmati.wallet.domain.Wallet;
import com.sarahemmati.wallet.domain.enums.OperationType;

import java.math.BigDecimal;

public interface LedgerService {
    void record(Wallet wallet, String ref, OperationType type, BigDecimal amount, String requestId);


}

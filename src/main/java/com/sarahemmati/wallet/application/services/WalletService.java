package com.sarahemmati.wallet.application.services;

import com.sarahemmati.wallet.api.dto.WalletResponse;
import com.sarahemmati.wallet.domain.Wallet;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public interface WalletService {
    WalletResponse myWallet(String username);

    void deposit(String username, BigDecimal amount, @Nullable String ref, String requestId);

    void transfer(String fromUsername, String toUsername, BigDecimal amount, String ref, String requestId);
}
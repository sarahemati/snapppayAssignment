package com.sarahemmati.wallet.application.services;


import com.sarahemmati.wallet.domain.LedgerEntry;
import com.sarahemmati.wallet.domain.Wallet;
import com.sarahemmati.wallet.domain.enums.OperationType;
import com.sarahemmati.wallet.infra.repository.LedgerRepo;
import com.sarahemmati.wallet.infra.repository.WalletRepo;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepo walletRepo;
    private final LedgerRepo ledgerRepo;

    public WalletService(WalletRepo walletRepo, LedgerRepo ledgerRepo){
        this.walletRepo = walletRepo; this.ledgerRepo = ledgerRepo;
    }

    public record WalletView(String username, BigDecimal balance, List<LedgerItem> lastLedger){}
    public record LedgerItem(String type, BigDecimal amount, String ref){}

    public WalletView me(String username){
        Wallet w = walletRepo.findByUserUsername(username).orElseThrow(() -> new IllegalArgumentException("WALLET_NOT_FOUND"));
        var items = ledgerRepo.findTop20ByWalletUserUsernameOrderByIdDesc(username)
                .stream().map(e -> new LedgerItem(e.getType().name(), e.getAmount(), e.getRef())).toList();
        return new WalletView(username, w.getBalance(), items);
    }



    @Transactional
    public void deposit(String username, BigDecimal amount, @Nullable String ref){
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("AMOUNT_INVALID");
        if (ref != null && ledgerRepo.existsByRef(ref)) return; // idempotent

        Wallet w = walletRepo.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("WALLET_NOT_FOUND"));
        w.credit(amount);
        walletRepo.save(w);

        String useRef = (ref != null && !ref.isBlank()) ? ref : UUID.randomUUID().toString();
        ledgerRepo.save(LedgerEntry.of(w, amount, OperationType.DEPOSIT, useRef));
    }


    @Transactional
    public void transfer(String fromUsername, String toUsername, BigDecimal amount, String ref){
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("AMOUNT_INVALID");
        if (fromUsername.equals(toUsername)) throw new IllegalArgumentException("SAME_WALLET");

        if (ledgerRepo.existsByRef(ref)) return;

        Wallet from = walletRepo.findByUsernameForUpdate(fromUsername).orElseThrow(() -> new IllegalArgumentException("FROM_WALLET_NOT_FOUND"));
        Wallet to   = walletRepo.findByUsernameForUpdate(toUsername).orElseThrow(() -> new IllegalArgumentException("TO_WALLET_NOT_FOUND"));

        from.debit(amount);
        to.credit(amount);
        walletRepo.save(from);
        walletRepo.save(to);

        ledgerRepo.save(LedgerEntry.of(from, amount.negate(), OperationType.TRANSFER_OUT, ref));
        ledgerRepo.save(LedgerEntry.of(to, amount,          OperationType.TRANSFER_IN,  ref));
    }
}

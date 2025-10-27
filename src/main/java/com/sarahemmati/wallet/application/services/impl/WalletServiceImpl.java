package com.sarahemmati.wallet.application.services.impl;

import com.sarahemmati.wallet.api.dto.LedgerItem;
import com.sarahemmati.wallet.api.dto.WalletResponse;
import com.sarahemmati.wallet.application.services.AccountLimitService;
import com.sarahemmati.wallet.application.services.AuditService;
import com.sarahemmati.wallet.application.services.LedgerService;
import com.sarahemmati.wallet.application.services.WalletService;
import com.sarahemmati.wallet.domain.User;
import com.sarahemmati.wallet.domain.Wallet;
import com.sarahemmati.wallet.domain.enums.OperationType;
import com.sarahemmati.wallet.infra.repository.LedgerRepo;
import com.sarahemmati.wallet.infra.repository.UserRepo;
import com.sarahemmati.wallet.infra.repository.WalletRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final UserRepo userRepo;
    private final WalletRepo walletRepo;
    private final LedgerService ledgerService;
    private final AccountLimitService accountLimitService;
    private final LedgerRepo ledgerRepo;
    private final AuditService auditService;

    public WalletServiceImpl(UserRepo userRepo,
                             WalletRepo walletRepo,
                             LedgerService ledgerService,
                             AccountLimitService accountLimitService, LedgerRepo ledgerRepo, AuditService auditService) {
        this.userRepo = userRepo;
        this.walletRepo = walletRepo;
        this.ledgerService = ledgerService;
        this.accountLimitService = accountLimitService;
        this.ledgerRepo = ledgerRepo;
        this.auditService = auditService;
    }



    @Transactional
    @Override
    public void deposit(String username, BigDecimal amount, String ref, String requestId) {
        log.info("Deposi {}", amount);
        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("USER_NOT_FOUND"));

        Wallet wallet = (Wallet) walletRepo.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("WALLET_NOT_FOUND"));

        wallet.credit(amount);
        walletRepo.save(wallet);
        ledgerService.record(wallet, ref, OperationType.DEPOSIT, amount, requestId);
        try {
            auditService.log(username, OperationType.DEPOSIT,amount, ref, "Deposit",requestId); }
        catch (Exception e) {
            log.warn("AUDIT_WRITE_FAILED ref={} req={}", ref, requestId, e);
        }
        log.info("Deposit user={}, amt={}, ref={}, req={}", username, amount, ref, requestId);

    }


    @Transactional
    @Override
    public void transfer(String fromUsername, String toUsername,
                         BigDecimal amount, String ref, String requestId) {

        if (fromUsername.equals(toUsername))
            throw new IllegalStateException("CANNOT_TRANSFER_TO_SELF");

        User fromUser = userRepo.findByUsername(fromUsername)
                .orElseThrow(() -> new IllegalStateException("SENDER_NOT_FOUND"));
        User toUser = userRepo.findByUsername(toUsername)
                .orElseThrow(() -> new IllegalStateException("RECEIVER_NOT_FOUND"));

        Wallet fromWallet = (Wallet) walletRepo.findByUser(fromUser)
                .orElseThrow(() -> new IllegalStateException("SENDER_WALLET_NOT_FOUND"));
        Wallet toWallet = (Wallet) walletRepo.findByUser(toUser)
                .orElseThrow(() -> new IllegalStateException("RECEIVER_WALLET_NOT_FOUND"));
        if (fromWallet.getBalance().compareTo(amount) < 0)
            throw new IllegalStateException("INSUFFICIENT_FUNDS");

        accountLimitService.checkAndConsumeLimit(fromUser, amount);


        // Debit from sender

        fromWallet.debit(amount);
        walletRepo.save(fromWallet);
        ledgerService.record(fromWallet, ref, OperationType.TRANSFER_OUT, amount.negate(), requestId);

        // Credit to receiver
        toWallet.credit(amount);
        walletRepo.save(toWallet);
        ledgerService.record(toWallet, ref, OperationType.TRANSFER_IN, amount, requestId);
        log.info("WITHDRAW done user={}, newBalance={}", fromUsername, fromWallet.getBalance());

        try {
            auditService.log(fromUsername, OperationType.TRANSFER,amount, ref, "Transfer",requestId);
        }
        catch (Exception e) { log.warn("AUDIT_WRITE_FAILED ref={} req={}", ref, requestId, e); }
        log.info("WITHDRAW  user={}, amt={}, ref={}, req={}", fromUsername, amount, ref, requestId);
    }


    @Transactional(readOnly = true)
    @Override
    public WalletResponse myWallet(String username) {
        log.info("myWallet {}", username);
        Wallet w = walletRepo.findByUserUsername(username).orElseThrow(() -> new IllegalArgumentException("WALLET_NOT_FOUND"));
        var items = ledgerRepo.findTop20ByWalletUserUsernameOrderByIdDesc(username)
                .stream().map(e -> new LedgerItem(e.getType().name(), e.getAmount(), e.getRef())).toList();
        return new WalletResponse(username, w.getBalance(), items);
    }
}

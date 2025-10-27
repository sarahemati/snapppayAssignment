package com.sarahemmati.wallet.application.services.impl;


import com.sarahemmati.wallet.application.services.LedgerService;
import com.sarahemmati.wallet.domain.Ledger;
import com.sarahemmati.wallet.domain.Wallet;
import com.sarahemmati.wallet.domain.enums.OperationType;
import com.sarahemmati.wallet.infra.repository.LedgerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class LedgerServiceImpl implements LedgerService {

    private final LedgerRepo ledgerRepo;


    public LedgerServiceImpl(LedgerRepo ledgerRepo) {
        this.ledgerRepo = ledgerRepo;
    }

    @Transactional
    public void record(Wallet wallet, String ref, OperationType type, BigDecimal amount, String requestId) {
        // Prevent duplicate (idempotent) inserts
        log.info("Recording ledger");
        if (ledgerRepo.findByRef(ref).isPresent()) return;

        var entry = Ledger.builder()
                .wallet(wallet)
                .ref(ref)
                .type(type)
                .amount(amount)
                .ref(requestId)
                .build();

        ledgerRepo.save(entry);
    }

}

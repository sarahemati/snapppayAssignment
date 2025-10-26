package com.sarahemmati.wallet.application.services.impl;


import com.sarahemmati.wallet.application.services.AccountLimitService;
import com.sarahemmati.wallet.domain.*;
import com.sarahemmati.wallet.infra.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class AccountLimitServiceImpl implements AccountLimitService {

    private final AccountLimitConfigRepo configRepo;
    private final AccountLimitUsageRepo usageRepo;


    public AccountLimitServiceImpl(AccountLimitConfigRepo configRepo, AccountLimitUsageRepo usageRepo) {
        this.configRepo = configRepo;
        this.usageRepo = usageRepo;
    }

    @Transactional
    public void checkAndConsumeLimit(User user, BigDecimal txAmount) {
        log.info("Checking limit for user {}", user);
        var config = configRepo.findByUser(user)
                .orElseGet(() -> configRepo.save(AccountLimitConfig.builder()
                        .user(user)
                        .dailyLimit(new BigDecimal("10000"))
                        .singleTxLimit(new BigDecimal("5000"))
                        .build()));

        // Check single transaction limit
        if (txAmount.compareTo(config.getSingleTxLimit()) > 0) {
            log.error("Transaction Limit exceeded for user {}", user);
            throw new IllegalStateException("TX_LIMIT_EXCEEDED");
        }
        // Check daily usage
        var today = LocalDate.now();
        var usage = usageRepo.findByUserAndDate(user, today)
                .orElseGet(() -> usageRepo.save(AccountLimitUsage.builder()
                        .user(user)
                        .date(today)
                        .usedAmount(BigDecimal.ZERO)
                        .build()));

        var newTotal = usage.getUsedAmount().add(txAmount);
        if (newTotal.compareTo(config.getDailyLimit()) > 0) {
            log.error("DAILY_LIMIT_EXCEEDED");
            throw new IllegalStateException("DAILY_LIMIT_EXCEEDED");
        }

        usage.setUsedAmount(newTotal);
        usageRepo.save(usage);

    }
}

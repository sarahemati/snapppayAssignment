package com.sarahemmati.wallet;

import com.sarahemmati.wallet.application.services.WalletService;
import com.sarahemmati.wallet.domain.AccountLimitConfig;
import com.sarahemmati.wallet.domain.User;
import com.sarahemmati.wallet.infra.repository.AccountLimitConfigRepo;
import com.sarahemmati.wallet.infra.repository.AccountLimitUsageRepo;
import com.sarahemmati.wallet.infra.repository.UserRepo;
import com.sarahemmati.wallet.infra.repository.WalletRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AccountLimitServiceIT {
    @Autowired
    UserRepo userRepo;
    @Autowired
    WalletRepo walletRepo;
    @Autowired
    AccountLimitConfigRepo limitConfigRepo;
    @Autowired
    AccountLimitUsageRepo limitUsageRepo;

    @Autowired WalletService walletService;

    User ali, sara;

    @BeforeEach
    void init() {
        ali  = userRepo.save(new User("ali", "{noop}ph", "ROLE_USER"));
        sara = userRepo.save(new User("sara", "{noop}ph", "ROLE_USER"));
        walletRepo.save(new com.sarahemmati.wallet.domain.Wallet(ali));
        walletRepo.save(new com.sarahemmati.wallet.domain.Wallet(sara));

        walletService.deposit("ali", new BigDecimal("200.00"), "init", "req-init");

        limitConfigRepo.save(AccountLimitConfig.builder()
                .user(ali)
                .dailyLimit(new BigDecimal("100.00"))
                .singleTxLimit(new BigDecimal("60.00"))
                .build());
    }

    @Test
    void single_tx_limit_violation_should_fail() {
        var ex = assertThrows(IllegalStateException.class, () ->
                walletService.transfer("ali", "sara",
                        new BigDecimal("70.00"),
                        "ref-single-violate",
                        UUID.randomUUID().toString()
                ));
        assertThat(ex.getMessage()).isIn("DAILY_LIMIT_EXCEEDED", "TX_LIMIT_EXCEEDED");
        var usage = limitUsageRepo.findByUserAndDate(ali, java.time.LocalDate.now());
        assertThat(usage).isEmpty();
    }
}

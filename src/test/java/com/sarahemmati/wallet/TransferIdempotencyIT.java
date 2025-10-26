package com.sarahemmati.wallet;

import com.sarahemmati.wallet.application.services.impl.WalletServiceImpl;
import com.sarahemmati.wallet.domain.User;
import com.sarahemmati.wallet.domain.Wallet;
import com.sarahemmati.wallet.infra.repository.UserRepo;
import com.sarahemmati.wallet.infra.repository.WalletRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")

class TransferInsufficientIT {

    @Autowired
    UserRepo userRepo;
    @Autowired
    WalletRepo walletRepo;
    @Autowired
    WalletServiceImpl walletService;

    @BeforeEach
    void setup() {
        var a = userRepo.save(new User("ali", "ph", "ROLE_USER"));
        var b = userRepo.save(new User("sara", "ph", "ROLE_USER"));
        walletRepo.save(new Wallet(a));
        walletRepo.save(new Wallet(b));
        // شارژ اولیه علی فقط 20
        walletService.deposit("ali", new BigDecimal("20.00"), "init", "req-init");
    }

    @Test
    void transfer_should_fail_when_insufficient() {
        var ex = Assertions.assertThrows(IllegalStateException.class, () ->
                walletService.transfer("ali", "sara", new BigDecimal("50.00"),
                        UUID.randomUUID().toString(), "req-1"));

        assertEquals("INSUFFICIENT_FUNDS", ex.getMessage());
    }
}
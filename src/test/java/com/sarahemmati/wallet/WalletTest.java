package com.sarahemmati.wallet;

import com.sarahemmati.wallet.domain.User;
import com.sarahemmati.wallet.domain.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
class WalletTest {

    @Test
    void credit_and_debit_rules() {
        User u = new User("u1", "ph", "ROLE_USER");
        Wallet w = new Wallet(u);

        // initial
        assertEquals(new BigDecimal(String.valueOf(BigDecimal.ZERO)), w.getBalance());

        // credit
        w.credit(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), w.getBalance());

        // negative/zero amounts
        assertThrows(IllegalArgumentException.class, () -> w.credit(new BigDecimal("-1")));
        assertThrows(IllegalArgumentException.class, () -> w.debit(new BigDecimal("0")));

        // insufficient funds
        assertThrows(IllegalStateException.class, () -> w.debit(new BigDecimal("150.00")));

        // debit ok
        w.debit(new BigDecimal("30.00"));
        assertEquals(new BigDecimal("70.00"), w.getBalance());
    }

    @Test
    void debit_should_fail_when_insufficient_funds() {
        User u = new User("u1", "ph", "ROLE_USER");
        Wallet w = new Wallet(u);

        w.credit(new BigDecimal("50.00"));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> w.debit(new BigDecimal("60.00")));

        assertEquals("INSUFFICIENT_FUNDS", ex.getMessage());
        assertEquals(0, w.getBalance().compareTo(new BigDecimal("50.00")));
    }

    @Test
    void credit_and_debit_should_reject_non_positive_amounts() {
        User u = new User("u1", "ph", "ROLE_USER");
        Wallet w = new Wallet(u);

        assertThrows(IllegalArgumentException.class, () -> w.credit(new BigDecimal("0")));
        assertThrows(IllegalArgumentException.class, () -> w.credit(new BigDecimal("-1")));

        assertThrows(IllegalArgumentException.class, () -> w.debit(new BigDecimal("0")));
        assertThrows(IllegalArgumentException.class, () -> w.debit(new BigDecimal("-1")));
    }


}

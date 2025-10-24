package com.sarahemmati.wallet.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "WALLET")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Wallet extends BaseEntity {

    @OneToOne(optional = false)
    @JoinColumn(name = "USER_ID", unique = true)
    private User user;

    @Column(nullable=false, precision=18, scale=2)
    private BigDecimal balance = BigDecimal.ZERO;

    public Wallet(User user) {
        this.user = user;
    }

    public void credit(BigDecimal amt){
        requirePositive(amt);
        this.balance = this.balance.add(amt);
    }

    public void debit(BigDecimal amt){
        requirePositive(amt);
        if(this.balance.compareTo(amt) < 0) throw new IllegalStateException("INSUFFICIENT_FUNDS");
        this.balance = this.balance.subtract(amt);
    }

    private static void requirePositive(BigDecimal amt){
        if (amt == null || amt.signum() <= 0) throw new IllegalArgumentException("AMOUNT_INVALID");
    }
}


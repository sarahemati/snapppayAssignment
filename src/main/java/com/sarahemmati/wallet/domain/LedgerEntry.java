package com.sarahemmati.wallet.domain;

import com.sarahemmati.wallet.domain.enums.OperationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(
        name = "LEDGER",
        indexes = {
                @Index(name = "ix_ledger_wallet_ref", columnList = "wallet_id, ref", unique = true),
                @Index(name = "ix_ledger_ref", columnList = "ref")
        }
)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class LedgerEntry extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name="WALLET_ID")
    private Wallet wallet;

    @Column(nullable=false, precision=18, scale=2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private OperationType type;

    @Column(nullable=false, length=100)
    private String ref;

    private LedgerEntry(Wallet wallet, BigDecimal amount, OperationType type, String ref) {
        this.wallet = wallet;
        this.amount = amount;
        this.type = type;
        this.ref = ref;
    }

    public static LedgerEntry of(Wallet w, BigDecimal amt, OperationType type, String ref){
        return new LedgerEntry(w, amt, type, ref);
    }
}



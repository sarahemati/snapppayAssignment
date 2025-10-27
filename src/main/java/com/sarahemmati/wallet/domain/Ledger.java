package com.sarahemmati.wallet.domain;

import com.sarahemmati.wallet.domain.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "LEDGER",
        indexes = {
                @Index(name = "ix_ledger_wallet_ref", columnList = "WALLET_ID, REF", unique = true),
                @Index(name = "ix_ledger_ref", columnList = "REF")
        }
)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Builder
public class Ledger extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name="WALLET_ID")
    private Wallet wallet;

    @Column(name="AMOUNT",nullable=false, precision=18, scale=2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name="OPERATION_TYPE",nullable=false, length=20)
    private OperationType type;

    @Column(name="REF",nullable=false, length=100)
    private String ref;

    private Ledger(Wallet wallet, BigDecimal amount, OperationType type, String ref) {
        this.wallet = wallet;
        this.amount = amount;
        this.type = type;
        this.ref = ref;
    }

    public static Ledger of(Wallet w, BigDecimal amt, OperationType type, String ref){
        return new Ledger(w, amt, type, ref);
    }
}



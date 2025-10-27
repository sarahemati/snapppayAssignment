package com.sarahemmati.wallet.domain;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNT_LIMIT_USAGE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USER_ID", "DATE"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountLimitUsage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name="DATE",nullable = false)
    private LocalDate date;

    @Column(name="USED_AMOUNT",nullable = false)
    private BigDecimal usedAmount;

    private UUID lastTxId;


}

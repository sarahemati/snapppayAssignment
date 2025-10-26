package com.sarahemmati.wallet.domain;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNT_LIMIT_USAGE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "date"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountLimitUsage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private BigDecimal usedAmount;

    private UUID lastTxId;


}

package com.sarahemmati.wallet.domain;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACCOUNT_LIMIT_CONFIG")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountLimitConfig extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private BigDecimal dailyLimit;

    @Column(nullable = false)
    private BigDecimal singleTxLimit;

}

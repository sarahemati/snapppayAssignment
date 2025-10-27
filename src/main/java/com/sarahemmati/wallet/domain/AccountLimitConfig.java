package com.sarahemmati.wallet.domain;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNT_LIMIT_CONFIG")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountLimitConfig extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;

    @Column(name="DAILY_LIMIT",nullable = false)
    private BigDecimal dailyLimit;

    @Column(name="SINGLE_TX_LIMIT",nullable = false)
    private BigDecimal singleTxLimit;

}

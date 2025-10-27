package com.sarahemmati.wallet.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountLimitConfigRes {

        private String username;
        private BigDecimal dailyLimit;
        private BigDecimal singleTxLimit;

}

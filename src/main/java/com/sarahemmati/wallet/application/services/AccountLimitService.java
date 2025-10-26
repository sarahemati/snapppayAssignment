package com.sarahemmati.wallet.application.services;

import com.sarahemmati.wallet.domain.User;

import java.math.BigDecimal;

public interface AccountLimitService {

     void checkAndConsumeLimit(User user, BigDecimal txAmount);


}

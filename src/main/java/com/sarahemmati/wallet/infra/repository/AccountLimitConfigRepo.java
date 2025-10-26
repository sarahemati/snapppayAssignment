package com.sarahemmati.wallet.infra.repository;


import com.sarahemmati.wallet.domain.AccountLimitConfig;
import com.sarahemmati.wallet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountLimitConfigRepo extends JpaRepository<AccountLimitConfig, Long> {

    Optional<AccountLimitConfig> findByUser(User user);
}

package com.sarahemmati.wallet.infra.repository;


import com.sarahemmati.wallet.domain.AccountLimitUsage;
import com.sarahemmati.wallet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AccountLimitUsageRepo extends JpaRepository<AccountLimitUsage, Long> {

    Optional<AccountLimitUsage> findByUserAndDate(User user, LocalDate date);
}

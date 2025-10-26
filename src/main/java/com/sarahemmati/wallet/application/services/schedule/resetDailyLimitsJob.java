package com.sarahemmati.wallet.application.services.schedule;

import com.sarahemmati.wallet.infra.repository.AccountLimitUsageRepo;
import org.springframework.scheduling.annotation.Scheduled;

public class resetDailyLimitsJob {
    private final AccountLimitUsageRepo accountLimitUsageRepo;

    public resetDailyLimitsJob(AccountLimitUsageRepo accountLimitUsageRepo) {
        this.accountLimitUsageRepo = accountLimitUsageRepo;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyLimits() {
        accountLimitUsageRepo.deleteAll();
    }
}

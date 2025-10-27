package com.sarahemmati.wallet.application.services.schedule;

import com.sarahemmati.wallet.infra.repository.AccountLimitUsageRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class resetDailyLimitsJob {
    private final AccountLimitUsageRepo accountLimitUsageRepo;

    public resetDailyLimitsJob(AccountLimitUsageRepo accountLimitUsageRepo) {
        this.accountLimitUsageRepo = accountLimitUsageRepo;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetDailyLimits() {
        log.info("resetDailyLimits");
        accountLimitUsageRepo.deleteAll();
        log.info("resetDailyLimits done");
    }

}

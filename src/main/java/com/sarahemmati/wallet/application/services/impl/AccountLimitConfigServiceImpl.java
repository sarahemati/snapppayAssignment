package com.sarahemmati.wallet.application.services.impl;

import com.sarahemmati.wallet.api.dto.CreateConfigDto;
import com.sarahemmati.wallet.application.services.AccountLimitConfigService;
import com.sarahemmati.wallet.domain.AccountLimitConfig;
import com.sarahemmati.wallet.infra.repository.AccountLimitConfigRepo;
import com.sarahemmati.wallet.infra.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;

@Service
public class AccountLimitConfigServiceImpl implements AccountLimitConfigService {
    private final AccountLimitConfigRepo accountLimitConfigRepo;
    private final UserRepo userRepo;

    public AccountLimitConfigServiceImpl(AccountLimitConfigRepo accountLimitConfigRepo, UserRepo userRepo) {
        this.accountLimitConfigRepo = accountLimitConfigRepo;
        this.userRepo = userRepo;
    }


    @Override
    @Transactional
    public CreateConfigDto createLimitConfig(CreateConfigDto req){

        var user = userRepo.findByUsername(req.username())
                .orElseThrow(() -> new IllegalStateException("USER_NOT_FOUND"));

        if (req.singleTxLimit().compareTo(req.dailyLimit()) > 0) {
            throw new IllegalArgumentException("SINGLE_TX_LIMIT_GT_DAILY");
        }

        var cfg = accountLimitConfigRepo.findByUser(user)
                .orElseGet(() -> AccountLimitConfig.builder().user(user).build());

        cfg.setDailyLimit(req.dailyLimit().setScale(2, RoundingMode.DOWN));
        cfg.setSingleTxLimit(req.singleTxLimit().setScale(2, RoundingMode.DOWN));

        accountLimitConfigRepo.save(cfg);

        return new CreateConfigDto(
                user.getUsername(),
                cfg.getDailyLimit(),
                cfg.getSingleTxLimit()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CreateConfigDto getByUsername(String username) {
        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("USER_NOT_FOUND"));
        var cfg = accountLimitConfigRepo.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("ACCOUNT_LIMIT_CONFIG_NOT_FOUND"));
        return new CreateConfigDto(
                cfg.getUser().getUsername(),
                cfg.getDailyLimit(),
                cfg.getSingleTxLimit()
        );
    }


}

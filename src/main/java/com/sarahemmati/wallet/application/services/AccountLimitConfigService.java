package com.sarahemmati.wallet.application.services;

import com.sarahemmati.wallet.api.dto.CreateConfigDto;

public interface AccountLimitConfigService {
    CreateConfigDto createLimitConfig(CreateConfigDto createConfigDto);

    CreateConfigDto getByUsername(String username);
}

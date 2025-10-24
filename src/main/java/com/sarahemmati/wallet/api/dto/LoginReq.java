package com.sarahemmati.wallet.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(@NotBlank String username, @NotBlank String password){}

package com.sarahemmati.wallet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupReq(@NotBlank String username, @Size(min=6) String password){}


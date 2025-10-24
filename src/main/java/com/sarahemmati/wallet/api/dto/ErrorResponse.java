package com.sarahemmati.wallet.api.dto;

import java.time.Instant;

public record ErrorResponse(
        String code,          //  AMOUNT_INVALID, INSUFFICIENT_FUNDS
        String message,
        Instant timestamp,
        String path,
        String requestId
) {}

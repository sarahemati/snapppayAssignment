package com.sarahemmati.wallet.api.dto;

import com.sarahemmati.wallet.domain.enums.OperationType;

import java.math.BigDecimal;
import java.time.Instant;

public record LogReq(Instant ts, String username, OperationType operation,
                     BigDecimal amount, String ref, String details, String requestId) {
}

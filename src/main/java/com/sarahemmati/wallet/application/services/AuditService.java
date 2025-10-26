package com.sarahemmati.wallet.application.services;

import com.sarahemmati.wallet.domain.enums.OperationType;

import java.math.BigDecimal;

public interface AuditService {
    void log(String username, OperationType operation, BigDecimal amount, String ref, String details, String requestId);

}

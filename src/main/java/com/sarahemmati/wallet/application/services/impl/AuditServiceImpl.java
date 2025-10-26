package com.sarahemmati.wallet.application.services.impl;

import com.sarahemmati.wallet.application.services.AuditService;
import com.sarahemmati.wallet.domain.AuditLog;
import com.sarahemmati.wallet.domain.enums.OperationType;
import com.sarahemmati.wallet.infra.repository.AuditLogRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepo repo;

    public AuditServiceImpl(AuditLogRepo repo) { this.repo = repo; }

    public void log(String username, OperationType operation, BigDecimal amount, String ref, String details, String requestId) {
        repo.save(new AuditLog(Instant.now(), username, operation, amount, ref, details, requestId));
    }
}

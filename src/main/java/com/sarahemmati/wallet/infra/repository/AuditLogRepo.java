package com.sarahemmati.wallet.infra.repository;


import com.sarahemmati.wallet.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepo extends JpaRepository<AuditLog, Long> { }

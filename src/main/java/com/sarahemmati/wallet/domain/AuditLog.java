package com.sarahemmati.wallet.domain;

import com.sarahemmati.wallet.domain.enums.OperationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "AUDIT_LOGS", indexes = {
        @Index(name="ix_audit_ts", columnList = "ts"),
        @Index(name="ix_audit_user", columnList = "username")
})
@Getter
@NoArgsConstructor
public class AuditLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Instant ts;

    @Column(nullable=false, length=64)
    private String username;

    @Column(nullable=false, length=32)
    @Enumerated(EnumType.STRING)
    private OperationType operation;

    @Column(precision=18, scale=2)
    private BigDecimal amount;

    @Column(length=100)
    private String ref;          // Idempotency Key یا تراکنش

    @Column(length=200)
    private String details;

    @Column(length=40)
    private String requestId;

    public AuditLog(Instant ts, String username, OperationType operation,
                    BigDecimal amount, String ref, String details, String requestId) {
        this.ts = ts;
        this.username = username;
        this.operation = operation;
        this.amount = amount;
        this.ref = ref;
        this.details = details;
        this.requestId = requestId;
    }
}

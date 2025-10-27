package com.sarahemmati.wallet.domain;

import com.sarahemmati.wallet.domain.enums.OperationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "AUDIT_LOG", indexes = {
        @Index(name="ix_audit_user", columnList = "username")
})
@Getter
@NoArgsConstructor
public class AuditLog extends BaseEntity {


    @Column(name="USERNAME",nullable=false, length=64)
    private String username;

    @Column(name="OPERATION",nullable=false, length=32)
    @Enumerated(EnumType.STRING)
    private OperationType operation;

    @Column(name="AMOUNT",precision=18, scale=2)
    private BigDecimal amount;

    @Column(name="REF",length=100)
    private String ref;

    @Column(name="DETAILS",length=200)
    private String details;

    @Column(name="REQUEST_ID",length=40)
    private String requestId;

    public AuditLog( String username, OperationType operation,
                    BigDecimal amount, String ref, String details, String requestId) {

        this.username = username;
        this.operation = operation;
        this.amount = amount;
        this.ref = ref;
        this.details = details;
        this.requestId = requestId;
    }
}

package com.sarahemmati.wallet.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    @Version
    @Column(name = "VERSION")
    protected Integer version;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    protected LocalDateTime updatedAt;


}

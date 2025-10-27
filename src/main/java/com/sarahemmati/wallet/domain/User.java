package com.sarahemmati.wallet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends BaseEntity {

    @OneToOne(mappedBy = "user")
    private Wallet wallet;


    @Column(name="USERNAME",nullable = false, unique = true, length = 64)
    @EqualsAndHashCode.Include
    private String username;

    @Column(name="PASSWORD_HASH",nullable = false, length = 200)
    private String passwordHash;

    @Column(name="ROLES",nullable = false, length = 100)
    private String roles = "ROLE_USER";

    public User(String username, String passwordHash, String roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        if (roles != null) this.roles = roles;
    }
}

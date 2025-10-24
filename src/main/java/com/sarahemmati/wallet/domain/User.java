package com.sarahemmati.wallet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(nullable = false, unique = true, length = 64)
    @EqualsAndHashCode.Include
    private String username;

    @Column(nullable = false, length = 200)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String roles = "ROLE_USER";

    public User(String username, String passwordHash, String roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        if (roles != null) this.roles = roles;
    }
}

package com.sarahemmati.wallet.infra.repository;

import com.sarahemmati.wallet.domain.Wallet;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletRepo extends JpaRepository<Wallet, Long> {
    @Query("select w from Wallet w where w.user.username = :username")
    Optional<Wallet> findByUserUsername(String username);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select w from Wallet w where w.user.username=:username")
    Optional<Wallet> findByUsernameForUpdate(@Param("username") String username);
}
package com.sarahemmati.wallet.infra.repository;

import com.sarahemmati.wallet.domain.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerRepo extends JpaRepository<LedgerEntry, Long> {
    boolean existsByRef(String ref);
    List<LedgerEntry> findTop20ByWalletUserUsernameOrderByIdDesc(String username);
}
package com.sarahemmati.wallet.infra.repository;

import com.sarahemmati.wallet.domain.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LedgerRepo extends JpaRepository<Ledger, Long> {
    boolean existsByRef(String ref);

    boolean existsByWalletIdAndRef(Long walletId, String ref);

    List<Ledger> findTop20ByWalletUserUsernameOrderByIdDesc(String username);

    Optional<Object> findByRef(String ref);
}

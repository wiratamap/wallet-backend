package com.company.wallet.wallets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WalletsRepository extends JpaRepository<Wallet, UUID> {
    List<Wallet> findAllByAccount_Id(UUID accountId);
}

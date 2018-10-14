package com.company.wallet.transactionhistories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionHistoriesRepository extends JpaRepository<TransactionHistory, UUID> {
    List<TransactionHistory> findAllByAccount_Id(UUID accountId);
    List<TransactionHistory> findTop5ByAccount_IdOrderByTransactionDateDesc(UUID accountId);
}

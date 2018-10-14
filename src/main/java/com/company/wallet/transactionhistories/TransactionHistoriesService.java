package com.company.wallet.transactionhistories;

import com.company.wallet.accounts.Account;
import com.company.wallet.wallets.TransactionType;
import com.company.wallet.wallets.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionHistoriesService {

    private final TransactionHistoriesRepository transactionHistoriesRepository;


    @Autowired
    public TransactionHistoriesService(TransactionHistoriesRepository transactionHistoriesRepository) {
        this.transactionHistoriesRepository = transactionHistoriesRepository;
    }

    public TransactionHistory create(Wallet walletInformation, Account accountInformation, TransactionType transactionType, double amount) {
        Date transactionDate = new Date();

        TransactionHistory transactionHistory = new TransactionHistory(amount, transactionType, transactionDate, walletInformation, accountInformation);

        return transactionHistoriesRepository.save(transactionHistory);
    }

    public List<TransactionHistory> fetchAllByAccountId(UUID accountId) {
        List<TransactionHistory> transactionHistoresToBeSearch = transactionHistoriesRepository.findTop5ByAccount_IdOrderByTransactionDateDesc(accountId);

        if (transactionHistoresToBeSearch.isEmpty()) {
            throw new TransactionHistoryNotFound();
        }

        return transactionHistoresToBeSearch;
    }
}

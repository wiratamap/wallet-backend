package com.company.wallet.transactionhistoriestest;

import com.company.wallet.accounts.Account;
import com.company.wallet.accounts.AccountsRepository;
import com.company.wallet.transactionhistories.TransactionHistoriesRepository;
import com.company.wallet.transactionhistories.TransactionHistoriesService;
import com.company.wallet.transactionhistories.TransactionHistory;
import com.company.wallet.transactionhistories.TransactionHistoryNotFound;
import com.company.wallet.wallets.Wallet;
import com.company.wallet.wallets.WalletsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.company.wallet.wallets.TransactionType.CREDIT;
import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TransactionHistoriesServiceTest {

    @Autowired
    TransactionHistoriesService transactionHistoriesService;

    @Autowired
    WalletsRepository walletsRepository;

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    TransactionHistoriesRepository transactionHistoriesRepository;

    @After
    public void cleanUp() {
        transactionHistoriesRepository.deleteAll();
        walletsRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    @Test
    public void create_expectTransactionHistoryCreated() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsRepository.save(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        walletsRepository.save(aWallet);

        transactionHistoriesService.create(aWallet, anAccount, CREDIT, 5000);

        assertEquals(1, transactionHistoriesRepository.count());
    }

    @Test
    public void fetchAllByAccountId_expectAllTransactionHistories() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsRepository.save(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        walletsRepository.save(aWallet);

        List<TransactionHistory> expectedTransactionHistories = new ArrayList<>();
        TransactionHistory savedTransactionHistory = transactionHistoriesService.create(aWallet, anAccount, CREDIT, 5000);
        expectedTransactionHistories.add(savedTransactionHistory);

        assertEquals(expectedTransactionHistories, transactionHistoriesService.fetchAllByAccountId(anAccount.getId()));
    }

    @Test(expected = TransactionHistoryNotFound.class)
    public void fetchAllByAccountId_expectTransactionHistoryNotFound() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsRepository.save(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        walletsRepository.save(aWallet);
        transactionHistoriesService.fetchAllByAccountId(anAccount.getId());
    }

}

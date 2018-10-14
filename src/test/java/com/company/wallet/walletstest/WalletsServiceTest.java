package com.company.wallet.walletstest;

import com.company.wallet.accounts.Account;
import com.company.wallet.accounts.AccountNotFoundException;
import com.company.wallet.accounts.AccountsRepository;
import com.company.wallet.accounts.AccountsService;
import com.company.wallet.transactionhistories.TransactionHistoriesRepository;
import com.company.wallet.wallets.*;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static com.company.wallet.wallets.TransactionType.CREDIT;
import static com.company.wallet.wallets.TransactionType.DEBIT;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WalletsServiceTest {

    @Autowired
    WalletsService walletsService;

    @Autowired
    WalletsRepository walletsRepository;

    @Autowired
    AccountsService accountsService;

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
    public void create_expectWalletsSuccessfullyCreated() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        Account savedAccount = accountsService.create(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);

        assertEquals(aWallet, walletsService.create(aWallet, savedAccount.getId()));
    }

    @Test(expected = AccountNotFoundException.class)
    public void create_expectPersonNotFound_whenPersonDoesNotExist() {
        Wallet aWallet = new Wallet(10000, null);

        walletsService.create(aWallet, UUID.randomUUID());
    }

    @Test
    public void fetch_expectSuccessfullyRetrieveParticularWalletBalance() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsService.create(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        walletsRepository.save(aWallet);

        assertEquals(aWallet, walletsService.fetch(aWallet.getId()));
    }

    @Test
    public void fetchAllByAccountId_expectWalletsOfParticularAccount() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsService.create(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        Wallet anotherWallet = new Wallet(15000, anAccount);
        walletsRepository.save(aWallet);
        walletsRepository.save(anotherWallet);

        assertEquals(2, walletsService.fetchAllByAccountId(anAccount.getId()).size());
    }

    @Test(expected = WalletNotFoundException.class)
    public void fetchAllByAccountId_expectWalletsAreNotFound_whenNoWalletForParticularAccount() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsService.create(anAccount);

        walletsService.fetchAllByAccountId(anAccount.getId());
    }

    @Test(expected = WalletNotFoundException.class)
    public void fetch_expectWalletNotFound_whenWalletIsNotFound() {
        walletsService.fetch(UUID.randomUUID());
    }

    @Test
    public void update_expectBalanceAmount15000_whenCreditAmountIs5000() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsService.create(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        walletsRepository.save(aWallet);

        Wallet updatedBalance = walletsService.update(aWallet.getId(), CREDIT, 5000);

        assertEquals(walletsRepository.findById(aWallet.getId()).get(), updatedBalance);
    }

    @Test(expected = InsufficientBalanceException.class)
    public void update_expectInsufficientBalance_whenDebitAmountMoreThanBalance() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsService.create(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        walletsRepository.save(aWallet);

        walletsService.update(aWallet.getId(), DEBIT, 20000);
    }

}

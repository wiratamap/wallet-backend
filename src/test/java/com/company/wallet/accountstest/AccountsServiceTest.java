package com.company.wallet.accountstest;

import com.company.wallet.accounts.Account;
import com.company.wallet.accounts.AccountNotFoundException;
import com.company.wallet.accounts.AccountsRepository;
import com.company.wallet.accounts.AccountsService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountsServiceTest {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private AccountsRepository accountsRepository;

    @After
    public void cleanUp() {
        accountsRepository.deleteAll();
    }

    @Test
    public void create_expectPersonSuccessfullyCreated() {
        Account newAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");

        assertEquals(newAccount, accountsService.create(newAccount));
    }

    @Test
    public void update_expectPersonSuccessfullyUpdated() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        Account savedAccount = accountsRepository.save(anAccount);

        Account newAccountData = new Account("Wiratama P", "Wiratama.Paramasatya@btpn.com");
        Account updatedAccount = accountsService.update(savedAccount.getId(), newAccountData);

        assertEquals(accountsRepository.findById(savedAccount.getId()).get(), updatedAccount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void update_expectPersonFailToUpdate_whenPersonNotFound() {
        Account newAccountData = new Account("Wiratama P", "Wiratama.Paramasatya@btpn.com");
        accountsService.update(UUID.randomUUID(), newAccountData);
    }

    @Test
    public void fetch_successfullyFetchWiratamaParamasatyaPersonInformation() {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        Account savedAccount = accountsRepository.save(anAccount);

        assertEquals(savedAccount, accountsService.fetch(savedAccount.getId()));
    }

    @Test(expected = AccountNotFoundException.class)
    public void fetch_throwsPersonNotFound_whenPersonNotFound() {
        accountsService.fetch(UUID.randomUUID());
    }

}

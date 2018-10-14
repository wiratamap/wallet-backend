package com.company.wallet.walletstest;

import com.company.wallet.accounts.Account;
import com.company.wallet.accounts.AccountsRepository;
import com.company.wallet.transactionhistories.TransactionHistoriesRepository;
import com.company.wallet.wallets.Wallet;
import com.company.wallet.wallets.WalletsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class WalletsControllerTest {

    @Autowired
    MockMvc mockMvc;

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

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void create_expectStatusCreated() throws Exception {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsRepository.save(anAccount);

        Wallet newWallet = new Wallet(10000, anAccount);

        this.mockMvc.perform(post("/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .param("account_id", String.valueOf(anAccount.getId()))
                .content(objectMapper.writeValueAsString(newWallet)))
                .andExpect(status().isCreated());
    }

    @Test
    public void create_expectUserIsNotFound() throws Exception {
        Wallet newWallet = new Wallet(10000, null);

        this.mockMvc.perform(post("/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .param("account_id", String.valueOf(UUID.randomUUID()))
                .content(objectMapper.writeValueAsString(newWallet)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fetch_expectStatusOk() throws Exception {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsRepository.save(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        Wallet savedWallet = walletsRepository.save(aWallet);

        this.mockMvc.perform(get("/wallets/" + savedWallet.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedWallet)));
    }

    @Test
    public void fetch_expectStatusWalletNotFound() throws Exception {
        this.mockMvc.perform(get("/wallets/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fetchAllByAccountId_expectStatusOk() throws Exception {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        Account savedAccount = accountsRepository.save(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        walletsRepository.save(aWallet);

        Wallet anotherWallet = new Wallet(10000, anAccount);
        walletsRepository.save(anotherWallet);

        this.mockMvc.perform(get("/wallets")
                .param("account_id", String.valueOf(savedAccount.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void fetchAllByAccountId_expectStatusWalletsNotFound_whenParticularAccountDoesNotHaveWallet() throws Exception {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        Account savedAccount = accountsRepository.save(anAccount);

        this.mockMvc.perform(get("/wallets")
                .param("account_id", String.valueOf(savedAccount.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void update_expectStatusAccepted_whenSuccessfullyCreditSomeAmount() throws Exception {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsRepository.save(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        Wallet savedWallet = walletsRepository.save(aWallet);

        this.mockMvc.perform(post("/wallets/" + savedWallet.getId())
                .param("transaction_type", "CREDIT")
                .param("amount", String.valueOf(5000)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void update_expectStatusNotAcceptable_whenBalanceIsInsufficient() throws Exception {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        accountsRepository.save(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        Wallet savedWallet = walletsRepository.save(aWallet);

        this.mockMvc.perform(post("/wallets/" + savedWallet.getId())
                .param("transaction_type", "DEBIT")
                .param("amount", String.valueOf(20000)))
                .andExpect(status().isNotAcceptable());
    }
}

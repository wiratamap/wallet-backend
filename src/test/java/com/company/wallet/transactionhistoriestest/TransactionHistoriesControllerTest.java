package com.company.wallet.transactionhistoriestest;

import com.company.wallet.accounts.Account;
import com.company.wallet.accounts.AccountsRepository;
import com.company.wallet.transactionhistories.TransactionHistoriesRepository;
import com.company.wallet.transactionhistories.TransactionHistoriesService;
import com.company.wallet.wallets.Wallet;
import com.company.wallet.wallets.WalletsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.company.wallet.wallets.TransactionType.CREDIT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TransactionHistoriesControllerTest {

    @Autowired
    TransactionHistoriesService transactionHistoriesService;

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

    @Test
    public void fetchAllByAccountId_expectStatusOk() throws Exception {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        Account savedAccount = accountsRepository.save(anAccount);

        Wallet aWallet = new Wallet(10000, anAccount);
        walletsRepository.save(aWallet);

        Wallet anotherWallet = new Wallet(10000, anAccount);
        walletsRepository.save(anotherWallet);

        transactionHistoriesService.create(anotherWallet, aWallet.getAccount(), CREDIT, 5000);

        this.mockMvc.perform(get("/transaction-histories")
                .param("account_id", String.valueOf(savedAccount.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void fetchAllByAccountId_expectStatusTransactionHistoryNotFound_whenParticularAccountDoesNotHaveTransaction() throws Exception {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        Account savedAccount = accountsRepository.save(anAccount);

        this.mockMvc.perform(get("/transaction-histories")
                .param("account_id", String.valueOf(savedAccount.getId())))
                .andExpect(status().isNotFound());
    }

}

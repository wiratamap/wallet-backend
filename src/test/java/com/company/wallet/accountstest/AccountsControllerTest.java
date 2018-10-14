package com.company.wallet.accountstest;

import com.company.wallet.accounts.Account;
import com.company.wallet.accounts.AccountsRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AccountsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountsRepository accountsRepository;

    @After
    public void cleanUp() {
        accountsRepository.deleteAll();
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void create_expectStatusCreated() throws Exception {
        Account newAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");

        this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated());
    }

    @Test
    public void update_expectStatusIsAccepted() throws Exception {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        Account savedAccount = accountsRepository.save(anAccount);

        Account newAccountData = new Account("Wiratama P", "Wiratama.Paramasatya@btpn.com");

        this.mockMvc.perform(put("/accounts/" + savedAccount.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAccountData)))
                .andExpect(status().isAccepted());

    }

    @Test
    public void update_expectPersonFailToUpdate_whenPersonNotFound() throws Exception {
        Account newAccountData = new Account("Wiratama P", "Wiratama.Paramasatya@btpn.com");

        this.mockMvc.perform(put("/accounts/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAccountData)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fetch_expectStatusIsOk() throws Exception {
        Account anAccount = new Account("Wiratama Paramasatya", "wiratamaparamasatya@gmail.com");
        Account savedAccount = accountsRepository.save(anAccount);

        this.mockMvc.perform(get("/accounts/" + savedAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedAccount)));
    }

    @Test
    public void fetch_expectStatusIsNotFound() throws Exception {
        this.mockMvc.perform(get("/accounts/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}

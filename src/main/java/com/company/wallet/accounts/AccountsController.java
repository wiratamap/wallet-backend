package com.company.wallet.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountsService accountsService;

    @Autowired
    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> fetch(@PathVariable(value = "id") UUID id)  {
        Account responseBody = accountsService.fetch(id);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account newAccount) {
        Account responseBody = accountsService.create(newAccount);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> update(@PathVariable(value = "id") UUID id, @RequestBody Account newAccountData) {
        Account responseBody = accountsService.update(id, newAccountData);

        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }

}

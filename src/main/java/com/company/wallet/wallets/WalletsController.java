package com.company.wallet.wallets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletsController {

    private final WalletsService walletsService;

    @Autowired
    public WalletsController(WalletsService walletsService) {
        this.walletsService = walletsService;
    }

    @PostMapping
    public ResponseEntity<Wallet> create(@RequestBody Wallet newWallet, @RequestParam(value = "account_id") UUID accountId) {
        Wallet responseBody = walletsService.create(newWallet, accountId);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @GetMapping("/{wallet_id}")
    public ResponseEntity<Wallet> fetch(@PathVariable(value = "wallet_id") UUID walletId) {
        Wallet responseBody = walletsService.fetch(walletId);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Wallet>> fetchAllByAccountId(@RequestParam(value = "account_id") UUID accountId) {
        List<Wallet> responseBody = walletsService.fetchAllByAccountId(accountId);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/{wallet_id}")
    public ResponseEntity<Wallet> update(@PathVariable(value = "wallet_id") UUID walletId, @RequestParam(value = "transaction_type") TransactionType transactionType, @RequestParam(value = "amount") double amount) {
        Wallet responseBody = walletsService.update(walletId, transactionType, amount);

        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }
}

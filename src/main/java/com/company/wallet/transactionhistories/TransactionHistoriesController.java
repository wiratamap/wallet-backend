package com.company.wallet.transactionhistories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction-histories")
public class TransactionHistoriesController {

    private final TransactionHistoriesService transactionHistoriesService;

    @Autowired
    public TransactionHistoriesController(TransactionHistoriesService transactionHistoriesService) {
        this.transactionHistoriesService = transactionHistoriesService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionHistory>> fetchByAccountId(@RequestParam(value = "account_id") UUID accountId) {
        List<TransactionHistory> responseBody = transactionHistoriesService.fetchAllByAccountId(accountId);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}

package com.company.wallet.transactionhistories;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Transaction history not found.")
public class TransactionHistoryNotFound extends RuntimeException {
}

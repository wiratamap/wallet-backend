package com.company.wallet.wallets;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Insufficient balance, Transaction failed.")
public class InsufficientBalanceException extends RuntimeException {
}

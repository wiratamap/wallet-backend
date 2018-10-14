package com.company.wallet.wallets;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Wallet is not found.")
public class WalletNotFoundException extends RuntimeException {
}

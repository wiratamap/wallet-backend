package com.company.wallet.accounts;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Account is not found.")
public class AccountNotFoundException extends RuntimeException {
}

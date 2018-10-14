package com.company.wallet.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountsService {

    private final AccountsRepository accountsRepository;

    @Autowired
    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public Account create(Account newAccount) {
        return accountsRepository.save(newAccount);
    }

    public Account update(UUID id, Account newAccountData) {
        Account updateAbleAccount = fetch(id);

        updateAbleAccount.updateTo(newAccountData);

        return accountsRepository.save(updateAbleAccount);
    }

    public Account fetch(UUID id) {
        Optional<Account> personToBeSearch = accountsRepository.findById(id);

        if (personToBeSearch.isPresent()) {
            return personToBeSearch.get();
        }

        throw new AccountNotFoundException();
    }
}

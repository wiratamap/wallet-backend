package com.company.wallet.wallets;

import com.company.wallet.accounts.Account;
import com.company.wallet.accounts.AccountsService;
import com.company.wallet.transactionhistories.TransactionHistoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletsService {

    private final WalletsRepository walletsRepository;
    private final AccountsService accountsService;
    private final TransactionHistoriesService transactionHistoriesService;

    @Autowired
    public WalletsService(WalletsRepository walletsRepository, AccountsService accountsService, TransactionHistoriesService transactionHistoriesService) {
        this.walletsRepository = walletsRepository;
        this.accountsService = accountsService;
        this.transactionHistoriesService = transactionHistoriesService;
    }

    public Wallet create(Wallet newWallet, UUID accountId) {
        Account account = accountsService.fetch(accountId);

        newWallet.mapWalletInto(account);

        return walletsRepository.save(newWallet);
    }

    public Wallet fetch(UUID walletId) {
        Optional<Wallet> walletToBeSearch = walletsRepository.findById(walletId);

        if (walletToBeSearch.isPresent()) {
            return walletToBeSearch.get();
        }

        throw new WalletNotFoundException();
    }

    public List<Wallet> fetchAllByAccountId(UUID accountId) {
        accountsService.fetch(accountId);

        List<Wallet> walletsToBeSearch = walletsRepository.findAllByAccount_Id(accountId);

        if (walletsToBeSearch.isEmpty()) {
            throw new WalletNotFoundException();
        }

        return walletsToBeSearch;
    }

    public Wallet update(UUID walletId, TransactionType transactionType, double amount) {
        Wallet aWallet = fetch(walletId);

        transactionType.updateBalance(aWallet, amount);

        transactionHistoriesService.create(aWallet, aWallet.getAccount(), transactionType, amount);

        return walletsRepository.save(aWallet);
    }
}

package com.company.wallet.wallets;

public enum TransactionType {
    CREDIT {
        @Override
        public void updateBalance(Wallet aWallet, double amount) {
            aWallet.credit(amount);
        }
    },
    DEBIT {
        @Override
        public void updateBalance(Wallet aWallet, double amount) {
            aWallet.debit(amount);
        }
    };

    public abstract void updateBalance(Wallet aWallet, double amount);
}

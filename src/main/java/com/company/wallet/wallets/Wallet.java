package com.company.wallet.wallets;

import com.company.wallet.accounts.Account;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column
    private double balance;

    @ManyToOne
    private Account account;

    public Wallet(double balance, Account account) {
        this.balance = balance;
        this.account = account;
    }

    public Wallet() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Double.compare(wallet.balance, balance) == 0 &&
                Objects.equals(id, wallet.id) &&
                Objects.equals(account, wallet.account);
    }

    public void mapWalletInto(Account account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, account);
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", balance=" + balance +
                ", account=" + account +
                '}';
    }

    void credit(double amount) {
        this.balance += amount;
    }

    void debit(double amount) {
        if (this.balance < amount) {
            throw new InsufficientBalanceException();
        }

        this.balance -= amount;
    }
}

package com.revolut.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private final List<IAccount> accounts = new ArrayList<>();

    /**
     * Constructor that will also seed the DAO with initial accounts
     */
    public AccountDao() {
        this.accounts.add(new Account("6ba581fb-36c3-46bb-a37d-e8123d5f3e96", "12345678", "123456", "Mr A Test", BigDecimal.ONE));
        this.accounts.add(new Account("aba46032-b2f8-4f09-a8e1-a66665640a05", "23456789", "234567", "Mrs B Test", BigDecimal.TEN));
        this.accounts.add(new OverdraftAccount("ccae6d36-abaf-4620-89ee-732272a5958f", "34567890", "345678", "Dr C Test", BigDecimal.ZERO, BigDecimal.TEN));
    }

    List<IAccount> fetchAll() {
        return this.accounts;
    }

    public IAccount findAccountByUid(String accountUid) {
        synchronized (this.accounts) {
            return this.accounts
                    .stream()
                    .filter(a -> a.uid().equals(accountUid))
                    .findFirst()
                    .orElseThrow();
        }
    }

    public IAccount findAccountByAccountDetails(String accountNo, String sortCode) {
        synchronized (this.accounts) {
            return this.accounts
                    .stream()
                    .filter(a -> a.accountNumber().equals(accountNo) &&
                            a.sortCode().equals(sortCode))
                    .findFirst()
                    .orElseThrow();
        }
    }

    public void save(IAccount account) {
        synchronized (this.accounts) {
            this.accounts.remove(account);
            this.accounts.add(account);
        }
    }
}

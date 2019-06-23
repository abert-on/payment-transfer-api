package com.revolut.account;

import com.revolut.util.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.Objects;

public class Account implements IAccount{

    private String uid;
    private String accountNumber;
    private String sortCode;
    private String name;
    private BigDecimal balance;


    Account(String uid, String accountNumber, String sortCode, String name, BigDecimal balance) {
        this.uid = uid;
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String uid() {
        return uid;
    }

    @Override
    public String accountNumber() {
        return accountNumber;
    }

    @Override
    public String sortCode() {
        return sortCode;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public BigDecimal currentBalance() {
        synchronized (this.balance) {
            return this.balance;
        }
    }


    @Override
    public boolean balanceCoversAmount(BigDecimal paymentAmount) {
        return this.balance.doubleValue() >= paymentAmount.doubleValue();
    }

    @Override
    public void transferOut(BigDecimal amount) throws InsufficientFundsException {
        synchronized (this.balance) {
            if (!balanceCoversAmount(amount)) {
                throw new InsufficientFundsException("Insufficien funds in account : " + uid());
            }
            this.balance = this.balance.subtract(amount);
        }
    }


    @Override
    public void transferIn(BigDecimal amount) {
        synchronized (this.balance) {
            this.balance = this.balance.add(amount);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return uid.equals(account.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}

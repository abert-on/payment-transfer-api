package com.revolut.account;

import com.revolut.util.InsufficientFundsException;

import java.math.BigDecimal;

public interface IAccount {


    String uid();

    String accountNumber();

    String sortCode();

    String name();

    BigDecimal currentBalance();

    /**
     * Check that this account has a high enough balance to cover a given transfer amount
     * @param paymentAmount the amount to cover
     * @return true if current balance is grater than given amount
     */
    boolean balanceCoversAmount(BigDecimal paymentAmount);

    /**
     * Reduces the current balance by a given transfer amount
     * @param amount the amount to transfer out of the account
     * @throws InsufficientFundsException if the current balance isn't enough to cover the transfer amount
     */
    void transferOut(BigDecimal amount) throws InsufficientFundsException;

    /**
     * Increases the current balance by a given transfer amount
     * @param amount the amount to transfer in to the account
     */
    void transferIn(BigDecimal amount);
}

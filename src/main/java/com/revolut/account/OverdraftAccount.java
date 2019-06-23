package com.revolut.account;

import java.math.BigDecimal;

public class OverdraftAccount extends Account {

    private final BigDecimal overDraft;

    public OverdraftAccount(String uid, String accountNumber, String sortCode, String name, BigDecimal balance, BigDecimal overdraft) {
        super(uid, accountNumber, sortCode, name, balance);
        this.overDraft = overdraft;
    }

    /**
     * Check that this account has a high enough balance (including overdraft) to cover a given transfer amount
     * @param paymentAmount the amount to cover
     ** @return true if current balance is grater than given amount
     */
    @Override
    public boolean balanceCoversAmount(BigDecimal paymentAmount) {
        return currentBalance().add(this.overDraft).doubleValue() >= paymentAmount.doubleValue();
    }
}

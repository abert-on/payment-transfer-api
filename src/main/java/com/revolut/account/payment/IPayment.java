package com.revolut.account.payment;

import java.math.BigDecimal;

public interface IPayment {

    String uid();

    String accountNumber();

    String sortCode();

    BigDecimal amount();

    EPaymentStatus status();

    void setStatus(EPaymentStatus status);

    void save();
}

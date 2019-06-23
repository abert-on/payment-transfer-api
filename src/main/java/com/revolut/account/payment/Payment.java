package com.revolut.account.payment;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static com.revolut.Application.paymentDao;

public class Payment implements IPayment {

    private String uid;
    private String accountNumber;
    private String sortCode;
    private BigDecimal amount;
    private EPaymentStatus status;

    Payment(String accountNumber, String sortCode, BigDecimal amount) {
        this.uid = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.amount = amount;
    }

    @Override
    public String uid() {
        return this.uid;
    }

    @Override
    public String accountNumber() {
        return this.accountNumber;
    }

    @Override
    public String sortCode() {
        return this.sortCode;
    }

    @Override
    public BigDecimal amount() {
        return this.amount;
    }

    @Override
    public EPaymentStatus status() {
        return this.status;
    }

    @Override
    public void setStatus(EPaymentStatus status) {
        this.status = status;
    }

    @Override
    public void save() {
        if (this.uid == null) {
            this.uid = UUID.randomUUID().toString();
        }
        paymentDao.save(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return uid.equals(payment.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}

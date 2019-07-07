package com.revolut.account.payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentDao {

    private final List<IPayment> payments = new ArrayList<>();

    void save(IPayment payment) {
        synchronized (this.payments) {
            this.payments.remove(payment);
            this.payments.add(payment);
        }
    }


}

package com.revolut.account.payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentDao {

    private final List<IPayment> payments = new ArrayList<>();

    List<IPayment> fetchAllPayments() {
        synchronized (this.payments) {
            return new ArrayList<>(payments);
        }
    }

    void save(IPayment payment) {
        synchronized (this.payments) {
            this.payments.remove(payment);
            this.payments.add(payment);
        }
    }


}

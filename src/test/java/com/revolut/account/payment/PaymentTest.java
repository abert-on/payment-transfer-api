package com.revolut.account.payment;

import com.revolut.Application;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentTest {

    @Test
    public void testPaymentCreation() {
        // test
        Payment sut = new Payment("12345678", "123456", BigDecimal.TEN);

        // verify
        assertThat(sut.uid()).isNotNull();
        assertThat(sut.accountNumber()).isEqualTo("12345678");
        assertThat(sut.sortCode()).isEqualTo("123456");
        assertThat(sut.amount()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    public void testSetStatus() {
        // setup
        Payment sut = new Payment("12345678", "123456", BigDecimal.TEN);

        // test
        sut.setStatus(EPaymentStatus.COMPLETE);

        // verify
        assertThat(sut.status()).isEqualTo(EPaymentStatus.COMPLETE);
    }

    @Test

    public void testSave() {
        // setup
        Payment sut = new Payment("TEST", "TEST", BigDecimal.TEN);

        // test
        sut.save();

        // verify
        assertThat(Application.paymentDao.fetchAllPayments()).contains(sut);
    }

}
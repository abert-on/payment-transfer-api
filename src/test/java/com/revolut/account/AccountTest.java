package com.revolut.account;

import com.revolut.util.InsufficientFundsException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class AccountTest {

    @Test
    public void testAccountCreation() {
        // setup
        String uid = UUID.randomUUID().toString();

        // test
        Account sut = new Account(uid, "12345678", "123456", "Bob", BigDecimal.TEN);

        // verify
        assertThat(sut.uid()).isEqualTo(uid);
        assertThat(sut.accountNumber()).isEqualTo("12345678");
        assertThat(sut.sortCode()).isEqualTo("123456");
        assertThat(sut.name()).isEqualTo("Bob");
        assertThat(sut.currentBalance()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    public void testBalanceCoversAmount() {
        // setup
        Account sut = new Account(UUID.randomUUID().toString(), "12345678", "123456", "Bob", BigDecimal.TEN);

        // test
        assertThat(sut.balanceCoversAmount(BigDecimal.ONE)).isTrue();
        assertThat(sut.balanceCoversAmount(new BigDecimal(200))).isFalse();
    }

    @Test
    public void testTransferOut() throws InsufficientFundsException {
        // setup
        Account sut = new Account(UUID.randomUUID().toString(), "12345678", "123456", "Bob", BigDecimal.TEN);

        // test
        sut.transferOut(BigDecimal.ONE);

        // verify
        assertThat(sut.currentBalance()).isEqualTo(new BigDecimal(9));
    }

    @Test(expected = InsufficientFundsException.class)
    public void testTransferOutInsuffcientFunds() throws InsufficientFundsException {
        // setup
        Account sut = new Account(UUID.randomUUID().toString(), "12345678", "123456", "Bob", BigDecimal.ZERO);

        // test
        sut.transferOut(BigDecimal.ONE);

        // verify
        fail("InsufficientFundsException should have been thrown");
    }

    @Test
    public void testTransferIn() {
        // setup
        Account sut = new Account(UUID.randomUUID().toString(), "12345678", "123456", "Bob", BigDecimal.TEN);

        // test
        sut.transferIn(BigDecimal.ONE);

        // verify
        assertThat(sut.currentBalance()).isEqualTo(new BigDecimal(11));
    }
}

package com.revolut.account;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class AccountDaoTest {

    @Test
    public void testFetchAll() {
        // setup
        AccountDao sut = new AccountDao();

        // test
        List<IAccount> results = sut.fetchAll();

        // verify
        assertThat(results).hasSize(3);
        assertThat(results).containsExactly(
                new Account("6ba581fb-36c3-46bb-a37d-e8123d5f3e96", "12345678", "123456", "Mr A Test", BigDecimal.ONE),
                new Account("aba46032-b2f8-4f09-a8e1-a66665640a05", "23456789", "234567", "Mrs B Test", BigDecimal.TEN),
                new OverdraftAccount("ccae6d36-abaf-4620-89ee-732272a5958f", "34567890", "345678", "Dr C Test", BigDecimal.ZERO, BigDecimal.TEN)
        );
    }

    @Test
    public void testFindAccountByUid() {
        // setup
        AccountDao sut = new AccountDao();

        // test
        IAccount result = sut.findAccountByUid("6ba581fb-36c3-46bb-a37d-e8123d5f3e96");

        // verify
        assertThat(result).isEqualTo(new Account("6ba581fb-36c3-46bb-a37d-e8123d5f3e96", "12345678", "123456", "Mr A Test", BigDecimal.ONE));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFindAccountByUidNotFound() {
        // setup
        AccountDao sut = new AccountDao();

        // test
        sut.findAccountByUid("nonsense");

        // verify
        fail("No account should be found");
    }

    @Test
    public void testFindAccountByDetails() {
        // setup
        AccountDao sut = new AccountDao();

        // test
        IAccount result = sut.findAccountByAccountDetails("12345678", "123456");

        // verify
        assertThat(result).isEqualTo(new Account("6ba581fb-36c3-46bb-a37d-e8123d5f3e96", "12345678", "123456", "Mr A Test", BigDecimal.ONE));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFindAccountByDetailsNoAccountNumberMatch() {
        // setup
        AccountDao sut = new AccountDao();

        // test
        IAccount result = sut.findAccountByAccountDetails("xxxxxxxx", "123456");

        // verify
        fail("No account should be found");
    }

    @Test(expected = NoSuchElementException.class)
    public void testFindAccountByDetailsNoSortCodeMatch() {
        // setup
        AccountDao sut = new AccountDao();

        // test
        IAccount result = sut.findAccountByAccountDetails("12345678", "xxxxxx");

        // verify
        fail("No account should be found");
    }

    @Test
    public void testSave() {
        // setup
        AccountDao sut = new AccountDao();
        Account newAcc = new Account("Test", "xxxxxxxx", "xxxxxx", "Bob", BigDecimal.ZERO);

        // test
        sut.save(newAcc);

        // verify
        assertThat(sut.fetchAll()).hasSize(4);
        assertThat(sut.fetchAll()).contains(newAcc);
    }
}

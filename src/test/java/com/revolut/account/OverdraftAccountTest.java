package com.revolut.account;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class OverdraftAccountTest {

    @Test
    public void testOverdraftCountsTowardsBalance() {
        // setup
        OverdraftAccount sut = new OverdraftAccount(
                "Test",
                "xxxxxxxx",
                "xxxxxx",
                "name",
                BigDecimal.ZERO, // balance of 0
                BigDecimal.TEN); // overdraft of 10

        // test
        boolean result = sut.balanceCoversAmount(BigDecimal.TEN);

        // verify
        assertThat(result).isTrue();
    }

    @Test
    public void testNotEnoughOverdraft() {
        // setup
        OverdraftAccount sut = new OverdraftAccount(
                "Test",
                "xxxxxxxx",
                "xxxxxx",
                "name",
                BigDecimal.ZERO, // balance of 0
                BigDecimal.ONE); // overdraft of 1

        // test
        boolean result = sut.balanceCoversAmount(BigDecimal.TEN);

        // verify
        assertThat(result).isFalse();
    }

}
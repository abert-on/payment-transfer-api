package com.revolut.account.payment;

import com.revolut.Application;
import com.revolut.util.TestUtils;
import org.junit.*;
import spark.Spark;

import java.io.IOException;

import static com.revolut.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentControllerTest {

    @Before
    public void before() {
        Application.main(null);
        Spark.awaitInitialization();
    }

    @After
    public void after() {
        Spark.stop();
        Spark.awaitStop();
    }

    @Test
    public void testTransferBetweenInternalAccounts() throws IOException {
        // setup
        String requestBody = "{\"accountNumber\" : \"23456789\", \"sortCode\" : \"234567\", \"amount\" : 0.45}";

        // test
        TestResponse response = request(
                "PUT",
                "/accounts/6ba581fb-36c3-46bb-a37d-e8123d5f3e96/transfer",
                requestBody);

        // verify
        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).matches(TestUtils.getExpectedBody(this.getClass(), "internalSuccess.json"));

        // check to and from account balances updated
        TestResponse fromAccount = request("GET", "/accounts/6ba581fb-36c3-46bb-a37d-e8123d5f3e96");
        assertThat(fromAccount.body).contains("\"balance\":0.55");

        TestResponse toAccount = request("GET", "/accounts/aba46032-b2f8-4f09-a8e1-a66665640a05");
        assertThat(toAccount.body).contains("\"balance\":10.45");
    }

    @Test
    public void testTransferBetweenExternalAccounts() throws IOException {
        // setup
        String requestBody = "{\"accountNumber\" : \"87654321\", \"sortCode\" : \"654321\", \"amount\" : 0.45}";

        // test
        TestResponse response = request(
                "PUT",
                "/accounts/6ba581fb-36c3-46bb-a37d-e8123d5f3e96/transfer",
                requestBody);

        // verify
        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).matches(TestUtils.getExpectedBody(this.getClass(), "externalSuccess.json"));

        // check from account balances updated
        TestResponse fromAccount = request("GET", "/accounts/6ba581fb-36c3-46bb-a37d-e8123d5f3e96");
        assertThat(fromAccount.body).contains("\"balance\":0.55");
    }

    @Test
    public void testTransferFromAccountNotFound() throws IOException {
        // setup
        String requestBody = "{\"accountNumber\" : \"nonsense\", \"sortCode\" : \"234567\", \"amount\" : 123.45}";

        // test
        TestResponse response = request(
                "PUT",
                "/accounts/nonsense/transfer",
                requestBody);

        // verify
        assertThat(response.status).isEqualTo(404);
        assertThat(response.body).isEqualTo("No account found with id nonsense");
    }

    @Test
    public void testTransferFromAccountInsufficientFunds() throws IOException {
        // setup
        String requestBody = "{\"accountNumber\" : \"23456789\", \"sortCode\" : \"234567\", \"amount\" : 123.45}";

        // test
        TestResponse response = request(
                "PUT",
                "/accounts/6ba581fb-36c3-46bb-a37d-e8123d5f3e96/transfer",
                requestBody);

        // verify
        assertThat(response.status).isEqualTo(400);
        assertThat(response.body).isEqualTo("Insufficient funds for transfer from account: 6ba581fb-36c3-46bb-a37d-e8123d5f3e96");
    }

    @Test
    public void testTransferFromAccountSufficientFundsWithOverdraft() throws IOException {
        // setup
        String requestBody = "{\"accountNumber\" : \"23456789\", \"sortCode\" : \"234567\", \"amount\" : 1.0}";

        // test
        TestResponse response = request(
                "PUT",
                "/accounts/ccae6d36-abaf-4620-89ee-732272a5958f/transfer",
                requestBody);

        // verify
        assertThat(response.status).isEqualTo(200);

        // check from account balances updated
        TestResponse fromAccount = request("GET", "/accounts/ccae6d36-abaf-4620-89ee-732272a5958f");
        assertThat(fromAccount.body).contains("\"balance\":-1.0");
    }

}
package com.revolut.account;

import com.revolut.Application;
import com.revolut.util.TestUtils.TestResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.io.IOException;

import static com.revolut.util.TestUtils.getExpectedBody;
import static com.revolut.util.TestUtils.request;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountsControllerTest {

    @BeforeClass
    public static void beforeClass() {
        Application.main(null);
        Spark.awaitInitialization();
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
        Spark.awaitStop();
    }

    @Test
    public void testFetchAll() throws IOException {
        // test
        TestResponse response = request("GET", "/accounts");

        // verify
        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).isEqualTo(getExpectedBody(this.getClass(), "fetchAllAccounts.json"));
    }

    @Test
    public void testFetchAccount() throws IOException {
        // test
        TestResponse response = request("GET", "/accounts/6ba581fb-36c3-46bb-a37d-e8123d5f3e96");

        // verify
        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).isEqualTo("{\"uid\":\"6ba581fb-36c3-46bb-a37d-e8123d5f3e96\",\"accountNumber\":\"12345678\",\"sortCode\":\"123456\",\"name\":\"Mr A Test\",\"balance\":1}");
    }

    @Test
    public void testFetchAccountNotFound() throws IOException {
        // test
        TestResponse response = request("GET", "/accounts/nonsense");

        // verify
        assertThat(response.status).isEqualTo(404);
        assertThat(response.body).isEqualTo("No account found with id nonsense");
    }
}

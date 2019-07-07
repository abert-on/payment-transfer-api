package com.revolut;

import com.revolut.account.AccountController;
import com.revolut.account.AccountDao;
import com.revolut.account.payment.PaymentController;
import com.revolut.account.payment.PaymentDao;

import static spark.Spark.get;
import static spark.Spark.put;

public class Application {

    public static void main(String[] args) {
        AccountDao accountDao = new AccountDao();
        AccountController accountController = new AccountController(accountDao);

        PaymentDao paymentDao = new PaymentDao();
        PaymentController paymentController = new PaymentController(accountDao, paymentDao);

        get("/accounts", accountController.fetchAllAccounts);
        get("/accounts/:accountUid", accountController.fetchAccount);
        put("/accounts/:accountUid/transfer", paymentController.transfer);
    }
}

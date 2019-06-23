package com.revolut;

import com.revolut.account.AccountController;
import com.revolut.account.AccountDao;
import com.revolut.account.payment.PaymentController;
import com.revolut.account.payment.PaymentDao;

import static spark.Spark.*;

public class Application {

    public static AccountDao accountDao;
    public static PaymentDao paymentDao;

    public static void main(String[] args) {
        accountDao = new AccountDao();
        paymentDao = new PaymentDao();

        get("/accounts", AccountController.fetchAllAccounts);
        get("/accounts/:accountUid", AccountController.fetchAccount);
        put("/accounts/:accountUid/transfer", PaymentController.transfer);
    }
}

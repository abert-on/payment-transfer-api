package com.revolut.account.payment;

import com.revolut.account.AccountDao;
import com.revolut.account.IAccount;
import com.revolut.util.InsufficientFundsException;
import com.revolut.util.JsonUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.NoSuchElementException;

public class PaymentController {

    private AccountDao accountDao;
    private PaymentDao paymentDao;

    public PaymentController(AccountDao accountDao, PaymentDao paymentDao) {
        this.accountDao = accountDao;
        this.paymentDao = paymentDao;
    }

    public Route transfer = (Request request, Response response) -> {
        String uid = request.params(":accountUid");
        try {
            IAccount fromAccount = this.accountDao.findAccountByUid(uid);
            IPayment payment = JsonUtil.jsonToObject(request.body(), Payment.class);

            return processPayment(response, fromAccount, payment);
        }
        catch (NoSuchElementException exception) {
            response.status(404);
            return "No account found with id " + uid;
        }
    };

    private String processPayment(Response response, IAccount fromAccount, IPayment payment) {
        payment.setStatus(EPaymentStatus.PENDING);

        try {
            updateFromAccount(fromAccount, payment);
        }
        catch (InsufficientFundsException exception) {
            payment.setStatus(EPaymentStatus.FAILED);
            this.paymentDao.save(payment);
            response.status(400);
            return "Insufficient funds for transfer from account: " + fromAccount.uid();
        }

        updateToAccount(payment);

        this.paymentDao.save(payment);
        response.status(200);
        return JsonUtil.objectToJson(payment);
    }

    private  void updateToAccount(IPayment payment) {
        if (transferringToInternalAccount(payment)) {
            IAccount toAccount = this.accountDao.findAccountByAccountDetails(payment.accountNumber(), payment.sortCode());
            toAccount.transferIn(payment.amount());
            this.accountDao.save(toAccount);
            payment.setStatus(EPaymentStatus.COMPLETE);
        }
    }

    private void updateFromAccount(IAccount fromAccount, IPayment payment) throws InsufficientFundsException {
        fromAccount.transferOut(payment.amount());
        this.accountDao.save(fromAccount);
    }

    private boolean transferringToInternalAccount(IPayment payment) {
        try {
            this.accountDao.findAccountByAccountDetails(payment.accountNumber(), payment.sortCode());
            return true;
        }
        catch (NoSuchElementException exception) {
            return false;
        }
    }
}

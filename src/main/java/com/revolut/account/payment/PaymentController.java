package com.revolut.account.payment;

import com.revolut.account.IAccount;
import com.revolut.util.InsufficientFundsException;
import com.revolut.util.JsonUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.NoSuchElementException;

import static com.revolut.Application.accountDao;

public class PaymentController {

    public static Route transfer = (Request request, Response response) -> {
        String uid = request.params(":accountUid");
        try {
            IAccount fromAccount = accountDao.findAccountByUid(uid);
            IPayment payment = JsonUtil.jsonToObject(request.body(), Payment.class);

            return processPayment(response, fromAccount, payment);
        }
        catch (NoSuchElementException exception) {
            response.status(404);
            return "No account found with id " + uid;
        }
    };

    private static String processPayment(Response response, IAccount fromAccount, IPayment payment) {
        payment.setStatus(EPaymentStatus.PENDING);

        try {
            updateFromAccount(fromAccount, payment);
        }
        catch (InsufficientFundsException exception) {
            payment.setStatus(EPaymentStatus.FAILED);
            payment.save();
            response.status(400);
            return "Insufficient funds for transfer from account: " + fromAccount.uid();
        }

        updateToAccount(payment);

        payment.save();
        response.status(200);
        return JsonUtil.objectToJson(payment);
    }

    private static void updateToAccount(IPayment payment) {
        if (transferringToInternalAccount(payment)) {
            IAccount toAccount = accountDao.findAccountByAccountDetails(payment.accountNumber(), payment.sortCode());
            toAccount.transferIn(payment.amount());
            accountDao.save(toAccount);
            payment.setStatus(EPaymentStatus.COMPLETE);
        }
    }

    private static void updateFromAccount(IAccount fromAccount, IPayment payment) throws InsufficientFundsException {
        fromAccount.transferOut(payment.amount());
        accountDao.save(fromAccount);
    }

    private static boolean transferringToInternalAccount(IPayment payment) {
        try {
            accountDao.findAccountByAccountDetails(payment.accountNumber(), payment.sortCode());
            return true;
        }
        catch (NoSuchElementException exception) {
            return false;
        }
    }
}

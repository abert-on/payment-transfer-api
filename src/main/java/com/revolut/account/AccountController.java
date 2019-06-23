package com.revolut.account;

import spark.Request;
import spark.Response;
import spark.Route;
import com.revolut.util.JsonUtil;

import java.util.List;
import java.util.NoSuchElementException;

import static com.revolut.Application.accountDao;

public class AccountController {


    public static Route fetchAllAccounts = (Request request, Response response) -> {
        List<IAccount> accounts = accountDao.fetchAll();
        return JsonUtil.objectToJson(accounts);
    };

    public static Route fetchAccount = (Request request, Response response) -> {
        String uid = request.params(":accountUid");
        try {
            IAccount account = accountDao.findAccountByUid(uid);
            return JsonUtil.objectToJson(account);
        }
        catch (NoSuchElementException exception) {
            response.status(404);
            return "No account found with id " + uid;
        }
    };
}

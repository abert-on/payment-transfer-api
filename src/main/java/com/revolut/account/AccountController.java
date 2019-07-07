package com.revolut.account;

import spark.Request;
import spark.Response;
import spark.Route;
import com.revolut.util.JsonUtil;

import java.util.List;
import java.util.NoSuchElementException;

public class AccountController {

    private AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    public Route fetchAllAccounts = (Request request, Response response) -> {
        List<IAccount> accounts = this.accountDao.fetchAll();
        return JsonUtil.objectToJson(accounts);
    };

    public Route fetchAccount = (Request request, Response response) -> {
        String uid = request.params(":accountUid");
        try {
            IAccount account = this.accountDao.findAccountByUid(uid);
            return JsonUtil.objectToJson(account);
        }
        catch (NoSuchElementException exception) {
            response.status(404);
            return "No account found with id " + uid;
        }
    };
}

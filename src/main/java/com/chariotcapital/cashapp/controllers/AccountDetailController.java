package com.chariotcapital.cashapp.controllers;

import com.chariotcapital.cashapp.models.AccountDetail;
import com.chariotcapital.cashapp.repositories.AccountDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/api/accounts")
public class AccountDetailController {
    @Autowired
    private AccountDetailRepository acc_detailRepository;

    //add account number
    @PostMapping(path="/add_account_number")
    public Map<String, AccountDetail> addAccountNumber (@RequestBody Map<String, AccountDetail> map_request) {
        Map<String, AccountDetail> map = new HashMap<>();

        AccountDetail acc_detail = map_request.get("account");

        AccountDetail does_user_exist = acc_detailRepository.findByUserToken(acc_detail.getUserToken());

        AccountDetail user_acc_detail = new AccountDetail();

        if(does_user_exist == null){
            user_acc_detail.setUserToken(acc_detail.getUserToken());
            user_acc_detail.setAccountNumber(acc_detail.getAccountNumber());

            acc_detailRepository.save(user_acc_detail);

            map.put("account", user_acc_detail);

            return map;
        }

        user_acc_detail.setUserToken(null);
        user_acc_detail.setAccountNumber(null);
        user_acc_detail.setAccountBalance(0);
        user_acc_detail.setDebit(0);
        user_acc_detail.setDebit(0);

        map.put("account", user_acc_detail);

        return map;
    }

    //update existing account number
    @PatchMapping(path="/update_account_number")
    public Map<String, AccountDetail> updateAccountNumber (@RequestBody Map<String, AccountDetail> map_request) {
        Map<String, AccountDetail> map = new HashMap<>();

        AccountDetail acc_detail = map_request.get("account");

        AccountDetail user_exist = acc_detailRepository.findByUserToken(acc_detail.getUserToken());

        if(user_exist != null){
            user_exist.setAccountNumber(acc_detail.getAccountNumber());

            acc_detailRepository.save(user_exist);

            map.put("account", user_exist);

            return map;
        }

        AccountDetail user_acc_detail = new AccountDetail();
        user_acc_detail.setAccountNumber(null);

        map.put("account", user_acc_detail);

        return map;
    }

    //update account debit amount
    @PatchMapping(path="/update_debit_amount")
    public Map<String, AccountDetail> updateDebitAmount (@RequestBody Map<String, AccountDetail> map_request) {
        Map<String, AccountDetail> map = new HashMap<>();

        AccountDetail acc_detail = map_request.get("account");

        AccountDetail db_user = acc_detailRepository.findByUserToken(acc_detail.getUserToken());

        if(db_user != null){
            db_user.setDebit(acc_detail.getDebit());
            db_user.setAccountBalance(acc_detail.getDebit() - db_user.getCredit());

            acc_detailRepository.save(db_user);

            map.put("account", db_user);

            return map;
        }

        AccountDetail user_acc_detail = new AccountDetail();
        user_acc_detail.setUserToken(null);
        user_acc_detail.setDebit(0);
        user_acc_detail.setCredit(0);
        user_acc_detail.setAccountBalance(0);
        user_acc_detail.setCreatedDateTime(null);
        user_acc_detail.setUpdatedDateTime(null);

        map.put("account", user_acc_detail);

        return map;
    }

    //update account debit amount
    @PatchMapping(path="/update_credit_amount")
    public Map<String, AccountDetail> updateCreditAmount (@RequestBody Map<String, AccountDetail> map_request) {
        Map<String, AccountDetail> map = new HashMap<>();

        AccountDetail acc_detail = map_request.get("account");

        AccountDetail db_user = acc_detailRepository.findByUserToken(acc_detail.getUserToken());

        if(db_user != null){
            db_user.setCredit(acc_detail.getCredit());
            db_user.setAccountBalance(db_user.getDebit() - acc_detail.getCredit());

            acc_detailRepository.save(db_user);

            map.put("account", db_user);

            return map;
        }

        AccountDetail user_acc_detail = new AccountDetail();
        user_acc_detail.setUserToken(null);
        user_acc_detail.setDebit(0);
        user_acc_detail.setCredit(0);
        user_acc_detail.setAccountBalance(0);
        user_acc_detail.setCreatedDateTime(null);
        user_acc_detail.setUpdatedDateTime(null);

        map.put("account", user_acc_detail);

        return map;
    }

    //get only user account
    @GetMapping(path="/account/{user_token}")
    public @ResponseBody Map<String, AccountDetail> getUserAccount(@PathVariable("user_token") String userToken) {
        Map<String, AccountDetail> map = new HashMap<>();
        map.put("account", acc_detailRepository.findByUserToken(userToken));

        return map;
    }

    //delete only user account
    @DeleteMapping(path="/account/delete/{user_token}")
    public @ResponseBody Map<String, String> deleteUserAccount(@PathVariable("user_token") String userToken) {
        AccountDetail accountDetail = acc_detailRepository.findByUserToken(userToken);

        acc_detailRepository.delete(accountDetail);

        Map<String, String> map = new HashMap<>();
        map.put("account", null);
        map.put("message", "Account of user, " + userToken + ", is successfully deleted");

        return map;
    }

    //get all accounts in system
    @GetMapping(path="/all")
    public @ResponseBody Map<String, Iterable<AccountDetail>> getAllAccounts() {
        Map<String, Iterable<AccountDetail>> map = new HashMap<>();
        map.put("accounts", acc_detailRepository.findAll());

        return map;
    }
}

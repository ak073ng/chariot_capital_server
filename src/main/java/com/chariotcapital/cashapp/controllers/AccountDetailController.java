package com.chariotcapital.cashapp.controllers;

import com.chariotcapital.cashapp.models.AccountDetail;
import com.chariotcapital.cashapp.repositories.AccountDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/api/acc_details")
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

        map.put("account", user_acc_detail);

        return map;
    }

    //update existing account number
    @PatchMapping(path="/update_account_number")
    public Map<String, AccountDetail> updateAccountNumber (@RequestBody Map<String, AccountDetail> map_request) {
        Map<String, AccountDetail> map = new HashMap<>();

        AccountDetail acc_detail = map_request.get("account");

        AccountDetail does_user_exist = acc_detailRepository.findByUserToken(acc_detail.getUserToken());

        AccountDetail user_acc_detail = new AccountDetail();

        if(does_user_exist != null){
            user_acc_detail.setAccountNumber(acc_detail.getAccountNumber());

            acc_detailRepository.save(user_acc_detail);

            map.put("account", user_acc_detail);

            return map;
        }

        user_acc_detail.setAccountNumber(null);

        map.put("account", user_acc_detail);

        return map;
    }

    //update existing account info
    @PatchMapping(path="/update_account_info")
    public Map<String, AccountDetail> updateAccountInfo (@RequestBody Map<String, AccountDetail> map_request) {
        Map<String, AccountDetail> map = new HashMap<>();

        AccountDetail acc_detail = map_request.get("account");

        AccountDetail db_user = acc_detailRepository.findByUserToken(acc_detail.getUserToken());

        if(db_user != null){
            db_user.setDebit(acc_detail.getDebit());
            db_user.setCredit(acc_detail.getCredit());
            db_user.setAccountBalance(acc_detail.getDebit() - acc_detail.getCredit());

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

    //get all accounts in system
    @GetMapping(path="/all")
    public @ResponseBody Map<String, Iterable<AccountDetail>> getAllAccounts() {
        Map<String, Iterable<AccountDetail>> map = new HashMap<>();

        map.put("accounts", acc_detailRepository.findAll());

        return map;
    }
}

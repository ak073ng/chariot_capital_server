package com.chariotcapital.cashapp.controllers;

import com.chariotcapital.cashapp.models.AccountDetail;
import com.chariotcapital.cashapp.repositories.AccountDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/acc_details")
public class AccountDetailController {
    @Autowired
    private AccountDetailRepository acc_detailRepository;

    //add account details
    @PostMapping(path="/add_account")
    public AccountDetail addAccount (@RequestBody AccountDetail acc_detail) {
        Object does_user_exist = acc_detailRepository.findByUserToken(acc_detail.getUserToken());

        AccountDetail user_acc_detail = new AccountDetail();

        if(does_user_exist == null){
            user_acc_detail.setUserToken(acc_detail.getUserToken());
            user_acc_detail.setAccountNumber(acc_detail.getAccountNumber());
            user_acc_detail.setDebit(acc_detail.getDebit());
            user_acc_detail.setCredit(acc_detail.getCredit());
            user_acc_detail.setAccountBalance(acc_detail.getAccountBalance());

            acc_detailRepository.save(user_acc_detail);
        }

        return user_acc_detail;
    }

    //update existing account
    @PutMapping(path="/update_account")
    public AccountDetail updateAccount (@RequestBody AccountDetail acc_detail) {
        AccountDetail user_acc_detail = acc_detailRepository.findByUserToken(acc_detail.getUserToken());

        if(user_acc_detail != null){
            user_acc_detail.setUserToken(acc_detail.getUserToken());
            user_acc_detail.setAccountNumber(acc_detail.getAccountNumber());
            user_acc_detail.setDebit(acc_detail.getDebit());
            user_acc_detail.setCredit(acc_detail.getCredit());
            user_acc_detail.setAccountBalance(acc_detail.getAccountBalance());

            acc_detailRepository.save(user_acc_detail);
        }

        return user_acc_detail;
    }

    //get all accounts in system
    @GetMapping(path="/all")
    public @ResponseBody Iterable<AccountDetail> getAllAccounts() {
        // This returns a JSON or XML with the users
        return acc_detailRepository.findAll();
    }
}

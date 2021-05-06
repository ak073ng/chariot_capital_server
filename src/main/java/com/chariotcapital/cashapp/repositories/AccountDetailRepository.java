package com.chariotcapital.cashapp.repositories;

import com.chariotcapital.cashapp.models.AccountDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailRepository extends CrudRepository<AccountDetail, Integer> {
    AccountDetail findByUserToken(String userToken);
    AccountDetail findByAccountNumber(String accountNumber);
}

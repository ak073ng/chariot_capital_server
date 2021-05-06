package com.chariotcapital.cashapp.repositories;

import com.chariotcapital.cashapp.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    User findByUserToken(String userToken);
}

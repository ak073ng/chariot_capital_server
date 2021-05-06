package com.chariotcapital.cashapp.controllers;

import com.chariotcapital.cashapp.models.User;
import com.chariotcapital.cashapp.repositories.UserRepository;
import com.chariotcapital.cashapp.utility.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //user registration for new users
    @PostMapping(path="/register")
    public User registerUser (@RequestBody User user) {
        User new_user = new User();

        if(userRepository.findByEmail(user.getEmail()) == null){
            new_user.setUserToken(RandomStringUtils.randomAlphanumeric(6).toUpperCase());
            new_user.setFullName(user.getFullName());
            new_user.setUserName(user.getUserName());
            new_user.setEmail(user.getEmail());
            new_user.setPassword(Constants.getSecurePassword(user.getPassword(), Constants.APP_SALT.getBytes()));

            userRepository.save(new_user);
        } else{
            new_user.setUserToken("null");
            new_user.setFullName("null");
            new_user.setUserName("null");
            new_user.setEmail("null");
            new_user.setPassword("null");
        }

        return new_user;
    }

    //login for existing users
    @PostMapping(path="/login")
    public ResponseEntity<HttpStatus> loginUser (@RequestBody User user) {
        User db_user = userRepository.findByEmail(user.getEmail());

        if(db_user == null){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        } else{
            String user_pw = Constants.getSecurePassword(user.getPassword(), Constants.APP_SALT.getBytes());

            if(user_pw.contentEquals(db_user.getPassword())){
                return ResponseEntity.ok(HttpStatus.OK);
            } else{
                return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
            }
        }
    }

    //change password for forgetful users
    @PutMapping(path="/change_pw")
    public ResponseEntity<HttpStatus> changeUserPw (@RequestBody User user){
        User db_user = userRepository.findByEmail(user.getEmail());

        if(db_user == null){
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        } else{
            String encrypted_pw = Constants.getSecurePassword(user.getPassword(), Constants.APP_SALT.getBytes());

            db_user.setPassword(encrypted_pw);

            userRepository.save(db_user);

            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    //get all accounts
    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}

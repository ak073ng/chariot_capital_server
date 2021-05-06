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
            new_user.setSessionStatus(HttpStatus.OK.toString());

            userRepository.save(new_user);

            new_user.setPassword("<restricted>");

            return new_user;
        }

        new_user.setUserToken(null);
        new_user.setFullName(null);
        new_user.setUserName(null);
        new_user.setEmail(null);
        new_user.setPassword("<restricted>");
        new_user.setSessionStatus(HttpStatus.NOT_ACCEPTABLE.toString());

        return new_user;
    }

    //login for existing users
    @PostMapping(path="/login")
    public User loginUser (@RequestBody User user) {
        User auth_user = userRepository.findByEmail(user.getEmail());

        if(auth_user == null){
            User no_user = new User();
            no_user.setUserToken(null);
            no_user.setFullName(null);
            no_user.setUserName(null);
            no_user.setEmail(null);
            no_user.setPassword("<restricted>");
            no_user.setSessionStatus(HttpStatus.NOT_FOUND.toString());

            return no_user;
        }

        String user_pw = Constants.getSecurePassword(user.getPassword(), Constants.APP_SALT.getBytes());

        if(user_pw.contentEquals(auth_user.getPassword())){
            auth_user.setSessionStatus(HttpStatus.OK.toString());
            auth_user.setPassword("<restricted>");

            return auth_user;
        }

        auth_user.setSessionStatus(HttpStatus.UNAUTHORIZED.toString());
        auth_user.setPassword("<restricted>");

        return auth_user;
    }

    //change password for forgetful users
    @PatchMapping(path="/update_profile")
    public User updateProfile (@RequestBody User user){
        User auth_user = userRepository.findByEmail(user.getEmail());

        if(auth_user == null){
            User no_user = new User();
            no_user.setUserToken(null);
            no_user.setFullName(null);
            no_user.setUserName(null);
            no_user.setEmail(null);
            no_user.setPassword("<restricted>");
            no_user.setSessionStatus(HttpStatus.NOT_FOUND.toString());

            return no_user;
        }

        auth_user.setFullName(user.getFullName());
        auth_user.setUserName(user.getUserName());
        auth_user.setSessionStatus(HttpStatus.OK.toString());

        userRepository.save(auth_user);

        auth_user.setPassword("<restricted>");

        return auth_user;
    }

    //change password for forgetful users
    @PatchMapping(path="/change_pw")
    public User changeUserPw (@RequestBody User user){
        User auth_user = userRepository.findByEmail(user.getEmail());

        if(auth_user == null){
            User no_user = new User();
            no_user.setUserToken(null);
            no_user.setFullName(null);
            no_user.setUserName(null);
            no_user.setEmail(null);
            no_user.setPassword("<restricted>");
            no_user.setSessionStatus(HttpStatus.NOT_FOUND.toString());

            return no_user;
        }

        String encrypted_pw = Constants.getSecurePassword(user.getPassword(), Constants.APP_SALT.getBytes());

        auth_user.setPassword(encrypted_pw);
        auth_user.setSessionStatus(HttpStatus.OK.toString());

        userRepository.save(auth_user);

        auth_user.setPassword("<restricted>");

        return auth_user;
    }

    //get all accounts
    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}

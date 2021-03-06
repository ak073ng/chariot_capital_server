package com.chariotcapital.cashapp.controllers;

import com.chariotcapital.cashapp.models.AccountDetail;
import com.chariotcapital.cashapp.models.User;
import com.chariotcapital.cashapp.repositories.AccountDetailRepository;
import com.chariotcapital.cashapp.repositories.UserRepository;
import com.chariotcapital.cashapp.utility.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountDetailRepository acc_detailRepository;

    //user registration for new users
    @PostMapping(path="/register")
    public Map<String, User> registerUser (@RequestBody Map<String, User> map_request) {
        Map<String, User> map = new HashMap<>();

        User user = map_request.get("user");

        User new_user = new User();

        if(userRepository.findByEmail(user.getEmail()) == null){
            new_user.setUserToken(RandomStringUtils.randomAlphanumeric(6).toUpperCase());
            new_user.setFullName(user.getFullName());
            new_user.setUserName(user.getUserName());
            new_user.setEmail(user.getEmail());
            new_user.setPassword(Constants.getSecurePassword(user.getPassword(), Constants.APP_SALT.getBytes()));
            new_user.setNewPassword("<restricted>");
            new_user.setSessionStatus(HttpStatus.OK.toString());

            userRepository.save(new_user);

            new_user.setPassword("<restricted>");

            map.put("user", new_user);

            return map;
        }

        new_user.setUserToken(null);
        new_user.setFullName(null);
        new_user.setUserName(null);
        new_user.setEmail(null);
        new_user.setPassword("<restricted>");
        new_user.setNewPassword("<restricted>");
        new_user.setSessionStatus(HttpStatus.NOT_ACCEPTABLE.toString());

        map.put("user", new_user);

        return map;
    }

    //login for existing users
    @PostMapping(path="/login")
    public Map<String, User> loginUser (@RequestBody Map<String, User> map_request) {
        //response back to client
        Map<String, User> map = new HashMap<>();

        //request from client
        User user = map_request.get("user");

        //check for user in db
        User auth_user = userRepository.findByEmail(user.getEmail());

        //no user
        User no_user = new User();

        if(auth_user == null){
            no_user.setUserToken(null);
            no_user.setFullName(null);
            no_user.setUserName(null);
            no_user.setEmail(null);
            no_user.setPassword("<restricted>");
            no_user.setNewPassword("<restricted>");
            no_user.setSessionStatus(HttpStatus.NOT_FOUND.toString());
            no_user.setCreatedDateTime(null);
            no_user.setUpdatedDateTime(null);

            map.put("user", no_user);

            return map;
        }

        String user_pw = Constants.getSecurePassword(user.getPassword(), Constants.APP_SALT.getBytes());

        if(user_pw.contentEquals(auth_user.getPassword())){
            auth_user.setSessionStatus(HttpStatus.OK.toString());
            auth_user.setPassword("<restricted>");
            auth_user.setNewPassword("<restricted>");

            map.put("user", auth_user);

            return map;
        }

        //if password does not match, returns the following
        no_user.setUserToken(null);
        no_user.setFullName(null);
        no_user.setUserName(null);
        no_user.setEmail(null);
        no_user.setSessionStatus(HttpStatus.UNAUTHORIZED.toString());
        no_user.setPassword("<restricted>");
        no_user.setNewPassword("<restricted>");
        no_user.setCreatedDateTime(null);
        no_user.setUpdatedDateTime(null);

        map.put("user", no_user);

        return map;
    }

    //update user full name
    @PatchMapping(path="/update_fullname")
    public Map<String, User> updateFullName (@RequestBody Map<String, User> map_request){
        Map<String, User> map = new HashMap<>();

        User user = map_request.get("user");

        User auth_user = userRepository.findByEmail(user.getEmail());

        if(auth_user == null){
            User no_user = new User();
            no_user.setUserToken(null);
            no_user.setFullName(null);
            no_user.setUserName(null);
            no_user.setEmail(null);
            no_user.setPassword("<restricted>");
            no_user.setNewPassword("<restricted>");
            no_user.setSessionStatus(HttpStatus.NOT_FOUND.toString());

            map.put("user", no_user);

            return map;
        }

        auth_user.setFullName(user.getFullName());
        auth_user.setSessionStatus(HttpStatus.OK.toString());

        userRepository.save(auth_user);

        auth_user.setPassword("<restricted>");
        auth_user.setNewPassword("<restricted>");

        map.put("user", auth_user);

        return map;
    }

    //update user username
    @PatchMapping(path="/update_username")
    public Map<String, User> updateUserName (@RequestBody Map<String, User> map_request){
        Map<String, User> map = new HashMap<>();

        User user = map_request.get("user");

        User auth_user = userRepository.findByEmail(user.getEmail());

        if(auth_user == null){
            User no_user = new User();
            no_user.setUserToken(null);
            no_user.setFullName(null);
            no_user.setUserName(null);
            no_user.setEmail(null);
            no_user.setPassword("<restricted>");
            no_user.setNewPassword("<restricted>");
            no_user.setSessionStatus(HttpStatus.NOT_FOUND.toString());

            map.put("user", no_user);

            return map;
        }

        auth_user.setUserName(user.getUserName());
        auth_user.setSessionStatus(HttpStatus.OK.toString());

        userRepository.save(auth_user);

        auth_user.setPassword("<restricted>");
        auth_user.setNewPassword("<restricted>");

        map.put("user", auth_user);

        return map;
    }

    //change password for forgetful users
    @PatchMapping(path="/change_pw")
    public Map<String, User> changeUserPw (@RequestBody Map<String, User> map_request){
        Map<String, User> map = new HashMap<>();

        User user = map_request.get("user");

        User auth_user = userRepository.findByEmail(user.getEmail());

        //for if no user exist by that email
        User no_user = new User();

        if(auth_user == null){
            no_user.setUserToken(null);
            no_user.setFullName(null);
            no_user.setUserName(null);
            no_user.setEmail(null);
            no_user.setPassword("<restricted>");
            no_user.setNewPassword("<restricted>");
            no_user.setSessionStatus(HttpStatus.NOT_FOUND.toString());

            map.put("user", no_user);

            return map;
        }

        String current_encrypted_pw = Constants.getSecurePassword(user.getPassword(), Constants.APP_SALT.getBytes());
        String new_encrypted_pw = Constants.getSecurePassword(user.getNewPassword(), Constants.APP_SALT.getBytes());

        if(current_encrypted_pw.contentEquals(auth_user.getPassword())){
            auth_user.setPassword(new_encrypted_pw);
            auth_user.setSessionStatus(HttpStatus.OK.toString());

            userRepository.save(auth_user);

            auth_user.setPassword("<restricted>");
            auth_user.setNewPassword("<restricted>");

            map.put("user", auth_user);

            return map;
        }

        no_user.setUserToken(null);
        no_user.setFullName(null);
        no_user.setUserName(null);
        no_user.setEmail(null);
        no_user.setPassword("<restricted>");
        no_user.setNewPassword("<restricted>");
        no_user.setSessionStatus(HttpStatus.UNAUTHORIZED.toString());

        map.put("user", no_user);

        return map;
    }

    //get user
    @GetMapping(path="/user/{user_token}")
    public @ResponseBody Map<String, User> getUserAccount(@PathVariable("user_token") String userToken) {
        Map<String, User> map = new HashMap<>();
        map.put("account", userRepository.findByUserToken(userToken));

        return map;
    }

    //delete user
    @DeleteMapping(path="/user/delete/{user_token}")
    public @ResponseBody Map<String, String> deleteUser(@PathVariable("user_token") String userToken) {
        User user = userRepository.findByUserToken(userToken);

        AccountDetail accountDetail = acc_detailRepository.findByUserToken(userToken);

        acc_detailRepository.delete(accountDetail);
        userRepository.delete(user);

        Map<String, String> map = new HashMap<>();
        map.put("user", null);
        map.put("message", "User, " + userToken + ", is successfully deleted");

        return map;
    }

    //get all users
    @GetMapping(path="/all")
    public @ResponseBody Map<String, Iterable<User>> getAllUsers() {
        Map<String, Iterable<User>> map = new HashMap<>();

        map.put("users", userRepository.findAll());

        return map;
    }
}

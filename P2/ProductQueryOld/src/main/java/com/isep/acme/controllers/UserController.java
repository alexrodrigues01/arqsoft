package com.isep.acme.controllers;

import com.isep.acme.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.isep.acme.model.UserView;

@RestController
@RequestMapping(path = "/admin/user")

public class UserController {

    private  UserService userServiceImpl;


    @Autowired
    public void setService(@Value("${user.service}") String bean, ApplicationContext applicationContext){
        userServiceImpl = (UserService) applicationContext.getBean(bean);
    }
    @GetMapping("/{userId}")
    public UserView getUser(@PathVariable final Long userId) {

        return userServiceImpl.getUser(userId);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDetails> create(@PathVariable final String username) {
        UserDetails userDetails = userServiceImpl.loadUserByUsername(username);

        return ResponseEntity.ok().body(userDetails);
    }
}

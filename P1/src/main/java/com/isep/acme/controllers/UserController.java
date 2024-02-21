package com.isep.acme.controllers;

import com.isep.acme.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.isep.acme.model.UserView;

@RestController
@RequestMapping(path = "/admin/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

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

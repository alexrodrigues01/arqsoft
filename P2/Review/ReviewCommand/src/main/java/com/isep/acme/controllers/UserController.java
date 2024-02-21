package com.isep.acme.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.isep.acme.model.user.UserView;
import com.isep.acme.services.UserService;

@RestController
@RequestMapping(path = "/admin/user")
public class UserController {

    private  UserService userServiceImpl;

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

package com.isep.acme.controllers;

import com.isep.acme.model.UserView;
import com.isep.acme.services.ProductServiceImpl;
import com.isep.acme.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserControllerTest {


    UserController userController;


    UserService userService;

    @BeforeEach
    void setUp(){
        userService= Mockito.mock(UserService.class);
        userController= new UserController();
        userController.userServiceImpl=userService;
    }


    @Test
    void getUser() {
        Long userId = 1L;
        UserView expectedUserView = new UserView();

        when(userService.getUser(eq(userId))).thenReturn(expectedUserView);

        UserView actualUserView = userController.getUser(userId);

        assertEquals(expectedUserView, actualUserView);
    }

}

package com.isep.acme.services.acme.controllers;

import com.isep.acme.controllers.UserController;
import com.isep.acme.model.UserView;
import com.isep.acme.services.UserService;
import com.isep.acme.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

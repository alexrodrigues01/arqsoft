package com.isep.acme.services.acme.services;

import com.isep.acme.model.User;
import com.isep.acme.model.UserView;
import com.isep.acme.model.UserViewMapper;
import com.isep.acme.repositories.UserRepository;
import com.isep.acme.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserViewMapper userViewMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testLoadUserByUsername() {
        String username = "testUser";
        User user = new User(username, "password");
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        verify(userRepository).findByUsername(username);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UsernameNotFoundException() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));

    }

    @Test
    void testGetUser() {
        Long userId = 1L;
        User user = new User("testUser", "password");
        UserView userView = new UserView();
        when(userRepository.getById(userId)).thenReturn(user);
        when(userViewMapper.toUserView(user)).thenReturn(userView);

        UserView result = userService.getUser(userId);

        verify(userRepository).getById(userId);
        verify(userViewMapper).toUserView(user);
        assertEquals(userView, result);
    }

    @Test
    void testGetUserId() {
        Long userId = 1L;
        User user = new User("testUser", "password");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserId(userId);

        verify(userRepository).findById(userId);
        assertEquals(Optional.of(user), result);
    }
}

package com.isep.acme.services.neo4J;

import com.isep.acme.model.*;
import com.isep.acme.repositories.neo4J.UserRepositoryNeo4J;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceNeo4JImplTest {

    @Mock
    private UserRepositoryNeo4J userRepo;

    @Mock
    private UserViewMapper userViewMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceNeo4JImpl userService;
    @Test
    void testLoadUserByUsername() {
        UserNeo4J userNeo4J = new UserNeo4J("username", "password");
        userNeo4J.setUserId(1L);
        when(userRepo.findByUsername("username")).thenReturn(Optional.of(userNeo4J));

        userService.loadUserByUsername("username");

        verify(userRepo).findByUsername("username");
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {
        String username = "nonexistentuser";
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void testGetUser() {
        Long userId = 1L;
        UserNeo4J userNeo4J = new UserNeo4J("username", "password");
        userNeo4J.setUserId(userId);
        UserView userView = new UserView();
        when(userRepo.getById(userId)).thenReturn(userNeo4J);
        when(userViewMapper.toUserView(userNeo4J)).thenReturn(userView);

        UserView result = userService.getUser(userId);

        verify(userRepo).getById(userId);
        verify(userViewMapper).toUserView(userNeo4J);
        assertEquals(userView, result);
    }

    @Test
    void testGetUserId() {
        Long userId = 1L;
        UserNeo4J userNeo4J = new UserNeo4J("username", "password");
        userNeo4J.setUserId(userId);
        User user = new User("username", "password");
        when(userRepo.findById(userId)).thenReturn(Optional.of(userNeo4J));
        when(userMapper.toUser(userNeo4J)).thenReturn(user);

        Optional<User> result = userService.getUserId(userId);

        verify(userRepo).findById(userId);
        verify(userMapper).toUser(userNeo4J);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetUserIdUserNotFound() {
        Long userId = 1L;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserId(userId);

        verify(userRepo).findById(userId);
        assertFalse(result.isPresent());
    }
}

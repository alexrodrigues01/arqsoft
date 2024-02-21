package com.isep.acme.services.redis;

import com.isep.acme.model.*;
import com.isep.acme.repositories.redis.UserRepositoryRedis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceRedisImplTest {

    @Mock
    private UserRepositoryRedis userRepo;

    @Mock
    private UserViewMapper userViewMapper;

    @InjectMocks
    private UserServiceRedisImpl userServiceRedis;

    private UserRedis userRedis;

    @BeforeEach
    void setUp() {

        userRedis = new UserRedis("username", "password", "Full Name", "123456789", "Address");
    }

    @Test
    void testLoadUserByUsername() {
        when(userRepo.findByUsername(eq("username"))).thenReturn(Optional.of(userRedis));

        UserDetails userDetails = userServiceRedis.loadUserByUsername("username");
        assertEquals("username", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        // Verify other assertions for user details
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {
        when(userRepo.findByUsername("nonexistent")).thenReturn(Optional.empty());

        try {
            userServiceRedis.loadUserByUsername("nonexistent");
        } catch (UsernameNotFoundException e) {
            assertEquals("User with username - nonexistent, not found", e.getMessage());
        }
    }

    @Test
    void testGetUserId() {
        when(userRepo.findByUserId(1L)).thenReturn(Optional.of(userRedis));

        Optional<User> user = userServiceRedis.getUserId(1L);
        assertEquals("username", user.get().getUsername());
        assertEquals("password", user.get().getPassword());
        assertEquals("Full Name", user.get().getFullName());
        // Verify other assertions for user details
    }

    @Test
    void testGetUserIdUserNotFound() {
        when(userRepo.findByUserId(2L)).thenReturn(Optional.empty());

        Optional<User> user = userServiceRedis.getUserId(2L);
        assertEquals(Optional.empty(), user);
    }
}

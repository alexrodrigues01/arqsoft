package com.isep.acme.services;

import com.isep.acme.model.User;
import com.isep.acme.model.UserView;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    UserView getUser(final Long userId);

    Optional<User> getUserId(Long user);
}

package com.isep.acme.services.neo4J;

import com.isep.acme.model.*;
import com.isep.acme.repositories.neo4J.UserRepositoryNeo4J;
import com.isep.acme.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service("UserServiceNeo4JImpl")
@RequiredArgsConstructor
public class UserServiceNeo4JImpl implements UserDetailsService, UserService {

    @Autowired
    private final UserRepositoryNeo4J userRepo;
    @Autowired
    private final UserViewMapper userViewMapper;
    @Autowired
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
    }

    public UserView getUser(final Long userId){
        return userViewMapper.toUserView(userRepo.getById(userId));
    }

    public Optional<User> getUserId(Long user) {
        Optional<UserNeo4J> userNeo4J = userRepo.findById(user);
        if(userNeo4J.isEmpty())
            return Optional.empty();
        return Optional.of(userMapper.toUser(userNeo4J.get()));
    }
}

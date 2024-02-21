package com.isep.acme.services.redis;

import com.isep.acme.model.*;
import com.isep.acme.repositories.redis.UserRepositoryRedis;
import com.isep.acme.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("UserServiceRedisImpl")
@RequiredArgsConstructor
public class UserServiceRedisImpl implements UserDetailsService, UserService {
    @Autowired
    private final UserRepositoryRedis userRepo;
    @Autowired
    private final UserViewMapper userViewMapper;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
    }

    public UserView getUser(final Long userId){
        UserRedis userRedis = userRepo.getById(userId);
        User user= new User(userRedis.getUsername(),userRedis.getPassword(),userRedis.getFullName(),userRedis.getNif(),userRedis.getMorada());
        user.setUserId(userId);
        return userViewMapper.toUserView(user);
    }

    public Optional<User> getUserId(Long user) {
        UserRedis userRedis;
        Optional<UserRedis> userRedisOptional = userRepo.findByUserId(user);
        if(userRedisOptional.isPresent()){
            userRedis = userRedisOptional.get();
            User newUser= new User(userRedis.getUsername(),userRedis.getPassword(),userRedis.getFullName(),userRedis.getNif(),userRedis.getMorada());
            newUser.setUserId(user);
            return Optional.of(newUser);
        }
        return Optional.empty();
    }
}

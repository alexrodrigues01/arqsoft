package com.isep.acme.services.mongoDB;

import com.isep.acme.model.User;
import com.isep.acme.model.UserMongo;
import com.isep.acme.model.UserView;
import com.isep.acme.model.UserViewMapper;
import com.isep.acme.repositories.mongoDB.UserRepositoryMongo;
import com.isep.acme.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("UserServiceMongoImpl")
@RequiredArgsConstructor
public class UserServiceMongoImpl implements UserDetailsService,UserService {

    @Autowired
    private final UserRepositoryMongo userRepo;
    @Autowired
    private final UserViewMapper userViewMapper;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
    }

    public UserView getUser(final Long userId){
        UserMongo userMongo = userRepo.getById(userId);
        User user= new User(userMongo.getUsername(),userMongo.getPassword(),userMongo.getFullName(),userMongo.getNif(),userMongo.getMorada());
        user.setUserId(userId);
        return userViewMapper.toUserView(user);
    }

    public Optional<User> getUserId(Long user) {
        UserMongo userRedis;
        Optional<UserMongo> userRedisOptional = userRepo.findByUserId(user);
        if(userRedisOptional.isPresent()){
            userRedis = userRedisOptional.get();
            User newUser= new User(userRedis.getUsername(),userRedis.getPassword(),userRedis.getFullName(),userRedis.getNif(),userRedis.getMorada());
            newUser.setUserId(user);
            return Optional.of(newUser);
        }
        return Optional.empty();
    }
}
package com.isep.acme.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.isep.acme.model.User;
import com.isep.acme.model.UserView;
import com.isep.acme.model.UserViewMapper;
import com.isep.acme.repositories.UserRepository;

import java.util.Optional;


@Service

public class UserServiceImpl implements UserDetailsService, UserService {

    private UserRepository userRepo;

    @Autowired
    private UserViewMapper userViewMapper;

    @Autowired
    public void setUserRepo(@Value("${user.repo}") String userBean, ApplicationContext applicationContext){
        userRepo = (UserRepository) applicationContext.getBean(userBean);
    }


    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
    }

    public UserView getUser(final Long userId){
        return userViewMapper.toUserView(userRepo.getById(userId));
    }

    public Optional<User> getUserId(Long user) {
        return userRepo.findById(user);
    }
}

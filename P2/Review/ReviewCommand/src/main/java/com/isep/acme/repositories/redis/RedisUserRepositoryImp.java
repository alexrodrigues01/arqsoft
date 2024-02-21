package com.isep.acme.repositories.redis;

import com.isep.acme.model.user.User;
import com.isep.acme.repositories.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("RedisUserRepositoryImp")

public class RedisUserRepositoryImp extends RedisBaseRepository<User, Long> implements UserRepository {

    public RedisUserRepositoryImp(final RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, User.class);
    }

    @Override
    public User getById(Long userId) {
        return hashOperations.get(User.class.getSimpleName(), userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return hashOperations.entries(User.class.getSimpleName()).values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
    }
}

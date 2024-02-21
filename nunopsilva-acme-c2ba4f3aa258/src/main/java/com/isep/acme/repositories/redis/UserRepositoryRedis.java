package com.isep.acme.repositories.redis;

import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.User;
import com.isep.acme.model.UserRedis;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserRepositoryRedis extends CrudRepository<UserRedis, Long> {

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.userId", condition = "#p0.userId != null"),
            @CacheEvict(key = "#p0.username", condition = "#p0.username != null") })
    <S extends UserRedis> S save(S entity);



    @Cacheable
    Optional<UserRedis> findByUserId(Long userId);

    @Cacheable
    default UserRedis getById(final Long userId){
        final Optional<UserRedis> optionalUser = findByUserId(userId);

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException(User.class, userId);
        }
        if (!optionalUser.get().isEnabled()) {
            throw new ResourceNotFoundException(User.class, userId);
        }
        return optionalUser.get();
    }

    @Cacheable
    Optional<UserRedis> findByUsername(String username);

    @Cacheable
    Optional<UserRedis> findByNif(String nif);
}

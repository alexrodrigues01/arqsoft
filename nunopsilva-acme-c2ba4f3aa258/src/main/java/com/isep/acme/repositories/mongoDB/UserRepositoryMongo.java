package com.isep.acme.repositories.mongoDB;

import com.isep.acme.controllers.FileController;
import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.User;
import com.isep.acme.model.UserMongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserRepositoryMongo extends MongoRepository<UserMongo,Long>{
    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.userId", condition = "#p0.userId != null"),
            @CacheEvict(key = "#p0.username", condition = "#p0.username != null") })
    default  <S extends UserMongo> S save(S entity){
        final Optional<UserMongo> optionalUser = findByUserId(entity.getUserId());
        if (optionalUser.isEmpty()){
            return insert(entity);
        }

        logger.warn("User already exists");
        return null;
    }


    @Cacheable
    Optional<UserMongo> findByUserId(Long userId);

    @Cacheable
    default UserMongo getById(final Long userId){
        final Optional<UserMongo> optionalUser = findByUserId(userId);

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException(User.class, userId);
        }
        if (!optionalUser.get().isEnabled()) {
            throw new ResourceNotFoundException(User.class, userId);
        }
        return optionalUser.get();
    }

    @Cacheable
    Optional<UserMongo> findByUsername(String username);
}
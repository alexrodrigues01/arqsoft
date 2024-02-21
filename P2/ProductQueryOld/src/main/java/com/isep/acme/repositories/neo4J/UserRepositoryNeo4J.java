package com.isep.acme.repositories.neo4J;

import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.User;
import com.isep.acme.model.UserNeo4J;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserRepositoryNeo4J extends Neo4jRepository<UserNeo4J, Long> {

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.userId", condition = "#p0.userId != null"),
            @CacheEvict(key = "#p0.username", condition = "#p0.username != null") })
    <S extends UserNeo4J> S save(S entity);

    @Override
    @Cacheable
    Optional<UserNeo4J> findById(Long userId);

    @Cacheable
    default UserNeo4J getById(final Long userId){
        final Optional<UserNeo4J> optionalUser = findById(userId);

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException(User.class, userId);
        }
        if (!optionalUser.get().isEnabled()) {
            throw new ResourceNotFoundException(User.class, userId);
        }
        return optionalUser.get();
    }

    @Cacheable
    Optional<UserNeo4J> findByUsername(String username);


}
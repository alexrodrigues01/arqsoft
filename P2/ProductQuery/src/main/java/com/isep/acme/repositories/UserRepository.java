package com.isep.acme.repositories;

import com.isep.acme.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
@CacheConfig(cacheNames = "users")
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#p0.userId", condition = "#p0.userId != null"),
            @CacheEvict(key = "#p0.username", condition = "#p0.username != null")})
    <S extends User> S save(S entity);

    @Override
    @Cacheable
    Optional<User> findById(Long userId);

    @Cacheable
    User getById(final Long userId);

    @Cacheable
    Optional<User> findByUsername(String username);


}
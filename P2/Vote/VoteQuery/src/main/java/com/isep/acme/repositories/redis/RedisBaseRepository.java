package com.isep.acme.repositories.redis;


import com.isep.acme.repositories.Idable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RedisBaseRepository <T extends Idable<ID>, ID> implements CrudRepository<T, ID> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Class<T> classT;

    protected HashOperations<String, ID, T> hashOperations;


    public RedisBaseRepository(final RedisTemplate<String, Object> redisTemplate, final Class<T> classT) {
        this.redisTemplate=redisTemplate;
        this.classT = classT;
    }

    @PostConstruct
    private void setHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public <S extends T> S save(S entity) {
        hashOperations.put(classT.getSimpleName(), entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        final Map<ID, T> map = StreamSupport.stream(entities.spliterator(), false)
                .collect(Collectors.toMap(S::getId, s -> s));
        hashOperations.putAll(classT.getSimpleName(), map);
        return entities;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(hashOperations.get(classT.getSimpleName(), id));
    }


    @Override
    public boolean existsById(ID aLong) {
        return Optional.ofNullable(findById(aLong)).isPresent();
    }

    @Override
    public Iterable<T> findAll() {
        return hashOperations.entries(classT.getSimpleName()).values();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return StreamSupport.stream(ids.spliterator(), false)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }


    @Override
    public long count() {
        return hashOperations.entries(classT.getSimpleName()).size();
    }

    @Override
    public void deleteById(ID id) {
        hashOperations.delete(classT.getSimpleName(), id);
    }

    @Override
    public void delete(T entity) {
        deleteById(entity.getId());
    }


    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        StreamSupport.stream(ids.spliterator(), false)
                .forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        StreamSupport.stream(entities.spliterator(), false)
                .map(T::getId)
                .forEach(this::deleteById);
    }

    @Override
    public void deleteAll() {
        redisTemplate.delete(classT.getSimpleName());
    }
}

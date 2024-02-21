package com.isep.acme.repositories.mongoDB;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.repository.CrudRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class MongoBaseRepository <T,ID> implements CrudRepository<T, ID> {

    protected final MongoTemplate mongoTemplate;
    private final Class<T> classT;

    @Override
    public <S extends T> S save(S entity) {
        return mongoTemplate.save(entity);
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(mongoTemplate.findById(id, classT));
    }


    @Override
    public boolean existsById(ID aLong) {
        return mongoTemplate.findById(aLong, classT) != null;
    }

    @Override
    public Iterable<T> findAll() {
        return mongoTemplate.findAll(classT);
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return StreamSupport.stream(ids.spliterator(), false).map(id -> mongoTemplate.findById(id, classT)).filter(Objects::nonNull).toList();
    }

    @Override
    public long count() {
        return mongoTemplate.findAll(classT).size();
    }

    @Override
    public void deleteById(ID id) {
        Optional<T> object = findById(id);
        object.ifPresent(this::delete);
    }

    @Override
    public void delete(T entity) {
        mongoTemplate.remove(entity);
    }


    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        for (ID id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        for (T entity : mongoTemplate.findAll(classT)) {
            mongoTemplate.remove(entity);
        }
    }
}

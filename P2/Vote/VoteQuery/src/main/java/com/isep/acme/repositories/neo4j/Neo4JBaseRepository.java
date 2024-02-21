package com.isep.acme.repositories.neo4j;

import com.isep.acme.repositories.Idable;
import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class Neo4JBaseRepository<T extends Idable<ID>, ID> implements CrudRepository<T, ID> {

    protected final Neo4jTemplate neo4jRepository;
    private final Class<T> classT;

    @Override
    public <S extends T> S save(S entity) {
        return neo4jRepository.save(entity);
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        return neo4jRepository.saveAll(entities);
    }

    @Override
    public Optional<T> findById(ID id) {
        return neo4jRepository.findById(id, classT);
    }

    @Override
    public boolean existsById(ID aLong) {
        return neo4jRepository.existsById(aLong, classT);
    }

    @Override
    public Iterable<T> findAll() {
        return neo4jRepository.findAll(classT);
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return neo4jRepository.findAllById(ids, classT);
    }

    @Override
    public long count() {
        return neo4jRepository.count(classT);
    }

    @Override
    public void deleteById(ID id) {
        neo4jRepository.deleteById(id, classT);
    }

    @Override
    public void delete(T entity) {
        neo4jRepository.deleteById(entity.getId(), classT);
    }

    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        neo4jRepository.deleteAllById(ids, classT);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        neo4jRepository.deleteAllById(StreamSupport.stream(entities.spliterator(), false).map(t -> t.getId()).toList(), classT);
    }

    @Override
    public void deleteAll() {
        neo4jRepository.deleteAll(classT);
    }
}

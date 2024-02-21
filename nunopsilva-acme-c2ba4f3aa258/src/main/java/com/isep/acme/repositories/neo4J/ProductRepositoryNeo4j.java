package com.isep.acme.repositories.neo4J;

import com.isep.acme.model.ProductNeo4J;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProductRepositoryNeo4j extends Neo4jRepository<ProductNeo4J, Long> {
    Optional<ProductNeo4J> findBySku(String sku);
    Iterable<ProductNeo4J> findByDesignation(String designation);

    void deleteBySku(String sku);
}


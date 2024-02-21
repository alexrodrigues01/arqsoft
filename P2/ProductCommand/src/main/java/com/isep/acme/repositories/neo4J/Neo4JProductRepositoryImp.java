package com.isep.acme.repositories.neo4J;

import com.isep.acme.model.Product;
import com.isep.acme.repositories.ProductRepository;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("Neo4JProductRepositoryImp")
public class Neo4JProductRepositoryImp extends Neo4JBaseRepository<Product, Long> implements ProductRepository {

    public Neo4JProductRepositoryImp(final Neo4jTemplate neo4jTemplate) {
        super(neo4jTemplate, Product.class);
    }

    @Override
    public List<Product> findByDesignation(String designation) {
        String cypherQuery = "MATCH (p:Product) WHERE p.designation = $designation RETURN p";
        Map<String, Object> parameters = Map.of("designation", designation);
        return neo4jRepository.findAll(cypherQuery, parameters, Product.class);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        String cypherQuery = "MATCH (p:Product) WHERE p.sku = $sku RETURN p";
        Map<String, Object> parameters = Map.of("sku", sku);
        Class<Product> domainType = Product.class;
        return neo4jRepository.findOne(cypherQuery, parameters, domainType);
    }
}
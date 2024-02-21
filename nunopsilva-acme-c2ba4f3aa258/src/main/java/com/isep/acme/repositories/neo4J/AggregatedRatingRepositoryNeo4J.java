package com.isep.acme.repositories.neo4J;

import com.isep.acme.controllers.FileController;
import com.isep.acme.model.AggregatedRatingNeo4J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AggregatedRatingRepositoryNeo4J extends Neo4jRepository<AggregatedRatingNeo4J, Long> {
    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Query("MATCH (a:AggregatedRating)-[:HAS_PRODUCT]->(p:Product) WHERE id(p) = $productId RETURN a")
    Optional<AggregatedRatingNeo4J> findByProduct(Long productId);

    Optional<AggregatedRatingNeo4J> findByAggregatedId(Long aggregatedId);

    @Override
    default <S extends AggregatedRatingNeo4J> S save(S entity) {
        final Optional<AggregatedRatingNeo4J> optionalAggregatedRating = findByAggregatedId(entity.getAggregatedId());
        List<S> entities = new ArrayList<>();
        if (optionalAggregatedRating.isEmpty()) {
            entities.add(entity);
            saveAll(entities);
            return entity;
        }

        logger.warn("Product already exists");
        return null;
    }
}

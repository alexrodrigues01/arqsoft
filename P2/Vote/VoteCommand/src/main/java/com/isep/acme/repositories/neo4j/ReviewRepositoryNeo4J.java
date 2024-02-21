package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.Review;
import com.isep.acme.repositories.ReviewRepository;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("ReviewRepositoryNeo4J")
public class ReviewRepositoryNeo4J extends Neo4JBaseRepository<Review,Long> implements ReviewRepository {

    public ReviewRepositoryNeo4J(final Neo4jTemplate neo4jTemplate) {
        super(neo4jTemplate, Review.class);
    }

    @Override
    public Optional<Review> findById(Long reviewId) {
        String cypherQuery = "MATCH (r:Review) WHERE r.idReview = reviewId RETURN r";
        Map<String, Object> parameters = Map.of("reviewId", reviewId);
        return neo4jRepository.findAll(cypherQuery, parameters, Review.class).stream().findFirst();
    }
}

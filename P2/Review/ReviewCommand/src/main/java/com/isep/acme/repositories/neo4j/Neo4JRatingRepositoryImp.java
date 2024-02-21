package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.RatingRepository;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository("Neo4JRatingRepositoryImp")
public class Neo4JRatingRepositoryImp extends Neo4JBaseRepository<Rating, Long> implements RatingRepository {

    public Neo4JRatingRepositoryImp(final Neo4jTemplate neo4jTemplate) {
        super(neo4jTemplate, Rating.class);
    }

    @Override
    public Optional<Rating> findByRate(Double rate) {

        if (rate == null) return Optional.empty();
        String cypherQuery = "MATCH (r:Rating) WHERE r.rate = $rate RETURN r";
        Map<String, Object> parameters = Map.of("rate", rate);
        return neo4jRepository.findOne(cypherQuery, parameters, Rating.class);

    }
}
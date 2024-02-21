package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.repositories.AggregatedRatingRepository;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository("Neo4JAggregatedRatingRepositoryImp")
public class Neo4JAggregatedRatingRepositoryImp extends Neo4JBaseRepository<AggregatedRating, Long> implements AggregatedRatingRepository {

    public Neo4JAggregatedRatingRepositoryImp(final Neo4jTemplate neo4jRepository) {
        super(neo4jRepository, AggregatedRating.class);
    }

    @Override
    public Optional<AggregatedRating> findByProductId(Product product) {
        String cypherQuery = "MATCH (product:Product)<-[:HAS_PRODUCT]-(aggregatedRating:AggregatedRating) WHERE product.productID = $id RETURN aggregatedRating";
        Map<String, Object> parameters = Map.of("id", product.getProductID());
        return neo4jRepository.findOne(cypherQuery, parameters, AggregatedRating.class);
    }

}
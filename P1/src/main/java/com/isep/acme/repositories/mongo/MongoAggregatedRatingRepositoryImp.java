package com.isep.acme.repositories.mongo;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.repositories.AggregatedRatingRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("MongoAggregatedRatingRepositoryImp")
public class MongoAggregatedRatingRepositoryImp extends MongoBaseRepository<AggregatedRating, Long> implements AggregatedRatingRepository {

    public MongoAggregatedRatingRepositoryImp(final MongoTemplate mongoTemplate) {
        super(mongoTemplate, AggregatedRating.class);
    }

    @Override
    public Optional<AggregatedRating> findByProductId(Product product) {
        Query query = new Query();
        query.addCriteria(Criteria.where("product.productID").is(product.getProductID()));
        return mongoTemplate.find(query, AggregatedRating.class).stream().findAny();
    }
}

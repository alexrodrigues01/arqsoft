package com.isep.acme.repositories.mongo;

import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.repositories.ReviewRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("MongoReviewRepositoryImp")
public class MongoReviewRepositoryImp extends MongoBaseRepository<Review, Long> implements ReviewRepository {

    public MongoReviewRepositoryImp(final MongoTemplate mongoTemplate) {
        super(mongoTemplate, Review.class);
    }

    @Override
    public Optional<List<Review>> findByProductId(Product product) {
        Query query = new Query();
        query.addCriteria(Criteria.where("product._id").is(product.getProductID()));
        return Optional.of(mongoTemplate.find(query, Review.class));
    }

    @Override
    public Optional<List<Review>> findPendingReviews() {
        Query query = new Query();
        query.addCriteria(Criteria.where("approvalStatus").is("pending"));
        return Optional.of(mongoTemplate.find(query, Review.class));
    }

    @Override
    public Optional<List<Review>> findActiveReviews() {
        Query query = new Query();
        query.addCriteria(Criteria.where("approvalStatus").is("approved"));
        return Optional.of(mongoTemplate.find(query, Review.class));
    }

    @Override
    public Optional<List<Review>> findByProductIdStatus(Product product, String status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("product._id").is(product.getProductID()));
        query.addCriteria(Criteria.where("approvalStatus").is(status));
        return Optional.of(mongoTemplate.find(query, Review.class));
    }

    @Override
    public Optional<List<Review>> findByUserId(User user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user.userId").is(user.getUserId()));
        return Optional.of(mongoTemplate.find(query, Review.class));
    }

    @Override
    public Optional<List<Review>> findReviewVotedByUserId(Long userId) {
        final Query query1 = new Query();
        query1.addCriteria(Criteria.where("upVote.userID").is(userId));
        query1.addCriteria(Criteria.where("approvalStatus").is("approved"));

        final Query query2 = new Query();
        query2.addCriteria(Criteria.where("downVote.userID").is(userId));
        query2.addCriteria(Criteria.where("approvalStatus").is("approved"));

        final List<Review> list = mongoTemplate.find(query1, Review.class);
        list.addAll(mongoTemplate.find(query2, Review.class));
        return Optional.of(list);
    }
}

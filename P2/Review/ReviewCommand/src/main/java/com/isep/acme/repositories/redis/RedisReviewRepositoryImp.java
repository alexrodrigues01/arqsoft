
package com.isep.acme.repositories.redis;

import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.user.User;
import com.isep.acme.repositories.ReviewRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository("RedisReviewRepositoryImp")

public class RedisReviewRepositoryImp extends RedisBaseRepository<Review, Long> implements ReviewRepository {

    public RedisReviewRepositoryImp(final RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, Review.class);
    }

    @Override
    public Optional<List<Review>> findByProductId(Product product) {
        return Optional.of(hashOperations.entries(Review.class.getSimpleName()).values().stream()
                .filter(review -> review.getProduct().getProductID().equals(product.getProductID()))
                .toList());
    }

    @Override
    public Optional<List<Review>> findPendingReviews() {
        return Optional.of(hashOperations.entries(Review.class.getSimpleName()).values().stream()
                .filter(review -> review.getApprovalStatus().equalsIgnoreCase("pending"))
                .toList());
    }

    @Override
    public Optional<List<Review>> findActiveReviews() {
        return Optional.of(hashOperations.entries(Review.class.getSimpleName()).values().stream()
                .filter(review -> review.getApprovalStatus().equalsIgnoreCase("approved"))
                .toList());
    }

    @Override
    public Optional<List<Review>> findByProductIdStatus(Product product, String status) {
        return Optional.of(hashOperations.entries(Review.class.getSimpleName()).values().stream()
                .filter(review -> review.getApprovalStatus().equalsIgnoreCase(status))
                .filter(review -> review.getProduct().getProductID().equals(product.getProductID()))
                .toList());
    }

    @Override
    public Optional<List<Review>> findByUserId(User user) {
        return Optional.of(hashOperations.entries(Review.class.getSimpleName()).values().stream()
                .filter(review -> review.getUser().getUserId().equals(user.getUserId()))
                .toList());
    }

    @Override
    public Optional<List<Review>> findReviewVotedByUserId(Long userId) {
        return Optional.of(hashOperations.entries(Review.class.getSimpleName()).values().stream()
                .filter(review ->
                        review.getApprovalStatus().equalsIgnoreCase("approved")
                        && (review.getUpVote().stream().anyMatch(aaa -> aaa.getUserID().equals(userId))
                                || review.getDownVote().stream().anyMatch(aaa -> aaa.getUserID().equals(userId)))
                )
                .toList());
    }
}

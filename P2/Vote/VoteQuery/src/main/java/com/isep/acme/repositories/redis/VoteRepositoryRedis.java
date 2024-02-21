package com.isep.acme.repositories.redis;

import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.model.Vote;
import com.isep.acme.repositories.VoteRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("VoteRepositoryRedis")
public class VoteRepositoryRedis extends RedisBaseRepository<Vote, Long> implements VoteRepository {

    public VoteRepositoryRedis(final RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, Vote.class);
    }

    @Override
    public Optional<List<Vote>> getVoteByReview(Long reviewId) {
        return Optional.of(hashOperations.entries(Vote.class.getSimpleName()).values().stream()
                .filter(vote -> vote.getReviewId().equals(reviewId))
                .toList());
    }
}

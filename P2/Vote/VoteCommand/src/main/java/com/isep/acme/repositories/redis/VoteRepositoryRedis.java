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
    public boolean findVote(Vote vote) {
        if(hashOperations.entries(Vote.class.getSimpleName()).values().stream()
                .filter(v -> v.getReviewId().equals(vote.getReviewId()))
                .filter(v -> v.getVote().equals(vote.getVote()))
                .filter(v -> v.getUserId().equals(vote.getUserId()))
                .count()>0){
            return true;
        }else{
            return true;
        }
    }
}

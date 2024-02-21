package com.isep.acme.services.redis;

import com.isep.acme.model.*;
import com.isep.acme.repositories.redis.ReviewRepositoryRedis;
import com.isep.acme.services.ReviewRecommendation;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service("ReviewRecommendation1Redis")
public class ReviewRecommendation1Redis implements ReviewRecommendation {

    ReviewRepositoryRedis reviewRepository;


    StringRedisTemplate stringRedisTemplate;

    ReviewMapperAbstract reviewMapper;


    @Override
    public List<Review> getReviewsRecommended(Long userId) {
        Stream<ReviewRedis> reviewStream = StreamSupport.stream(reviewRepository.findAll().spliterator(), false);
         return reviewMapper.toReviewListReddis(reviewStream
                .filter(review -> {
                    review.initializeVotes();
                    addVotes(review);
                    review.setRating(new RatingRedis(0.0));
                    List<VoteRedis> upVotes = review.getUpVotes();
                    List<VoteRedis> downVotes = review.getDownVotes();

                    if (upVotes.size() >= 4) {
                        long totalVotes = upVotes.size() + downVotes.size();
                        long upvotePercentage = upVotes.size() * 100L / totalVotes;

                        return upvotePercentage >= 65;
                    }
                    return false;
                })
                .sorted((r1, r2) -> Integer.compare(
                        r2.getUpVotes().size(),
                        r1.getUpVotes().size()
                )).collect(Collectors.toList()));
    }

    private void addVotes(ReviewRedis reviewRedis){
        List<String> upVotes=getUpvotes(reviewRedis.getIdReview());
        List<String> downVotes =getDownvotes(reviewRedis.getIdReview());
        for(String upVote: upVotes){
            String[] voteArray= upVote.split(",");
            reviewRedis.addUpvote(new VoteRedis(voteArray[0],Long.parseLong(voteArray[1])));
        }
        for(String downVote: downVotes){
            String[] voteArray= downVote.split(",");
            reviewRedis.addDownVote(new VoteRedis(voteArray[0],Long.parseLong(voteArray[1])));
        }
    }

    public List<String> getUpvotes(Long reviewId) {
        return stringRedisTemplate.opsForList().range("upvotes:review:" + reviewId, 0, -1);
    }

    public List<String> getDownvotes(Long reviewId) {
        return stringRedisTemplate.opsForList().range("downvotes:review:" + reviewId, 0, -1);
    }

}

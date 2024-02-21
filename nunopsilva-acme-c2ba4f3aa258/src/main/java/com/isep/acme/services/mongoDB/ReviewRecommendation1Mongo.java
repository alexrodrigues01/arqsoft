package com.isep.acme.services.mongoDB;

import com.isep.acme.model.Review;
import com.isep.acme.model.ReviewMapperAbstract;
import com.isep.acme.model.ReviewMongo;
import com.isep.acme.model.Vote;
import com.isep.acme.repositories.mongoDB.ReviewRepositoryMongo;
import com.isep.acme.services.ReviewRecommendation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service("ReviewRecommendation1Mongo")
public class ReviewRecommendation1Mongo implements ReviewRecommendation {

    ReviewRepositoryMongo reviewRepository;

    ReviewMapperAbstract reviewMapperAbstract;

    @Override
    public List<Review> getReviewsRecommended(Long userId) {
        Stream<ReviewMongo> reviewStream = reviewRepository.findAll().stream();
        return reviewMapperAbstract.toReviewListFromMongo(reviewStream
                .filter(review -> {
                    List<Vote> upVotes = review.getUpVote();
                    List<Vote> downVotes = review.getDownVote();

                    if (upVotes.size() >= 4) {
                        long totalVotes = upVotes.size() + downVotes.size();
                        long upvotePercentage = upVotes.size() * 100L / totalVotes;

                        return upvotePercentage >= 65;
                    }
                    return false;
                })
                .sorted((r1, r2) -> Integer.compare(
                        r2.getUpVote().size(),
                        r1.getUpVote().size()
                )).collect(Collectors.toList()));
    }
}

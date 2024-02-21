package com.isep.acme.services.recommendationReviews;

import com.isep.acme.model.Review;
import com.isep.acme.model.Vote;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.services.ReviewRecommendation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service("ReviewRecommendation1")
public class ReviewRecommendation1 implements ReviewRecommendation {

    ReviewRepository reviewRepository;

    @Autowired
    public void setRepo(@Value("${review.repo}") String bean, ApplicationContext applicationContext){
        reviewRepository = (ReviewRepository) applicationContext.getBean(bean);
    }

    @Override
    public List<Review> getReviewsRecommended(Long userId) {
        Stream<Review> reviewStream = StreamSupport.stream(reviewRepository.findAll().spliterator(), false);
         return reviewStream
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
                )).collect(Collectors.toList());
    }
}

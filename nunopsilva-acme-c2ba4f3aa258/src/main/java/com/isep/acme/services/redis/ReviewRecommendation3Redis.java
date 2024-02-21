package com.isep.acme.services.redis;

import com.isep.acme.model.Review;
import com.isep.acme.model.ReviewMapperAbstract;
import com.isep.acme.model.ReviewRedis;
import com.isep.acme.model.User;
import com.isep.acme.repositories.redis.ReviewRepositoryRedis;
import com.isep.acme.services.ReviewRecommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("ReviewRecommendation3Redis")
public class ReviewRecommendation3Redis implements ReviewRecommendation {

    @Autowired
    ReviewRecommendation1Redis reviewRecommendation1;

    @Autowired
    ReviewRecommendation2Redis reviewRecommendation2;

    @Autowired
    ReviewRepositoryRedis reviewRepositoy;

    @Autowired
    ReviewMapperAbstract reviewMapperAbstract;

    @Override
    public List<Review> getReviewsRecommended(Long userId) {
        List<Review> reviewsRecommended1 = reviewRecommendation1.getReviewsRecommended(userId);
        List<Review> reviewsRecommended2 = reviewRecommendation2.getReviewsRecommended(userId);

        Set<Review> reviewSet1 = new HashSet<>(reviewsRecommended1);
        List<Review> commonReviews = reviewsRecommended2.stream()
                .filter(reviewSet1::contains)
                .collect(Collectors.toList());

        Set<User> uniqueUsers = commonReviews.stream()
                .map(Review::getUser)
                .collect(Collectors.toSet());
        Set<User> finalUniqueUsers = new HashSet<>();

        Optional<List<ReviewRedis>> personalReviewsRedis = reviewRepositoy.findByUserUserId(userId);

        List<Review> personalReviews = new ArrayList<>();
        if(personalReviewsRedis.isPresent()){
            personalReviews = reviewMapperAbstract.toReviewListReddis(personalReviewsRedis.get());
        }

        for (User user : uniqueUsers) {
            int commonReviewsCount = 0;
            Optional<List<ReviewRedis>> reviewsUserRedis = reviewRepositoy.findByUserUserId(user.getUserId());

            if (reviewsUserRedis.isPresent()) {
                List<Review> reviewsUser = reviewMapperAbstract.toReviewListReddis(reviewsUserRedis.get());
                for (Review userReview : reviewsUser) {
                    for (Review personalReview : personalReviews)
                        if (userReview.getRating().getRate().equals(personalReview.getRating().getRate())) {
                            commonReviewsCount++;
                        }
                }
                if (commonReviewsCount >= reviewsUserRedis.get().size() / 2) {
                    finalUniqueUsers.add(user);
                }
            }

        }

        return commonReviews.stream()
                .filter(review -> finalUniqueUsers.contains(review.getUser()))
                .collect(Collectors.toList());

    }
}

package com.isep.acme.services.h2;

import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.repositories.h2.ReviewRepository;
import com.isep.acme.services.ReviewRecommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service("ReviewRecommendation3")
public class ReviewRecommendation3 implements ReviewRecommendation {

    @Autowired
    ReviewRecommendation1 reviewRecommendation1;

    @Autowired
    ReviewRecommendation2 reviewRecommendation2;

    @Autowired
    ReviewRepository reviewRepositoy;


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

        Optional<List<Review>> personalReviews = reviewRepositoy.findByUserIdRecommendation(userId);
        for (User user : uniqueUsers) {
            int commonReviewsCount = 0;
            Optional<List<Review>> reviewsUser = reviewRepositoy.findByUserId(user);

            if (reviewsUser.isPresent()) {
                for (Review userReview : reviewsUser.get()) {
                    if (personalReviews.isPresent()) {
                        for (Review personalReview : personalReviews.get())
                            if (userReview.getRating().getRate().equals(personalReview.getRating().getRate())) {
                                commonReviewsCount++;
                            }
                    }
                }
                if (commonReviewsCount >= reviewsUser.get().size() / 2) {
                    finalUniqueUsers.add(user);
                }
            }

        }

        return commonReviews.stream()
                .filter(review -> finalUniqueUsers.contains(review.getUser()))
                .collect(Collectors.toList());

    }
}

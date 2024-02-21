package com.isep.acme.services.mongoDB;

import com.isep.acme.model.*;
import com.isep.acme.repositories.mongoDB.ReviewRepositoryMongo;
import com.isep.acme.repositories.mongoDB.UserRepositoryMongo;
import com.isep.acme.services.ReviewRecommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("ReviewRecommendation3Mongo")
public class ReviewRecommendation3Mongo implements ReviewRecommendation {

    @Autowired
    ReviewRecommendation1Mongo reviewRecommendation1;

    @Autowired
    ReviewRecommendation2Mongo reviewRecommendation2;

    @Autowired
    ReviewRepositoryMongo reviewRepositorio;

    @Autowired
    UserRepositoryMongo userRepositoryMongo;


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

        Optional<List<ReviewMongo>> personalReviewsMongo = reviewRepositorio.findByUserUserId(userId);

        List<Review> personalReviews = new ArrayList<>();
        if(personalReviewsMongo.isPresent()){
            for (ReviewMongo reviewMongo: personalReviewsMongo.get() ) {
                Product product= new Product(reviewMongo.getProduct().getProductID(), reviewMongo.getProduct().getSku(), reviewMongo.getProduct().getDesignation(), reviewMongo.getProduct().getDescription());
                User user= new User(reviewMongo.getUser().getUsername(),reviewMongo.getUser().getPassword(),reviewMongo.getUser().getFullName(),reviewMongo.getUser().getNif(),reviewMongo.getUser().getMorada());
                Rating rating= new Rating(reviewMongo.getRating().getRate()) ;
                personalReviews.add(new Review(reviewMongo.getReviewText(),reviewMongo.getPublishingDate(),product,reviewMongo.getFunFact(),rating,user));
            }
        }

        for (User user : uniqueUsers) {
            int commonReviewsCount = 0;
            Optional<List<ReviewMongo>> reviewsUserRedis = reviewRepositorio.findByUserUserId(user.getUserId());

            if (reviewsUserRedis.isPresent()) {
                List<Review> reviewsUser = new ArrayList<>();
                for (ReviewMongo reviewMongo: reviewsUserRedis.get() ) {
                    Product product= new Product(reviewMongo.getProduct().getProductID(), reviewMongo.getProduct().getSku(), reviewMongo.getProduct().getDesignation(), reviewMongo.getProduct().getDescription());
                    User user2= new User(reviewMongo.getUser().getUsername(),reviewMongo.getUser().getPassword(),reviewMongo.getUser().getFullName(),reviewMongo.getUser().getNif(),reviewMongo.getUser().getMorada());
                    Rating rating= new Rating(reviewMongo.getRating().getRate()) ;
                    reviewsUser.add(new Review(reviewMongo.getReviewText(),reviewMongo.getPublishingDate(),product,reviewMongo.getFunFact(),rating,user2));
                }
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

package com.isep.acme.services;

import com.isep.acme.model.Review;
import com.isep.acme.model.vote.Vote;
import com.isep.acme.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.data.util.Pair;

import java.util.*;

@Service("ReviewRecommendation2")
public class ReviewRecommendation2 implements ReviewRecommendation {

    ReviewRepository repository;

    @Autowired
    public void setRepo(@Value("${review.repo}") String bean, ApplicationContext applicationContext){
        repository = (ReviewRepository) applicationContext.getBean(bean);
    }

    @Override
    public List<Review> getReviewsRecommended(Long userId) {
        Iterable<Review> iterable = repository.findAll();

        List<Pair<Boolean, Review>> usersReview = new ArrayList<>();
        List<Review> userReviewFinalCompare = new ArrayList<>();
        Map<Long, List<Pair<Boolean, Review>>> voteCountMap = new HashMap<>();
        List<Long> usersToRecommend = new ArrayList<>();
        List<Review> reviewsRecommended = new ArrayList<>();


        // Catch the reviews of the user and the other users in common
        for (Review review : iterable) {
            for (Vote vote : review.getUpVote()) {
                if (vote.getUserID().equals(userId)) {
                    usersReview.add(Pair.of(true, review));
                    userReviewFinalCompare.add(review);
                }

            }
            for (Vote downvote : review.getDownVote()) {
                if (downvote.getUserID().equals(userId)) {
                    usersReview.add(Pair.of(false, review));
                    userReviewFinalCompare.add(review);
                }
            }
        }

        for (Review review : iterable) {
            for (Vote vote : review.getUpVote()) {
                if (voteCountMap.containsKey(vote.getUserID())) {
                    voteCountMap.get(vote.getUserID()).add(Pair.of(true, review));
                } else {
                    List<Pair<Boolean, Review>> reviews = new ArrayList<>();
                    reviews.add(Pair.of(true, review));
                    voteCountMap.put(vote.getUserID(), reviews);
                }
            }
            for (Vote downvote : review.getDownVote()) {
                if (voteCountMap.containsKey(downvote.getUserID())) {
                    voteCountMap.get(downvote.getUserID()).add(Pair.of(false, review));
                } else {
                    List<Pair<Boolean, Review>> reviews = new ArrayList<>();
                    reviews.add(Pair.of(false, review));
                    voteCountMap.put(downvote.getUserID(), reviews);
                }
            }
        }

        for (Map.Entry<Long, List<Pair<Boolean, Review>>> entry : voteCountMap.entrySet()) {
            int contador = 0;
            for (Pair<Boolean, Review> vote : entry.getValue()) {
                if (usersReview.contains(vote)) {
                    contador++;
                }
            }
            if (contador >= Math.ceil(entry.getValue().size() / 2.0)) {
                usersToRecommend.add(entry.getKey());
            }
        }

        for (Review review : iterable) {
            for (Long userIdVote : usersToRecommend) {
                if (Objects.equals(review.getUser().getUserId(), userIdVote) && !userReviewFinalCompare.contains(review)) {
                    reviewsRecommended.add(review);
                }
            }

        }


        return reviewsRecommended;
    }
}
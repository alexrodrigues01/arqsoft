package com.isep.acme.model;

import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring")
public abstract class ReviewMapperAbstract {
    public abstract List<Review> toReviewListFromNeo4J(Iterable<ReviewNeo4J> reviewNeo4JList);
    public abstract Review toReview(ReviewNeo4J reviewNeo4J);


    public abstract List<Review> toReviewListFromMongo(Iterable<ReviewMongo> reviewNeo4JList);
    public abstract List<Review> toReviewList(Iterable<ReviewRedis> reviewRedis);

    public List<Review> toReviewListReddis(Iterable<ReviewRedis> reviewRedis){
        List<Review>reviews= new ArrayList<>();
        for(ReviewRedis reviewRedis1: reviewRedis){
            Product product= new Product(reviewRedis1.getProduct().getProductID(), reviewRedis1.getProduct().getSku(), reviewRedis1.getProduct().getDesignation(), reviewRedis1.getProduct().getDescription());
            User user= new User(reviewRedis1.getUser().getUsername(),reviewRedis1.getUser().getPassword(),reviewRedis1.getUser().getFullName(),reviewRedis1.getUser().getNif(),reviewRedis1.getUser().getMorada());
            Rating rating= new Rating(reviewRedis1.getRating().getRate()) ;
            Review review= new Review(reviewRedis1.getReviewText(),reviewRedis1.getPublishingDate(),product,reviewRedis1.getFunFact(),rating,user);
            review.setDownVote(toVote(reviewRedis1.getUpVotes()));
            review.setDownVote(toVote(reviewRedis1.getDownVotes()));
            review.setIdReview(reviewRedis1.getIdReview());
            reviews.add(review);
        }
        return reviews;
    }



    public abstract List<Vote> toVote(List<VoteRedis> voteRedis);
    public abstract Review toReview(ReviewRedis reviewRedis);
}

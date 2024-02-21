package com.isep.acme.model;

import com.isep.acme.model.dto.ReviewDTO;

import java.util.ArrayList;
import java.util.List;

public class ReviewMapper {

    public static ReviewDTO toDto(Review review){
        return new ReviewDTO(review.getIdReview(), review.getReviewText(), review.getPublishingDate(), review.getApprovalStatus(), review.getFunFact(), review.getRating().getRate(), review.getUpVote().size());
    }

    public static ReviewDTO toDto(ReviewRedis review){
        review.initializeVotes();
        return new ReviewDTO(review.getIdReview(), review.getReviewText(), review.getPublishingDate(), review.getApprovalStatus(), review.getFunFact(), review.getRating().getRate(), review.getUpVotes().size());
    }

    public static ReviewDTO toDto(ReviewMongo review){
        return new ReviewDTO(review.getIdReview(), review.getReviewText(), review.getPublishingDate(), review.getApprovalStatus(), review.getFunFact(), review.getRating().getRate(), review.getUpVote().size());
    }

    public static List<ReviewDTO> toDtoList(List<Review> review) {
        List<ReviewDTO> dtoList = new ArrayList<>();

        for (Review rev: review) {
            dtoList.add(toDto(rev));
        }
        return dtoList;
    }

    public static List<ReviewDTO> toDtoListMongo(List<ReviewMongo> review) {
        List<ReviewDTO> dtoList = new ArrayList<>();

        for (ReviewMongo rev: review) {
            dtoList.add(toDto(rev));
        }
        return dtoList;
    }

    public static List<ReviewDTO> toDtoListRedis(List<ReviewRedis> review) {
        List<ReviewDTO> dtoList = new ArrayList<>();

        for (ReviewRedis rev: review) {

            rev.initializeVotes();
            dtoList.add(toDto(rev));
        }
        return dtoList;
    }

    public static ReviewDTO toDto(ReviewNeo4J review){
        return new ReviewDTO(review.getIdReview(), review.getReviewText(), review.getPublishingDate(), review.getApprovalStatus(), review.getFunFact(), review.getRating().getRate(), review.getUpVote().size());
    }

    public static List<ReviewDTO> reviewNeo4JtoDtoList(List<ReviewNeo4J> review) {
        List<ReviewDTO> dtoList = new ArrayList<>();

        for (ReviewNeo4J rev: review) {
            dtoList.add(toDto(rev));
        }
        return dtoList;
    }
}
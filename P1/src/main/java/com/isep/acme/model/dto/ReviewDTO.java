package com.isep.acme.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewDTO {

    private Long idReview;
    private String reviewText;
    private Date publishingDate;
    private String approvalStatus;
    private String funFact;
    private Double rating;
    private Integer vote;

    public ReviewDTO(Long idReview, String reviewText, Date publishingDate, String approvalStatus, String funFact, Double rating, Integer vote) {
        this.idReview = idReview;
        this.reviewText = reviewText;
        this.publishingDate = publishingDate;
        this.approvalStatus = approvalStatus;
        this.funFact = funFact;
        this.rating = rating;
        this.vote = vote;
    }
}

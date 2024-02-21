package com.isep.acme.model.review;

import com.isep.acme.model.user.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ReviewDTO {

    private Long idReview;
    private String reviewText;
    private Date publishingDate;
    private String approvalStatus;
    private String funFact;
    private Double rating;
    private Integer vote;

    private List<Long> approves;

    private String sku;

    public ReviewDTO(Long idReview, String reviewText, Date publishingDate, String approvalStatus, String funFact, Double rating, Integer vote, List<User> users) {
        this.idReview = idReview;
        this.reviewText = reviewText;
        this.publishingDate = publishingDate;
        this.approvalStatus = approvalStatus;
        this.funFact = funFact;
        this.rating = rating;
        this.vote = vote;
        this.approves = new ArrayList<>();
        for (User user:users) {
            approves.add(user.getUserId());
        }
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}

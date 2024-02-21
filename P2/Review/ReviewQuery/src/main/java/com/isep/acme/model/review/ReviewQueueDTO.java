package com.isep.acme.model.review;

import com.isep.acme.model.user.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewQueueDTO implements Serializable {


    private Long idReview;
    private String reviewText;
    private Date publishingDate;
    private String approvalStatus;
    private String funFact;
    private Double rating;

    private Long userID;

    private String sku;

    private List<Long> approves;

    public ReviewQueueDTO(Long idReview, String reviewText, Date publishingDate, String approvalStatus, String funFact, Double rating, Long userID, String sku, List<Long> approves) {
        this.idReview = idReview;
        this.reviewText = reviewText;
        this.publishingDate = publishingDate;
        this.approvalStatus = approvalStatus;
        this.funFact = funFact;
        this.rating = rating;
        this.userID = userID;
        this.sku = sku;
        this.approves = approves;
    }

    public ReviewQueueDTO(Long idReview) {
        this.idReview = idReview;
    }

    public void setIdReview( Long idReview ) {
        this.idReview = idReview;
    }

    public Long getIdReview() {
        return this.idReview;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getUserID() {
        return userID;
    }

    public String getSku() {
        return sku;
    }

    public List<Long> getApproves() {
        return approves;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setApproves(List<Long> approves) {
        this.approves = approves;
    }
}

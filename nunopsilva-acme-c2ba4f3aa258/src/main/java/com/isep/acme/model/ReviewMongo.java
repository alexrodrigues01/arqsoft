package com.isep.acme.model;

import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Setter
public class ReviewMongo {

    @org.springframework.data.annotation.Id
    private Long idReview;

    @Version
    private long version;

    @Column(nullable = false)
    private String approvalStatus;

    @Column(nullable = false)
    private String reviewText;

    @ElementCollection
    @Column(nullable = true)
    private List<Vote> upVote;

    @ElementCollection
    @Column(nullable = true)
    private List<Vote> downVote;

    @Column(nullable = true)
    private String report;

    @Column(nullable = false)
    private LocalDate publishingDate;

    @Column(nullable = false)
    private String funFact;


    private ProductMongo product;

    private UserMongo user;

    private RatingMongo rating;

    protected ReviewMongo(){}

    public ReviewMongo(final Long idReview, final long version, final String approvalStatus, final String reviewText, final LocalDate publishingDate, final String funFact) {
        this.idReview = Objects.requireNonNull(idReview);
        this.version = Objects.requireNonNull(version);
        setApprovalStatus(approvalStatus);
        setReviewText(reviewText);
        setPublishingDate(publishingDate);
        setFunFact(funFact);
    }

    public ReviewMongo(final Long idReview, final long version, final String approvalStatus, final  String reviewText, final List<Vote> upVote, final List<Vote> downVote, final String report, final LocalDate publishingDate, final String funFact, ProductMongo product, RatingMongo rating, UserMongo user) {
        this(idReview, version, approvalStatus, reviewText, publishingDate, funFact);

        setUpVote(upVote);
        setDownVote(downVote);
        setReport(report);
        setProduct(product);
        setRating(rating);
        setUser(user);

    }

    public ReviewMongo(final String reviewText, LocalDate publishingDate, ProductMongo product, String funFact, RatingMongo rating, UserMongo user) {
        long leftLimit = 1L;
        long rightLimit = 100000L;
        this.idReview= leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        setReviewText(reviewText);
        setProduct(product);
        setPublishingDate(publishingDate);
        setApprovalStatus("pending");
        setFunFact(funFact);
        setRating(rating);
        setUser(user);
        this.upVote = new ArrayList<>();
        this.downVote = new ArrayList<>();
    }

    public ReviewMongo(final Long id, final String reviewText, LocalDate publishingDate, ProductMongo product, String funFact, RatingMongo rating, UserMongo user) {
        this.idReview= id;
        setReviewText(reviewText);
        setProduct(product);
        setPublishingDate(publishingDate);
        setApprovalStatus("pending");
        setFunFact(funFact);
        setRating(rating);
        setUser(user);
        this.upVote = new ArrayList<>();
        this.downVote = new ArrayList<>();
    }

    public Long getIdReview() {
        return idReview;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public Boolean setApprovalStatus(String approvalStatus) {

        if( approvalStatus.equalsIgnoreCase("pending") ||
                approvalStatus.equalsIgnoreCase("approved") ||
                approvalStatus.equalsIgnoreCase("rejected")) {

            this.approvalStatus = approvalStatus;
            return true;
        }
        return false;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        if (reviewText == null || reviewText.isBlank()) {
            throw new IllegalArgumentException("Review Text is a mandatory attribute of Review.");
        }
        if (reviewText.length() > 2048) {
            throw new IllegalArgumentException("Review Text must not be greater than 2048 characters.");
        }

        this.reviewText = reviewText;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public LocalDate getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(LocalDate publishingDate) {
        this.publishingDate = publishingDate;
    }

    public long getVersion() {
        return version;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public void setProduct(ProductMongo product) {
        this.product = product;
    }

    public ProductMongo getProduct() {
        return product;
    }

    public UserMongo getUser() {
        return user;
    }

    public void setUser(UserMongo user) {
        this.user = user;
    }

    public RatingMongo getRating() {
        if(rating == null) {
            return new RatingMongo(0.0);
        }
        return rating;
    }

    public void setRating(RatingMongo rating) {
        this.rating = rating;
    }

    public List<Vote> getUpVote() {
        return upVote;
    }

    public void setUpVote(List<Vote> upVote) {
        this.upVote = upVote;
    }

    public List<Vote> getDownVote() {
        return downVote;
    }

    public void setDownVote(List<Vote> downVote) {
        this.downVote = downVote;
    }

    public boolean addUpVote(Vote upVote) {

        if( !this.approvalStatus.equals("approved"))
            return false;

        if(!this.upVote.contains(upVote)){
            this.upVote.add(upVote);
            return true;
        }
        return false;
    }

    public boolean addDownVote(Vote downVote) {

        if( !this.approvalStatus.equals( "approved") )
            return false;

        if(!this.downVote.contains(downVote)){
            this.downVote.add(downVote);
            return true;
        }
        return false;
    }
}

package com.isep.acme.model;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RedisHash
@Getter
public class ReviewRedis {

    @Indexed
    @org.springframework.data.annotation.Id
    private Long idReview;

    @Indexed
    @Version
    private long version;

    @Indexed
    @Column(nullable = false)
    private String approvalStatus;

    public String getDownVotesKey() {
        return downVotesKey;
    }

    public String getUpvotesKey() {
        return upvotesKey;
    }

    public boolean addUpvote(VoteRedis voteRedis) {
        if(upVotes==null){
            upVotes=new ArrayList<>();
        }
        if(upVotes.contains(voteRedis)){
            return false;
        }
        return this.upVotes.add(voteRedis);
    }
    public boolean addDownVote(VoteRedis voteRedis) {
        if(downVotes==null){
            downVotes=new ArrayList<>();
        }
        if(downVotes.contains(voteRedis)){
            return false;
        }
        return this.downVotes.add(voteRedis);
    }

    public void setUpVotes(List<VoteRedis> upVotes) {
        this.upVotes = upVotes;
    }

    public void setDownVotes(List<VoteRedis> downVotes) {
        this.downVotes = downVotes;
    }

    private List<VoteRedis> upVotes;

    public List<VoteRedis> getUpVotes() {
        return upVotes;
    }

    public List<VoteRedis> getDownVotes() {
        return downVotes;
    }

    private List<VoteRedis> downVotes;

    @Indexed
    @Column(nullable = false)
    private String reviewText;


    public void setDownVotesKey(String downVotesKey) {
        this.downVotesKey = downVotesKey;
    }

    public void setUpvotesKey(String upvotesKey) {
        this.upvotesKey = upvotesKey;
    }

    @Indexed
    private String downVotesKey;

    @Indexed
    private String upvotesKey;

    @Indexed
    @Column(nullable = true)
    private String report;

    @Indexed
    @Column(nullable = false)
    private LocalDate publishingDate;

    @Indexed
    @Column(nullable = false)
    private String funFact;

    @Indexed
    @JoinColumn(name = "product_id", nullable = false)
    private ProductRedis product;

    @Indexed
    @JoinColumn(name = "user_id", nullable = false)
    private UserRedis user;

    @Indexed
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private RatingRedis rating;

    protected ReviewRedis(){}

    public ReviewRedis(final Long idReview, final long version, final String approvalStatus, final String reviewText, final LocalDate publishingDate, final String funFact) {
        this.idReview = Objects.requireNonNull(idReview);
        this.version = Objects.requireNonNull(version);
        setApprovalStatus(approvalStatus);
        setReviewText(reviewText);
        setPublishingDate(publishingDate);
        setFunFact(funFact);
    }

    public ReviewRedis(final Long idReview, final long version, final String approvalStatus, final  String reviewText, final List<VoteRedis> upVote, final List<VoteRedis> downVote, final String report, final LocalDate publishingDate, final String funFact, ProductRedis product, RatingRedis rating, UserRedis user) {
        this(idReview, version, approvalStatus, reviewText, publishingDate, funFact);

        setReport(report);
        setProduct(product);
        setRating(rating);
        setUser(user);

    }

    public ReviewRedis(final String reviewText, LocalDate publishingDate, ProductRedis product, String funFact, RatingRedis rating, UserRedis user) {
        long leftLimit = 1L;
        long rightLimit = 100000L;
        this.idReview= leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        upVotes= new ArrayList<>();
        downVotes=new ArrayList<>();
        setReviewText(reviewText);
        setProduct(product);
        setPublishingDate(publishingDate);
        setApprovalStatus("pending");
        setFunFact(funFact);
        setRating(rating);
        setUser(user);
    }

    public ReviewRedis(final Long id, final String reviewText, LocalDate publishingDate, ProductRedis product, String funFact, RatingRedis rating, UserRedis user) {
        this.idReview= id;
        upVotes= new ArrayList<>();
        downVotes=new ArrayList<>();
        setReviewText(reviewText);
        setProduct(product);
        setPublishingDate(publishingDate);
        setApprovalStatus("pending");
        setFunFact(funFact);
        setRating(rating);
        setUser(user);
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
        if (report.length() > 2048) {
            throw new IllegalArgumentException("Report must not be greater than 2048 characters.");
        }
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

    public void setProduct(ProductRedis product) {
        this.product = product;
    }

    public ProductRedis getProduct() {
        return product;
    }

    public UserRedis getUser() {
        return user;
    }

    public void setUser(UserRedis user) {
        this.user = user;
    }

    public RatingRedis getRating() {
        if(rating == null) {
            return new RatingRedis(0.0);
        }
        return rating;
    }

    public void setRating(RatingRedis rating) {
        this.rating = rating;
    }

    public void initializeVotes(){
        if(upVotes==null){
            this.upVotes= new ArrayList<>();
        }
        if(downVotes==null){
            this.downVotes= new ArrayList<>();
        }
    }

}

package com.isep.acme.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isep.acme.model.review.SqlDateToLocalDateConverter;
import com.isep.acme.model.user.User;
import com.isep.acme.model.vote.Vote;
import com.isep.acme.repositories.Idable;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.convert.ConvertWith;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Entity
@Node
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review implements Idable<Long> {

    @Id
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
    @Relationship(type = "HAS_UPVOTE")
    private List<Vote> upVote;

    @ElementCollection
    @Column(nullable = true)
    @Relationship(type = "HAS_DOWNVOTE")
    private List<Vote> downVote;

    @Column(nullable = true)
    private String report;

    @Column(nullable = false)
    @ConvertWith(converter = SqlDateToLocalDateConverter.class)
    private Date publishingDate;

    @Column(nullable = false)
    private String funFact;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @Relationship(type = "HAS_PRODUCT")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @Relationship(type = "HAS_USER", direction = Relationship.Direction.INCOMING)
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @Relationship(type = "HAS_RATING", direction = Relationship.Direction.OUTGOING)
    private Rating rating;

    @ManyToMany(fetch = FetchType.EAGER)
    @Column(nullable = true)
    @Relationship(type = "HAS_APPROVED_RECOMMENDED", direction = Relationship.Direction.INCOMING)
    private List<User> approves;

    protected Review() {
        this.idReview = generateId();
    }

    public Review(final Long idReview, final long version, final String approvalStatus, final String reviewText, final Date publishingDate, final String funFact) {
        this.idReview = Objects.requireNonNull(idReview);
        this.version = Objects.requireNonNull(version);
        setApprovalStatus(approvalStatus);
        setReviewText(reviewText);
        setPublishingDate(publishingDate);
        setFunFact(funFact);
        this.upVote = new ArrayList<>();
        this.downVote = new ArrayList<>();
        this.approves = new ArrayList<>();
    }

    public Review(final Long idReview, final long version, final String approvalStatus, final String reviewText, final List<Vote> upVote, final List<Vote> downVote, final String report, final Date publishingDate, final String funFact, Product product, Rating rating, User user) {
        this(idReview, version, approvalStatus, reviewText, publishingDate, funFact);
        this.approves = new ArrayList<>();
        setUpVote(upVote);
        setDownVote(downVote);
        setReport(report);
        setProduct(product);
        setRating(rating);
        setUser(user);

    }

    public Review(final String reviewText, Date publishingDate, Product product, String funFact, Rating rating, User user) {
        this();
        setReviewText(reviewText);
        setProduct(product);
        setPublishingDate(publishingDate);
        setApprovalStatus("pending");
        setFunFact(funFact);
        setRating(rating);
        setUser(user);
        this.upVote = new ArrayList<>();
        this.downVote = new ArrayList<>();
        this.approves = new ArrayList<>();
    }

    public Long getIdReview() {
        return idReview;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public Boolean setApprovalStatus(String approvalStatus) {

        if (approvalStatus.equalsIgnoreCase("pending") ||
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

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
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

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rating getRating() {
        if (rating == null) {
            return new Rating(0.0);
        }
        return rating;
    }

    public void setRating(Rating rating) {
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

        if (!this.approvalStatus.equals("approved"))
            return false;

        if (!this.upVote.contains(upVote)) {
            this.upVote.add(upVote);
            return true;
        }
        return false;
    }

    public boolean addDownVote(Vote downVote) {

        if (!this.approvalStatus.equals("approved"))
            return false;

        if (!this.downVote.contains(downVote)) {
            this.downVote.add(downVote);
            return true;
        }
        return false;
    }

    @Override
    public Long getId() {
        return this.idReview;
    }

    @Override
    public Long generateId() {
        long value = new Random().nextInt();
        return value > 0 ? value : value * -1;
    }

    public List<User> getApproves() {
        return approves;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return idReview.equals(review.idReview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReview);
    }
}

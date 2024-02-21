package com.isep.acme.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isep.acme.model.dto.VoteDTO;
import com.isep.acme.model.dto.VoteQueueDTO;
import com.isep.acme.repositories.Idable;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;

import javax.persistence.*;
import java.util.Objects;
import java.util.Random;

@Entity
@EqualsAndHashCode
@Setter
@Node
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vote implements Idable<Long> {

    @Id
    @org.springframework.data.annotation.Id
    private Long voteId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String vote;

    @Column(nullable = false)
    private Long reviewId;

    protected Vote(){
        this.voteId = generateId();
    }

    public Vote(final Long voteId, final Long userId, final String vote, final Long reviewId) {
        this.voteId = generateId();
        this.voteId = Objects.requireNonNull(voteId);
        setUserId(userId);
        setVote(vote);
        setReviewId(reviewId);
    }

    public Vote(Long userId, String vote, Long reviewId) {
        this.voteId = generateId();
        this.userId = userId;
        this.vote = vote;
        this.reviewId = reviewId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getVote() {
        return vote;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public VoteQueueDTO toDto(){
        return new VoteQueueDTO(this.voteId,this.userId,this.vote,this.reviewId);
    }

    @Override
    public Long getId() {
        return this.voteId;
    }

    @Override
    public Long generateId() {
        long value = new Random().nextInt();
        return value > 0 ? value : value * -1;
    }
}

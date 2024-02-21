package com.isep.acme.model.vote;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class VoteDTO {
    private Long userId;
    private String vote;
    private Long reviewId;

    public VoteDTO(Long userId, String vote, Long reviewId) {
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
}
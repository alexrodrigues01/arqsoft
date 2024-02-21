package com.isep.acme.model.dto;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class VoteQueueDTO {

    private Long voteId;
    private Long userId;
    private String vote;
    private Long reviewId;

    public VoteQueueDTO(Long voteId, Long userId, String vote, Long reviewId) {
        this.voteId = voteId;
        this.userId = userId;
        this.vote = vote;
        this.reviewId = reviewId;
    }

    public Long getVoteId() {
        return voteId;
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

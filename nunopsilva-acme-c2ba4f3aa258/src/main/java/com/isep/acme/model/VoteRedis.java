package com.isep.acme.model;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Embeddable;
import java.util.Objects;

@RedisHash
@Embeddable
public class VoteRedis {

    @Indexed
    private String vote;
    @Indexed
    private Long userID;


    protected VoteRedis() {

    }

    public VoteRedis(String vote, Long userID) {
        this.vote = vote;
        this.userID = userID;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteRedis vote1 = (VoteRedis) o;
        return vote.equals(vote1.vote) && userID.equals(vote1.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vote, userID);
    }

}

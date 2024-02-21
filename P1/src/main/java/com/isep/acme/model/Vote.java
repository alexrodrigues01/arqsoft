package com.isep.acme.model;

import com.isep.acme.repositories.Idable;
import org.springframework.data.neo4j.core.schema.Node;

import javax.persistence.Embeddable;

import java.util.Objects;
import java.util.Random;

@Embeddable
@Node
public class Vote implements Idable<Long> {

    @org.springframework.data.annotation.Id
    private Long id;

    private String vote;
    private Long userID;


    protected Vote() {
        this.id = generateId();
    }

    public Vote(String vote, Long userID) {
        this();
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
        Vote vote1 = (Vote) o;
        return vote.equals(vote1.vote) && userID.equals(vote1.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vote, userID);
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long generateId() {
        long value = new Random().nextInt();
        return value > 0 ? value : value * -1;
    }
}

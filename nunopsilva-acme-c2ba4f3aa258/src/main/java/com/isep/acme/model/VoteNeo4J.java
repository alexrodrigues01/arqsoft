package com.isep.acme.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import javax.persistence.Embeddable;
import java.util.Objects;

@Node("Vote")
public class VoteNeo4J {

    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String vote;
    @Property
    private Long userID;


    protected VoteNeo4J() {

    }

    public VoteNeo4J(String vote, Long userID) {
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
        VoteNeo4J vote1 = (VoteNeo4J) o;
        return vote.equals(vote1.vote) && userID.equals(vote1.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vote, userID);
    }

}

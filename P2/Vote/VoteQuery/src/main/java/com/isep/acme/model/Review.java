package com.isep.acme.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isep.acme.repositories.Idable;
import org.springframework.data.neo4j.core.convert.ConvertWith;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import javax.persistence.*;
import java.util.*;


@Entity
@Node
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review implements Idable<Long> {

    @Id
    @org.springframework.data.annotation.Id
    private Long idReview;

    public Review(Long idReview){
        this.idReview = idReview;
    }

    public Review() {
        this.idReview=generateId();
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
}

package com.isep.acme.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isep.acme.repositories.Idable;
import org.springframework.data.neo4j.core.schema.Node;

import javax.persistence.*;
import java.util.Objects;
import java.util.Random;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Node
public class Rating implements Idable<Long> {

    @Id
    @org.springframework.data.annotation.Id
    private Long idRating;

    @Version
    private long version;

    @Column(nullable = false)
    private Double rate;

    protected Rating() {
        this.idRating = generateId();
    }

    public Rating(Long idRating, long version, Double rate) {
        this.idRating = Objects.requireNonNull(idRating);
        this.version = Objects.requireNonNull(version);
        setRate(rate);
    }

    public Rating(Double rate) {
        this();
        setRate(rate);
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public Long getId() {
        return this.idRating;
    }

    @Override
    public Long generateId() {
        long value = new Random().nextInt();
        return value > 0 ? value : value * -1;
    }
}

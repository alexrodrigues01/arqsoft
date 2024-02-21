package com.isep.acme.model;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;


@Setter
@Getter
public class RatingMongo {

    @org.springframework.data.annotation.Id
    private Long idRating;

    @Version
    private long version;

    @Column(nullable = false)
    private Double rate;

    protected RatingMongo(){}

    public RatingMongo(Long idRating, long version, Double rate) {
        this.idRating = Objects.requireNonNull(idRating);
        this.version = Objects.requireNonNull(version);
        setRate(rate);
    }

    public RatingMongo(Double rate) {
        long leftLimit = 1L;
        long rightLimit = 100000L;
        this.idRating= leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        setRate(rate);
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
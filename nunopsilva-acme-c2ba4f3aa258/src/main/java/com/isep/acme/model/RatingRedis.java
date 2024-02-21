package com.isep.acme.model;



import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.util.Objects;

@RedisHash
@Entity
public class RatingRedis {

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idRating;

    @Indexed
    @Version
    private long version;

    @Indexed
    @Column(nullable = false)
    private Double rate;

    protected RatingRedis(){}

    public RatingRedis(Long idRating, long version, Double rate) {
        this.idRating = Objects.requireNonNull(idRating);
        this.version = Objects.requireNonNull(version);
        setRate(rate);
    }

    public RatingRedis(Double rate) {
        setRate(rate);
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}

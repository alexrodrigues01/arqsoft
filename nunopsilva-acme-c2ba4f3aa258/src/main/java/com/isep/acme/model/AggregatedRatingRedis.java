package com.isep.acme.model;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;

@RedisHash

public class AggregatedRatingRedis {

    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long aggregatedId;

    @Indexed
    @Column()
    private double average;

    @Indexed

    private ProductRedis product;

    protected AggregatedRatingRedis() {}

    public AggregatedRatingRedis(double average, ProductRedis product) {
        this.average = average;
        this.product = product;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public ProductRedis getProduct() {
        return product;
    }

    public void setProduct(ProductRedis product) {
        this.product = product;
    }

    public Long getAggregatedId() {
        return aggregatedId;
    }
}

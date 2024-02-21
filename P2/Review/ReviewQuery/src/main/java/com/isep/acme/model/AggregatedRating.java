package com.isep.acme.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isep.acme.repositories.Idable;

import javax.persistence.*;
import java.util.Random;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class AggregatedRating implements Idable<Long> {

    @Id
    @org.springframework.data.annotation.Id
    private Long aggregatedId;

    @Column()
    private double average;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Product product;

    protected AggregatedRating() {
        this.aggregatedId = generateId();
    }

    public AggregatedRating(double average, Product product) {
        this();
        this.average = average;
        this.product = product;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getAggregatedId() {
        return aggregatedId;
    }

    @Override
    public Long getId() {
        return this.aggregatedId;
    }

    @Override
    public Long generateId() {
        long value = new Random().nextInt();
        return value > 0 ? value : value * -1;
    }
}

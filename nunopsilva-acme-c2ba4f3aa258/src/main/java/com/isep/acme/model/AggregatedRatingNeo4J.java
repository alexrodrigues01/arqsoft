package com.isep.acme.model;


import org.springframework.data.neo4j.core.schema.*;

@Node("AggregatedRating")
public class AggregatedRatingNeo4J {

    @Id
    @GeneratedValue
    private Long aggregatedId;

    @Property
    private double average;

    @Relationship(type = "RATED_OF")
    private ProductNeo4J product;

    protected AggregatedRatingNeo4J() {}

    public AggregatedRatingNeo4J(double average, ProductNeo4J product) {
        this.average = average;
        this.product = product;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public ProductNeo4J getProduct() {
        return product;
    }

    public void setProduct(ProductNeo4J product) {
        this.product = product;
    }

    public Long getAggregatedId() {
        return aggregatedId;
    }
}

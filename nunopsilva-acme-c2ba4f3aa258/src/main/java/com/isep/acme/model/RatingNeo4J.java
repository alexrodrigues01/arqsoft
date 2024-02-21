package com.isep.acme.model;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Objects;

@Node("Rating")
@Getter
@Setter
public class RatingNeo4J {

    @Id
    @GeneratedValue
    private Long idRating;

    @Property
    private long version;

    @Property
    private Double rate;

    protected RatingNeo4J(){}

    public RatingNeo4J(Long idRating, long version, Double rate) {
        this.idRating = Objects.requireNonNull(idRating);
        this.version = Objects.requireNonNull(version);
        setRate(rate);
    }

    public RatingNeo4J(Double rate) {
        setRate(rate);
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}

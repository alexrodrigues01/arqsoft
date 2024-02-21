package com.isep.acme.model;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RatingMapper {
    public abstract Rating toRating(RatingNeo4J ratingNeo4J);

    public abstract RatingNeo4J toRatingNeo4J(Rating rating);

}

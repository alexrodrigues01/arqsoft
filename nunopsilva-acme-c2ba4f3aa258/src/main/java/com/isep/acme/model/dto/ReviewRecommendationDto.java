package com.isep.acme.model.dto;

import lombok.Getter;

@Getter
public class ReviewRecommendationDto {
    private Long userID;

    public ReviewRecommendationDto(Long userID) {
        this.userID = userID;
    }
}

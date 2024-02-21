package com.isep.acme.model.review;

import java.io.Serializable;

public class    ModerateReviewDTO implements Serializable {

    private final Long userId;
    private final String approved;

    public ModerateReviewDTO(Long userId, String approved) {
        this.userId = userId;
        this.approved = approved;
    }

    public Long getUserId() {
        return userId;
    }

    public String getApproved() {
        return approved;
    }
}

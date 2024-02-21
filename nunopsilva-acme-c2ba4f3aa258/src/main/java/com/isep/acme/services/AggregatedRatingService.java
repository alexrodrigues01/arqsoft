package com.isep.acme.services;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.AggregatedRatingRedis;

public interface AggregatedRatingService {
    AggregatedRating save(String sku);
}

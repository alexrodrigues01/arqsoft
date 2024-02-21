package com.isep.acme.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.services.AggregatedRatingService;

@Tag(name = "AggregatedRating", description = "Endpoints for managing aggregated Rating")
@RestController
@RequestMapping("/aggregatedrating")
@RequiredArgsConstructor
public class AggregatedRatingController {

    private final AggregatedRatingService aService;

    @GetMapping(value = "/{sku}")
    public ResponseEntity<AggregatedRating> getAverage(@PathVariable("sku") final String sku) {

        AggregatedRating a = aService.save(sku);

        return ResponseEntity.ok().body(a);
    }
}

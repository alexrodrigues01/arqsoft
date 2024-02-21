package com.isep.acme.controllers;

import com.isep.acme.model.EventDTO;
import com.isep.acme.model.Review;
import com.isep.acme.model.TypeOfEvent;
import com.isep.acme.model.review.ReviewDTO;
import com.isep.acme.rabbitmq.Runner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.isep.acme.services.ReviewService;

import java.util.List;


@Tag(name = "Review", description = "Endpoints for managing Review")
@RestController
@RequiredArgsConstructor
class ReviewController {

    private final ReviewService rService;

    private final Runner runner;

    @Operation(summary = "finds a product through its sku and shows its review by status")
    @GetMapping("/products/{sku}/reviews/{status}")
    public ResponseEntity<List<ReviewDTO>> findById(@PathVariable(value = "sku") final String sku, @PathVariable(value = "status") final String status) throws Exception {

        final var review = rService.getReviewsOfProduct(sku, status);
        runner.sendMessage(new EventDTO(TypeOfEvent.GET,"Review", review));

        return ResponseEntity.ok().body( review );
    }

    @Operation(summary = "gets review by user")
    @GetMapping("/reviews/{userID}")
    public ResponseEntity<List<ReviewDTO>> findReviewByUser(@PathVariable(value = "userID") final Long userID) throws Exception {

        final var review = rService.findReviewsByUser(userID);
        runner.sendMessage(new EventDTO(TypeOfEvent.GET,"Review", review));

        return ResponseEntity.ok().body(review);
    }

    @Operation(summary = "gets pending reviews")
    @GetMapping("/reviews/pending")
    public ResponseEntity<List<ReviewDTO>> getPendingReview() throws Exception {

        List<ReviewDTO> r = rService.findPendingReview();
        runner.sendMessage(new EventDTO(TypeOfEvent.GET_LIST,"Review", r));

        return ResponseEntity.ok().body(r);
    }

    @Operation(summary = "Get recommended reviews")
    @GetMapping("/reviews/{userID}/recommended")
    public ResponseEntity<List<ReviewDTO>> getRecommendedReviews(@PathVariable(value = "userID") final Long userId){

        try {
            final List<ReviewDTO> rev = rService.recommendedReviews(userId);

            if (rev.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            runner.sendMessage(new EventDTO(TypeOfEvent.GET_LIST,"Review", rev));

            return ResponseEntity.ok().body(rev);
        }
        catch( IllegalArgumentException e ) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.isep.acme.services;

import com.isep.acme.model.Review;
import com.isep.acme.model.user.User;
import com.isep.acme.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("ReviewRecommendation4")
public class ReviewRecommendation4 implements ReviewRecommendation {

    ReviewRepository reviewRepositoy;

    @Autowired
    public void setRepo(@Value("${review.repo}") String bean, ApplicationContext applicationContext){
        reviewRepositoy = (ReviewRepository) applicationContext.getBean(bean);
    }

    @Override
    public List<Review> getReviewsRecommended(Long userId) {
        List<Review> reviews = new ArrayList<>();
        Iterable<Review> iterable = reviewRepositoy.findAll();
        iterable.forEach(reviews::add);
        return reviews;
    }
}

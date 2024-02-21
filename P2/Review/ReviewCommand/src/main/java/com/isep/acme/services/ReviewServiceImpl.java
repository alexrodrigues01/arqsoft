package com.isep.acme.services;

import java.lang.IllegalArgumentException;


import com.isep.acme.exceptions.ResourceNotFoundException;
import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.review.CreateReviewDTO;
import com.isep.acme.model.review.ReviewDTO;
import com.isep.acme.model.review.ReviewMapper;
import com.isep.acme.model.review.ReviewQueueDTO;
import com.isep.acme.model.user.User;
import com.isep.acme.rabbitmq.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.isep.acme.model.*;

import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.UserRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository repository;
    private ProductRepository pRepository;
    private UserRepository uRepository;
    private ReviewRecommendation reviewRecommendation;
    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RestService restService;

    @Autowired
    public void setProductRepo(@Value("${review.repo}") String bean, @Value("${product.repo}") String productBean, @Value("${user.repo}") String userBean,
                               @Value("${review.recommendation}") String beanRecommendation, ApplicationContext applicationContext){
        repository = (ReviewRepository) applicationContext.getBean(bean);
        pRepository = (ProductRepository) applicationContext.getBean(productBean);
        uRepository = (UserRepository) applicationContext.getBean(userBean);
        reviewRecommendation = (ReviewRecommendation) applicationContext.getBean(beanRecommendation);
    }

    public void setRepository(ReviewRepository repository) {
        this.repository = repository;
    }

    public void setpRepository(ProductRepository pRepository) {
        this.pRepository = pRepository;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public void setRestService(RestService restService) {
        this.restService = restService;
    }

    public void setReviewRecommendation(ReviewRecommendation reviewRecommendation) {
        this.reviewRecommendation = reviewRecommendation;
    }

    @Override
    public ReviewDTO create(final CreateReviewDTO createReviewDTO, String sku) {

        final Optional<Product> product = pRepository.findBySku(sku);

        if (product.isEmpty()) return null;

        final var user = userService.getUserId(createReviewDTO.getUserID());

        if (user.isEmpty()) return null;

        Rating rating = null;
        Optional<Rating> r = ratingService.findByRate(createReviewDTO.getRating());
        if (r.isPresent()) {
            rating = r.get();
        }

        LocalDate date = LocalDate.now();

        String funfact = restService.getFunFact(date);

        if (funfact == null) return null;

        Review review = new Review(createReviewDTO.getReviewText(), Date.valueOf(date), product.get(), funfact, rating, user.get());

        review = repository.save(review);

        return ReviewMapper.toDto(review);
    }

    @Override
    public Double getWeightedAverage(Product product) {

        Optional<List<Review>> r = repository.findByProductId(product);

        if (r.isEmpty()) return 0.0;

        double sum = 0;

        for (Review rev : r.get()) {
            Rating rate = rev.getRating();

            if (rate != null) {
                sum += rate.getRate();
            }
        }

        return sum / r.get().size();
    }

    @Override
    public Boolean deleteReview(Long reviewId) {

        Optional<Review> rev = repository.findById(reviewId);

        if (rev.isEmpty()) {
            return null;
        }
        Review r = rev.get();

        if (r.getUpVote().isEmpty() && r.getDownVote().isEmpty()) {
            repository.delete(r);
            return true;
        }
        return false;
    }

    @Override
    public ReviewDTO moderateReview(Long reviewID, String approved, Long userId) {

        Optional<Review> r = repository.findById(reviewID);

        if(r.isEmpty()){
            throw new ResourceNotFoundException("Review not found");
        }

        Optional<User> user = userService.getUserId(userId);

        if(user.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }

        if(approved.equalsIgnoreCase("approved")){
            r.get().getApproves().add(user.get());

            long nrOfApprovesWithRecommendation = 0;

            for (User userApproved:r.get().getApproves()) {
                List<Review> recommendedReviews = reviewRecommendation.getReviewsRecommended(userApproved.getUserId());
                if(recommendedReviews.contains(r.get())){
                    nrOfApprovesWithRecommendation++;
                }
                if(nrOfApprovesWithRecommendation>=2)
                    break;
            }

            if(nrOfApprovesWithRecommendation>=2){
                Boolean ap = r.get().setApprovalStatus(approved);
            }

        }else{
            // remove user from approval list if exists
            r.get().getApproves().remove(user.get());
            Boolean ap = r.get().setApprovalStatus(approved);

            if(!ap) {
                throw new IllegalArgumentException("Invalid status value");
            }
        }

        Review review = repository.save(r.get());

        ReviewDTO reviewDTO = ReviewMapper.toDto(review);
        reviewDTO.setSku(r.get().getProduct().getSku());
        return reviewDTO;
    }
}
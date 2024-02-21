package com.isep.acme.services;

import java.lang.IllegalArgumentException;


import com.isep.acme.exceptions.ResourceNotFoundException;
import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.review.AcceptRejectReviewDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository repository;
    private ProductRepository pRepository;
    private UserRepository uRepository;
    private ReviewRecommendation reviewRecommendation;

    @Autowired
    public void setProductRepo(@Value("${review.repo}") String bean, @Value("${product.repo}") String productBean, @Value("${user.repo}") String userBean, ApplicationContext applicationContext,  @Value("${review.recommendation}") String beanRecommendation){
        repository = (ReviewRepository) applicationContext.getBean(bean);
        pRepository = (ProductRepository) applicationContext.getBean(productBean);
        uRepository = (UserRepository) applicationContext.getBean(userBean);
        reviewRecommendation = (ReviewRecommendation) applicationContext.getBean(beanRecommendation);
    }

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RestService restService;

    @Autowired
    private Runner runner;


    @Override
    public Iterable<Review> getAll() {
        return repository.findAll();
    }
    @Override
    public List<ReviewDTO> getReviewsOfProduct(String sku, String status) {

        Optional<Product> product = pRepository.findBySku(sku);
        if (product.isEmpty()) return null;

        Optional<List<Review>> r = repository.findByProductIdStatus(product.get(), status);

        if (r.isEmpty()) return null;

        return ReviewMapper.toDtoList(r.get());
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
    public Boolean delete(Long reviewId) {

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
    public List<ReviewDTO> findPendingReview() {

        Optional<List<Review>> r = repository.findPendingReviews();

        if (r.isEmpty()) {
            return null;
        }

        return ReviewMapper.toDtoList(r.get());
    }


    @Override
    public List<ReviewDTO> findReviewsByUser(Long userID) {

        final Optional<User> user = uRepository.findById(userID);

        if (user.isEmpty()) return null;

        Optional<List<Review>> r = repository.findByUserId(user.get());

        if (r.isEmpty()) return null;

        return ReviewMapper.toDtoList(r.get());
    }

    @Override
    public List<ReviewDTO> recommendedReviews(Long userId) {

        //Check if user is valid
        final Optional<User> user = uRepository.findById(userId);

        if (user.isEmpty()) throw new IllegalArgumentException("User does not exists");

        return reviewRecommendation.getReviewsRecommended(userId)
                .stream()
                .map(ReviewMapper::toDto)
                .toList();
    }


    @Override
    public ReviewDTO create(final ReviewQueueDTO createReviewDTO) {

        final Optional<Product> product = pRepository.findBySku(createReviewDTO.getSku());

        if (product.isEmpty()) return null;

        final var user = uRepository.findById(createReviewDTO.getUserID());

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
        review.setIdReview(createReviewDTO.getIdReview());
        review = repository.save(review);

        return ReviewMapper.toDto(review);
    }

    @Override
    public ReviewDTO moderateReview(AcceptRejectReviewDTO acceptRejectReviewDTO) {

        Optional<Review> r = repository.findById(acceptRejectReviewDTO.getReviewId());

        if(r.isEmpty()){
            throw new ResourceNotFoundException("Review not found");
        }

        Boolean ap = r.get().setApprovalStatus(acceptRejectReviewDTO.getApproved());

        if(!ap) {
            throw new IllegalArgumentException("Invalid status value");
        }

        Review review = repository.save(r.get());

        return ReviewMapper.toDto(review);
    }

    @Override
    public ReviewDTO updateReview(ReviewQueueDTO reviewQueueDTO) {
        Optional<Review> r = repository.findById(reviewQueueDTO.getIdReview());

        if(r.isEmpty()){
            throw new ResourceNotFoundException("Review not found");
        }

        Review review = r.get();

        review.setApprovalStatus(reviewQueueDTO.getApprovalStatus());
        List<User> users = new ArrayList<>();
        for (Long user1:reviewQueueDTO.getApproves()) {
            User user2 = uRepository.getById(user1);
            if(user2 != null)
                users.add(user2);
        }
        review.setApproves(users);
        repository.save(review);
        return ReviewMapper.toDto(review);
    }

}
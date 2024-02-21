package com.isep.acme.services;

import com.isep.acme.exceptions.ResourceNotFoundException;

import java.lang.IllegalArgumentException;

import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.model.ReviewMapper;
import com.isep.acme.model.User;
import com.isep.acme.model.Vote;
import com.isep.acme.model.dto.VoteReviewDTO;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository repository;
    private ProductRepository pRepository;
    private UserRepository uRepository;
    private final UserServiceImpl userServiceImpl;
    private final RatingService ratingService;
    private final RestService restService;
    private ReviewRecommendation reviewRecommendation;

    @Autowired
    public void setRepos(@Value("${review.repo}") String bean, @Value("${product.repo}") String productBean, @Value("${user.repo}") String userBean,
                         @Value("${review.recommendation}") String beanRecommendation, ApplicationContext applicationContext){
        repository = (ReviewRepository) applicationContext.getBean(bean);
        pRepository = (ProductRepository) applicationContext.getBean(productBean);
        uRepository = (UserRepository) applicationContext.getBean(userBean);
        reviewRecommendation = (ReviewRecommendation) applicationContext.getBean(beanRecommendation);
    }

    @Override
    public Iterable<Review> getAll() {
        return repository.findAll();
    }

    @Override
    public ReviewDTO create(final CreateReviewDTO createReviewDTO, String sku) {

        final Optional<Product> product = pRepository.findBySku(sku);

        if (product.isEmpty()) return null;

        final var user = userServiceImpl.getUserId(createReviewDTO.getUserID());

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
    public List<ReviewDTO> getReviewsOfProduct(String sku, String status) {

        Optional<Product> product = pRepository.findBySku(sku);
        if (product.isEmpty()) return null;

        Optional<List<Review>> r = repository.findByProductIdStatus(product.get(), status);

        if (r.isEmpty()) return null;

        return ReviewMapper.toDtoList(r.get());
    }

    @Override
    public boolean addVoteToReview(Long reviewID, VoteReviewDTO voteReviewDTO) {

        Optional<Review> review = this.repository.findById(reviewID);

        if (review.isEmpty()) return false;

        Vote vote = new Vote(voteReviewDTO.getVote(), voteReviewDTO.getUserID());
        if (voteReviewDTO.getVote().equalsIgnoreCase("upVote")) {
            boolean added = review.get().addUpVote(vote);
            if (added) {
                this.repository.save(review.get());
                return true;
            }
        } else if (voteReviewDTO.getVote().equalsIgnoreCase("downVote")) {
            boolean added = review.get().addDownVote(vote);
            if (added) {
                this.repository.save(review.get());
                return true;
            }
        }
        return false;
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
    public Boolean DeleteReview(Long reviewId) {

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
    public ReviewDTO moderateReview(Long reviewID, String approved) throws ResourceNotFoundException, IllegalArgumentException {

        Optional<Review> r = repository.findById(reviewID);

        if (r.isEmpty()) {
            throw new ResourceNotFoundException("Review not found");
        }

        Boolean ap = r.get().setApprovalStatus(approved);

        if (!ap) {
            throw new IllegalArgumentException("Invalid status value");
        }

        Review review = repository.save(r.get());

        return ReviewMapper.toDto(review);
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

        return reviewRecommendation.getReviewsRecommended(user.get().getUserId())
                .stream()
                .map(ReviewMapper::toDto)
                .toList();
    }
}
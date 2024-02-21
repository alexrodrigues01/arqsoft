package com.isep.acme.services.mongoDB;

import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.*;
import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.model.dto.VoteReviewDTO;
import com.isep.acme.repositories.mongoDB.ProductRepositoryMongo;
import com.isep.acme.repositories.mongoDB.ReviewRepositoryMongo;
import com.isep.acme.repositories.mongoDB.UserRepositoryMongo;
import com.isep.acme.services.RestService;
import com.isep.acme.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("ReviewServiceMongoImpl")
public class ReviewServiceMongoImpl implements ReviewService {
    @Autowired
    ReviewRepositoryMongo repository;

    @Autowired
    ProductRepositoryMongo pRepository;

    @Autowired
    UserRepositoryMongo uRepository;

    @Autowired
    UserServiceMongoImpl userServiceImpl;

    @Autowired
    RatingServiceMongoImpl ratingService;

    @Autowired
    RestService restService;



    @Override
    public Iterable<Review> getAll() {
        Iterable<ReviewMongo> reviewsRedis= repository.findAll();
        List<Review> reviews= new ArrayList<>();
        for (ReviewMongo reviewRedis: reviewsRedis) {
            Product product= new Product(reviewRedis.getProduct().getProductID(), reviewRedis.getProduct().getSku(), reviewRedis.getProduct().getDesignation(), reviewRedis.getProduct().getDescription());
            User user= new User(reviewRedis.getUser().getUsername(),reviewRedis.getUser().getPassword(),reviewRedis.getUser().getFullName(),reviewRedis.getUser().getNif(),reviewRedis.getUser().getMorada());
            Rating rating= new Rating(reviewRedis.getRating().getRate()) ;
            reviews.add(new Review(reviewRedis.getReviewText(),reviewRedis.getPublishingDate(),product,reviewRedis.getFunFact(),rating,user));
        }
        return reviews;
    }

    @Override
    public ReviewDTO create(final CreateReviewDTO createReviewDTO, String sku) {

        final Optional<ProductMongo> product = pRepository.findBySku(sku);

        if(product.isEmpty()) return null;

        final var user = userServiceImpl.getUserId(createReviewDTO.getUserID());

        if(user.isEmpty()) return null;

        Rating rating = null;
        Optional<Rating> r = ratingService.findByRate(createReviewDTO.getRating());
        if(r.isPresent()) {
            rating = r.get();
        }
        else
            rating= new Rating(0.0);

        LocalDate date = LocalDate.now();

        String funfact = restService.getFunFact(date);

        if (funfact == null) return null;

        UserMongo userRedis= new UserMongo(user.get().getUsername(),user.get().getPassword(),user.get().getFullName(),user.get().getNif(),user.get().getMorada());

        RatingMongo ratingRedis= new RatingMongo(rating.getRate()) ;
        ReviewMongo review = new ReviewMongo(createReviewDTO.getReviewText(), date, product.get(), funfact, ratingRedis, userRedis);


        review = repository.save(review);

        if (review == null) return null;

        return ReviewMapper.toDto(review);
    }

    @Override
    public List<ReviewDTO> getReviewsOfProduct(String sku, String status) {

        Optional<ProductMongo> product = pRepository.findBySku(sku);
        if( product.isEmpty() ) return null;

        Optional<List<ReviewMongo>> r = repository.findByProductAndApprovalStatus(product.get(), status);

        if (r.isEmpty()) return null;

        return ReviewMapper.toDtoListMongo(r.get());
    }

    @Override
    public boolean addVoteToReview(Long reviewID, VoteReviewDTO voteReviewDTO) {

        Optional<ReviewMongo> review = this.repository.findById(reviewID);

        if (review.isEmpty()) return false;

        Vote vote = new Vote(voteReviewDTO.getVote(), voteReviewDTO.getUserID());
        if (voteReviewDTO.getVote().equalsIgnoreCase("upVote")) {
            boolean added = review.get().addUpVote(vote);
            if (added) {
                ReviewMongo reviewUpdated = this.repository.save(review.get());
                return reviewUpdated != null;
            }
        } else if (voteReviewDTO.getVote().equalsIgnoreCase("downVote")) {
            boolean added = review.get().addDownVote(vote);
            if (added) {
                ReviewMongo reviewUpdated = this.repository.save(review.get());
                return reviewUpdated != null;
            }
        }
        return false;
    }

    @Override
    public Double getWeightedAverage(Product product){
        ProductMongo productRedis= new ProductMongo(product.getProductID(), product.getSku(), product.getDesignation(), product.getDescription());
        Optional<List<ReviewMongo>> r = repository.findByProduct(productRedis);

        if (r.isEmpty()) return 0.0;

        double sum = 0;

        for (ReviewMongo rev: r.get()) {
            RatingMongo rate = rev.getRating();

            if (rate != null){
                sum += rate.getRate();
            }
        }

        return sum/r.get().size();
    }

    @Override
    public Boolean DeleteReview(Long reviewId)  {

        Optional<ReviewMongo> rev = repository.findById(reviewId);

        if (rev.isEmpty()){
            return null;
        }
        ReviewMongo r = rev.get();

        if (r.getUpVote().isEmpty() && r.getDownVote().isEmpty()) {
            repository.delete(r);
            return true;
        }
        return false;
    }

    @Override
    public List<ReviewDTO> findPendingReview(){

        Optional<List<ReviewMongo>> r = repository.getReviewByApprovalStatus("pending");

        if(r.isEmpty()){
            return null;
        }

        return ReviewMapper.toDtoListMongo(r.get());
    }

    @Override
    public ReviewDTO moderateReview(Long reviewID, String approved) throws ResourceNotFoundException, IllegalArgumentException {

        Optional<ReviewMongo> r = repository.findById(reviewID);

        if(r.isEmpty()){
            throw new ResourceNotFoundException("Review not found");
        }

        Boolean ap = r.get().setApprovalStatus(approved);

        if(!ap) {
            throw new IllegalArgumentException("Invalid status value");
        }

        ReviewMongo review = repository.save(r.get());

        return ReviewMapper.toDto(review);
    }


    @Override
    public List<ReviewDTO> findReviewsByUser(Long userID) {

        final Optional<UserMongo> user = uRepository.findById(userID);

        if(user.isEmpty()) return null;

        Optional<List<ReviewMongo>> r = repository.findByUserUserId(user.get().getUserId());

        if (r.isEmpty()) return null;

        return ReviewMapper.toDtoListMongo(r.get());
    }

    @Override
    public List<ReviewDTO> getRecomendedReviews() {
        return null;
    }
}
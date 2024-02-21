package com.isep.acme.services.redis;

import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.*;
import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.model.dto.VoteReviewDTO;
import com.isep.acme.repositories.redis.ProductRepositoryRedis;
import com.isep.acme.repositories.redis.ReviewRepositoryRedis;
import com.isep.acme.repositories.redis.UserRepositoryRedis;
import com.isep.acme.services.RestService;
import com.isep.acme.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service("ReviewServiceRedisImpl")
public class ReviewServiceRedisImpl implements ReviewService {

    @Autowired
    ReviewRepositoryRedis repository;

    @Autowired
    ProductRepositoryRedis pRepository;

    @Autowired
    UserRepositoryRedis uRepository;

    @Autowired
    UserServiceRedisImpl userServiceImpl;

    @Autowired
    RatingServiceRedisImpl ratingService;

    @Autowired
    RestService restService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;



    @Override
    public Iterable<Review> getAll() {
        Iterable<ReviewRedis> reviewsRedis= repository.findAll();
        List<Review> reviews= new ArrayList<>();
        for (ReviewRedis reviewRedis: reviewsRedis) {
            Product product= new Product(reviewRedis.getProduct().getProductID(), reviewRedis.getProduct().getSku(), reviewRedis.getProduct().getDesignation(), reviewRedis.getProduct().getDescription());
            User user= new User(reviewRedis.getUser().getUsername(),reviewRedis.getUser().getPassword(),reviewRedis.getUser().getFullName(),reviewRedis.getUser().getNif(),reviewRedis.getUser().getMorada());
            Rating rating= new Rating(reviewRedis.getRating().getRate()) ;
            reviews.add(new Review(reviewRedis.getReviewText(),reviewRedis.getPublishingDate(),product,reviewRedis.getFunFact(),rating,user));
            addVotes(reviewRedis);
        }
        return reviews;
    }

    @Override
    public ReviewDTO create(final CreateReviewDTO createReviewDTO, String sku) {

        final Optional<ProductRedis> product = pRepository.findBySku(sku);

        if(product.isEmpty()) return null;

        final var user = userServiceImpl.getUserId(createReviewDTO.getUserID());

        if(user.isEmpty()) return null;

        Rating rating = null;
        Optional<Rating> r = Optional.of(new Rating(0.0));
        if(r.isPresent()) {
            rating = r.get();
        }else {
            rating= new Rating(0.0);
        }

        LocalDate date = LocalDate.now();

        String funfact = restService.getFunFact(date);

        if (funfact == null) return null;

        UserRedis userRedis= new UserRedis(user.get().getUsername(),user.get().getPassword(),user.get().getFullName(),user.get().getNif(),user.get().getMorada());

        RatingRedis ratingRedis= new RatingRedis(rating.getRate()) ;
        ReviewRedis review = new ReviewRedis(createReviewDTO.getReviewText(), date, product.get(), funfact, ratingRedis, userRedis);


        review = repository.save(review);

        review.setUpvotesKey("upvotes:review:" + review.getIdReview());
        review.setDownVotesKey("downvotes:review:" + review.getIdReview());

        review= repository.save(review);


        if (review == null) return null;





        return ReviewMapper.toDto(review);
    }


    @Override
    public List<ReviewDTO> getReviewsOfProduct(String sku, String status) {

        Optional<ProductRedis> product = pRepository.findBySku(sku);
        if( product.isEmpty() ) return null;

        Optional<List<ReviewRedis>> r = repository.findByProductSkuAndApprovalStatus(product.get().getSku(), status);

        if (r.isEmpty()) return null;


        for(ReviewRedis reviewRedis: r.get()){
            addVotes(reviewRedis);
        }


        return ReviewMapper.toDtoListRedis(r.get());
    }


    private void addVotes(ReviewRedis reviewRedis){
        List<String> upVotes=getUpvotes(reviewRedis.getIdReview());
        List<String> downVotes =getDownvotes(reviewRedis.getIdReview());
        for(String upVote: upVotes){
            String[] voteArray= upVote.split(",");
            reviewRedis.addUpvote(new VoteRedis(voteArray[0],Long.parseLong(voteArray[1])));
        }
        for(String downVote: downVotes){
            String[] voteArray= downVote.split(",");
            reviewRedis.addDownVote(new VoteRedis(voteArray[0],Long.parseLong(voteArray[1])));
        }
    }

    @Override
    public boolean addVoteToReview(Long reviewID, VoteReviewDTO voteReviewDTO) {

        Optional<ReviewRedis> review = this.repository.findById(reviewID.toString());

        if (review.isEmpty()) return false;

        addVotes(review.get());
        VoteRedis vote = new VoteRedis(voteReviewDTO.getVote(), voteReviewDTO.getUserID());
        if (voteReviewDTO.getVote().equalsIgnoreCase("upVote")) {
            boolean added = review.get().addUpvote(vote);
            saveUpvote(reviewID,vote);
            return added;
        } else if (voteReviewDTO.getVote().equalsIgnoreCase("downVote")) {
            boolean added = review.get().addDownVote(vote);
            saveDownvote(reviewID,vote);
            return added;
        }
        return false;
    }

    @Override
    public Double getWeightedAverage(Product product){

        ProductRedis productRedis= new ProductRedis(product.getProductID(), product.getSku(), product.getDesignation(), product.getDescription());

        Optional<List<ReviewRedis>> r = repository.findByProductId(productRedis);

        if (r.isEmpty()) return 0.0;

        double sum = 0;

        for (ReviewRedis rev: r.get()) {
            RatingRedis rate = rev.getRating();

            if (rate != null){
                sum += rate.getRate();
            }
        }

        return sum/r.get().size();
    }

    @Override
    public Boolean DeleteReview(Long reviewId)  {

        Optional<ReviewRedis> rev = repository.findById(reviewId.toString());

        if (rev.isEmpty()){
            return null;
        }
        ReviewRedis r = rev.get();
        r.initializeVotes();
        addVotes(r);
        if (r.getUpVotes().isEmpty() && r.getDownVotes().isEmpty()) {
            repository.delete(r);
            return true;
        }
        return false;
    }

    @Override
    public List<ReviewDTO> findPendingReview(){

        Optional<List<ReviewRedis>> r = repository.findByApprovalStatus("pending");

        if(r.isEmpty()){
            return null;
        }
        for(ReviewRedis reviewRedis : r.get()){
            addVotes(reviewRedis);
        }

        return ReviewMapper.toDtoListRedis(r.get());
    }

    @Override
    public ReviewDTO moderateReview(Long reviewID, String approved) throws ResourceNotFoundException, IllegalArgumentException {

        Optional<ReviewRedis> r = repository.findById(reviewID.toString());

        if(r.isEmpty()){
            throw new ResourceNotFoundException("Review not found");
        }

        Boolean ap = r.get().setApprovalStatus(approved);

        if(!ap) {
            throw new IllegalArgumentException("Invalid status value");
        }

        ReviewRedis review = repository.save(r.get());
        addVotes(review);

        return ReviewMapper.toDto(review);
    }


    @Override
    public List<ReviewDTO> findReviewsByUser(Long userID) {

        final Optional<UserRedis> user = uRepository.findById(userID);

        if(user.isEmpty()) return null;

        Optional<List<ReviewRedis>> r = repository.findByUserUserId(userID);

        if (r.isEmpty()) return null;
        for(ReviewRedis reviewRedis : r.get()){
            addVotes(reviewRedis);
        }


        return ReviewMapper.toDtoListRedis(r.get());
    }

    @Override
    public List<ReviewDTO> getRecomendedReviews() {
        return null;
    }


    public void saveUpvote(Long reviewId, VoteRedis upvote) {
        stringRedisTemplate.opsForList().rightPush("upvotes:review:" + reviewId, String.format("%s,%s",upvote.getVote(),upvote.getUserID()));

    }

    public void saveDownvote(Long reviewId, VoteRedis downvote) {
        stringRedisTemplate.opsForList().rightPush("downvotes:review:" + reviewId, String.format("%s,%s",downvote.getVote(),downvote.getUserID()));
    }

    public List<String> getUpvotes(Long reviewId) {
        return stringRedisTemplate.opsForList().range("upvotes:review:" + reviewId, 0, -1);
    }

    public List<String> getDownvotes(Long reviewId) {
        return stringRedisTemplate.opsForList().range("downvotes:review:" + reviewId, 0, -1);
    }


    public List<String> findUpvotes(ReviewRedis review) {
        // Retrieve the upvotes list using the stored key.
        return stringRedisTemplate.opsForList().range(review.getUpvotesKey(), 0, -1);
    }

    public List<String> findDownvotes(ReviewRedis review) {
        // Retrieve the downvotes list using the stored key.
        return stringRedisTemplate.opsForList().range(review.getDownVotesKey(), 0, -1);
    }
}

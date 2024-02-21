package com.isep.acme.repositories.mongoDB;

import com.isep.acme.controllers.FileController;
import com.isep.acme.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepositoryMongo extends MongoRepository<ReviewMongo,Long> {
    Logger logger = LoggerFactory.getLogger(FileController.class);
    @Query("SELECT r FROM Review r WHERE r.product=:product ORDER BY r.publishingDate DESC")
    Optional<List<ReviewMongo>> findByProduct(ProductMongo product);


    @Query("SELECT r FROM Review r WHERE r.approvalStatus = :approvalStatus")
    Optional<List<ReviewMongo>> getReviewByApprovalStatus(@Param("approvalStatus") String approvalStatus);


    @Query("SELECT r FROM Review r WHERE r.product = :product AND r.approvalStatus = :status ORDER BY r.publishingDate DESC")
    Optional<List<ReviewMongo>> findByProductAndApprovalStatus(@Param("product") ProductMongo product, @Param("status") String status);

    @Query("SELECT r FROM Review r WHERE r.user=:user ORDER BY r.publishingDate DESC")
    Optional<List<ReviewMongo>> findByUserUserId(Long user);

}

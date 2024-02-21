package com.isep.acme.repositories.redis;

import com.isep.acme.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ReviewRepositoryRedis extends CrudRepository<ReviewRedis, String> {




    Optional<List<ReviewRedis>> findByProductId(ProductRedis product);



    Optional<List<ReviewRedis>> findPendingReviews();

    Optional<List<ReviewRedis>> findByApprovalStatus(String approvaStatus);

    Optional<List<ReviewRedis>> findActiveReviews();


    Optional<List<ReviewRedis>> findByProductSkuAndApprovalStatus(String sku, String status);


    Optional<List<ReviewRedis>> findByUser(UserRedis user);


    Optional<List<ReviewRedis>> findByUserUserId(Long user);
}


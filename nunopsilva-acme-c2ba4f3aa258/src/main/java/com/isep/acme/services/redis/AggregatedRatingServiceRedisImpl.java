package com.isep.acme.services.redis;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.AggregatedRatingRedis;
import com.isep.acme.model.Product;
import com.isep.acme.model.ProductRedis;
import com.isep.acme.repositories.redis.AggregatedRatingRepositoryRedis;
import com.isep.acme.repositories.redis.ProductRepositoryRedis;
import com.isep.acme.services.AggregatedRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("AggregatedRatingServiceRedisImpl")
public class AggregatedRatingServiceRedisImpl implements AggregatedRatingService {
    @Autowired
    AggregatedRatingRepositoryRedis arRepository;

    @Autowired
    private ProductRepositoryRedis pRepository;

    @Autowired
    ReviewServiceRedisImpl rService;

    @Autowired
    ProductServiceRedisImpl productService;

    @Override
    public AggregatedRating save(String sku ) {

        Optional<ProductRedis> productRedis = pRepository.findBySku( sku );

        if (productRedis.isEmpty()){
            return null;
        }
        Product product= new Product(productRedis.get().getProductID(), productRedis.get().getSku(), productRedis.get().getDesignation(), productRedis.get().getDescription());

        Double average = rService.getWeightedAverage(product);



        Optional<AggregatedRatingRedis> r = arRepository.findByProductId(productRedis.get());
        AggregatedRating aggregateF;

        if(r.isPresent()) {
            r.get().setAverage( average );
            arRepository.save(r.get());
            Product newProduct = new Product(r.get().getProduct().getSku(),r.get().getProduct().getDesignation(),r.get().getProduct().getDescription());
            aggregateF = new AggregatedRating(r.get().getAverage(), newProduct);
        }
        else {
            AggregatedRatingRedis f = new AggregatedRatingRedis(average, productRedis.get());
            arRepository.save(f);
            Product newProduct = new Product(f.getProduct().getSku(),f.getProduct().getDesignation(),f.getProduct().getDescription());
            aggregateF = new AggregatedRating(f.getAverage(), newProduct);
        }


        return aggregateF;
    }
}

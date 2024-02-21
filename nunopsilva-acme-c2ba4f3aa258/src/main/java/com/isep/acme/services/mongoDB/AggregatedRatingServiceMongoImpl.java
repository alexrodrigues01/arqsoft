package com.isep.acme.services.mongoDB;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.model.ProductMongo;


import com.isep.acme.repositories.mongoDB.AggregatedRatingRepositoryMongo;
import com.isep.acme.repositories.mongoDB.ProductRepositoryMongo;
import com.isep.acme.services.AggregatedRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("AggregatedRatingServiceMongoImpl")
public class AggregatedRatingServiceMongoImpl implements AggregatedRatingService {

    @Autowired
    AggregatedRatingRepositoryMongo arRepository;

    @Autowired
    private ProductRepositoryMongo pRepository;

    @Autowired
    ReviewServiceMongoImpl rService;

    @Autowired
    ProductServiceMongoImpl productService;


    @Override
    public AggregatedRating save(String sku ) {

        Optional<ProductMongo> productMongo = pRepository.findBySku( sku );

        if (productMongo.isEmpty()){
            return null;
        }

        Product product= new Product(productMongo.get().getProductID(), productMongo.get().getSku(), productMongo.get().getDesignation(), productMongo.get().getDescription());

        Double average = rService.getWeightedAverage(product);


        Optional<AggregatedRating> r = arRepository.findByProduct(product);
        AggregatedRating aggregateF;

        if(r.isPresent()) {
            r.get().setAverage( average );
            aggregateF = arRepository.save(r.get());
        }
        else {
            AggregatedRating f = new AggregatedRating(average, product);
            aggregateF = arRepository.save(f);
        }

        return aggregateF;
    }


}

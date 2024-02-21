package com.isep.acme.services.h2;

import com.isep.acme.services.AggregatedRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.repositories.h2.AggregatedRatingRepository;
import com.isep.acme.repositories.h2.ProductRepository;

import java.util.Optional;

@Service("AggregatedRatingServiceImpl")
public class AggregatedRatingServiceImpl implements AggregatedRatingService {

    @Autowired
    AggregatedRatingRepository arRepository;

    @Autowired
    private ProductRepository pRepository;

    @Autowired
    ReviewServiceImpl rService;

    @Autowired
    ProductServiceImpl productService;


    @Override
    public AggregatedRating save( String sku ) {

        Optional<Product> product = pRepository.findBySku( sku );

        if (product.isEmpty()){
            return null;
        }

        Double average = rService.getWeightedAverage(product.get());


        Optional<AggregatedRating> r = arRepository.findByProductId(product.get());
        AggregatedRating aggregateF;

        if(r.isPresent()) {
            r.get().setAverage( average );
            aggregateF = arRepository.save(r.get());
        }
        else {
            AggregatedRating f = new AggregatedRating(average, product.get());
            aggregateF = arRepository.save(f);
        }

        return aggregateF;
    }


}

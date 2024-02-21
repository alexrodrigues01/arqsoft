package com.isep.acme.services;

import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.repositories.AggregatedRatingRepository;
import com.isep.acme.repositories.ProductRepository;

import java.util.Optional;

@Service
public class AggregatedRatingServiceImpl implements AggregatedRatingService{

    private AggregatedRatingRepository arRepository;
    private ProductRepository pRepository;

    @Autowired
    private ReviewService rService;

    @Autowired
    private ProductService productService;

    @Autowired
    public void setRepos(@Value("${aggregated.repo}") String bean, @Value("${product.repo}") String productBean, ApplicationContext applicationContext){
        pRepository = (ProductRepository) applicationContext.getBean(productBean);
        arRepository = (AggregatedRatingRepository) applicationContext.getBean(bean);
    }

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

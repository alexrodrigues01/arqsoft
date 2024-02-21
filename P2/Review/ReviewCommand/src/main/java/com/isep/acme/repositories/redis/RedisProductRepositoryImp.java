package com.isep.acme.repositories.redis;

import com.isep.acme.model.Product;
import com.isep.acme.repositories.ProductRepository;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("RedisProductRepositoryImp")
public class RedisProductRepositoryImp extends RedisBaseRepository<Product, Long> implements ProductRepository {

    public RedisProductRepositoryImp(final RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, Product.class);
    }

    @SneakyThrows
    @Override
    public <S extends Product> S save(S entity) {
        final Optional<Product> possibleProductInDb=findBySku(entity.getSku());
        if(possibleProductInDb.isPresent() && !possibleProductInDb.get().getProductID().equals(entity.getProductID())){
            throw new Exception("Sku must be unique");
        }
        return super.save(entity);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return hashOperations.entries(Product.class.getSimpleName()).values().stream()
                .filter(product -> product.getSku().equalsIgnoreCase(sku))
                .findAny();
    }
}

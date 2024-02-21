package com.isep.acme.services.redis;

import com.isep.acme.model.*;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ProductDetailDTO;
import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.repositories.redis.ProductRepositoryRedis;
import com.isep.acme.services.ProductService;
import com.isep.acme.services.SkuGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("ProductServiceRedisImpl")
public class ProductServiceRedisImpl implements ProductService{

    @Autowired
    private ProductRepositoryRedis repository;

    private SkuGeneration skuGeneration;

    @Autowired
    public void setSkuGeneration(@Value("${sku.generation}") String bean, ApplicationContext applicationContext){
        skuGeneration= (SkuGeneration) applicationContext.getBean(bean);
    }
    @Override
    public Optional<Product> getProductBySku(final String sku ) {
        ProductRedis productRedis;
        Optional<ProductRedis> productRedisOptional= repository.findBySku(sku);
        if(productRedisOptional.isPresent()){
            productRedis= productRedisOptional.get();

            return Optional.of(new Product(productRedis.getProductID(), productRedis.getSku(), productRedis.getDesignation(), productRedis.getDescription()));
        }

        return Optional.empty();
    }

    @Override
    public Optional<ProductDTO> findBySku(String sku) {
        final Optional<ProductRedis> product = repository.findBySku(sku);

        if( product.isEmpty() )
            return Optional.empty();
        else
            return Optional.of( product.get().toDto() );
    }


    @Override
    public Iterable<ProductDTO> findByDesignation(final String designation) {
        Iterable<ProductRedis> p = repository.findByDesignation(designation);
        List<ProductDTO> pDto = new ArrayList();
        for (ProductRedis pd:p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    @Override
    public Iterable<ProductDTO> getCatalog() {
        Iterable<ProductRedis> p = repository.findAll();
        List<ProductDTO> pDto = new ArrayList();
        for (ProductRedis pd:p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    public ProductDetailDTO getDetails(String sku) {

        Optional<ProductRedis> p = repository.findBySku(sku);

        if (p.isEmpty())
            return null;
        else
            return new ProductDetailDTO(p.get().getSku(), p.get().getDesignation(), p.get().getDescription());
    }


    @Override
    public ProductDTO create(final Product product) {
        final ProductRedis p = new ProductRedis(product.getSku(), product.getDesignation(), product.getDescription());
        if(repository.findBySku(product.getSku()).isEmpty()){
            return repository.save(p).toDto();
        }
        throw new RuntimeException("Duplicated product");

    }

    @Override
    public ProductDTO createWithoutSku(final ProductWithoutSkuDTO productWithoutSku) {
        final ProductRedis p = new ProductRedis(skuGeneration.getSkuAlgorithm(productWithoutSku), productWithoutSku.getDesignation(), productWithoutSku.getDescription());

        return repository.save(p).toDto();
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) {
        ProductRedis productRedis= new ProductRedis(sku, product.getDesignation(), product.getDescription());

        final Optional<ProductRedis> productToUpdate = repository.findBySku(sku);

        if( productToUpdate.isEmpty() ) return null;

        productToUpdate.get().updateProduct(productRedis);

        ProductRedis productUpdated = repository.save(productToUpdate.get());

        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {
        Optional<ProductRedis> productRedisOptional= repository.findBySku(sku);
        productRedisOptional.ifPresent(productRedis -> repository.delete(productRedis));
    }
}




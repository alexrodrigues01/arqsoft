package com.isep.acme.services.mongoDB;

import com.isep.acme.model.*;

import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ProductDetailDTO;
import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.repositories.mongoDB.ProductRepositoryMongo;
import com.isep.acme.services.ProductService;
import com.isep.acme.services.SkuGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("ProductServiceMongoImpl")
public class ProductServiceMongoImpl implements ProductService {

    @Autowired
    private ProductRepositoryMongo repository;

    private SkuGeneration skuGeneration;

    @Autowired
    public void setSkuGeneration(@Value("${sku.generation}") String bean, ApplicationContext applicationContext){
        skuGeneration= (SkuGeneration) applicationContext.getBean(bean);
    }

    @Override
    public Optional<Product> getProductBySku(final String sku ) {

        ProductMongo productMongo = repository.findBySku(sku).get();
        Product product = new Product(productMongo.getProductID(), productMongo.getSku(), productMongo.getDesignation(), productMongo.getDescription());
        return Optional.of(product);
    }

    @Override
    public Optional<ProductDTO> findBySku(String sku) {
        final Optional<ProductMongo> product = repository.findBySku(sku);

        if( product.isEmpty() )
            return Optional.empty();
        else
            return Optional.of( product.get().toDto() );
    }


    @Override
    public Iterable<ProductDTO> findByDesignation(final String designation) {
        Iterable<ProductMongo> p = repository.findByDesignation(designation);
        List<ProductDTO> pDto = new ArrayList();
        for (ProductMongo pd:p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    @Override
    public Iterable<ProductDTO> getCatalog() {
        Iterable<ProductMongo> p = repository.findAll();
        List<ProductDTO> pDto = new ArrayList();
        for (ProductMongo pd:p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    public ProductDetailDTO getDetails(String sku) {

        Optional<ProductMongo> p = repository.findBySku(sku);

        if (p.isEmpty())
            return null;
        else
            return new ProductDetailDTO(p.get().getSku(), p.get().getDesignation(), p.get().getDescription());
    }


    @Override
    public ProductDTO create(final Product product) {
        final ProductMongo p = new ProductMongo(product.getSku(), product.getDesignation(), product.getDescription());
        if(repository.findBySku(p.sku).isEmpty()){
            return repository.save(p).toDto();
        }
        throw new RuntimeException("Prduct already exists");

    }

    @Override
    public ProductDTO createWithoutSku(final ProductWithoutSkuDTO productWithoutSku) {
        final ProductMongo p = new ProductMongo(skuGeneration.getSkuAlgorithm(productWithoutSku), productWithoutSku.getDesignation(), productWithoutSku.getDescription());

        return repository.save(p).toDto();
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) {
        ProductMongo productMongo = new ProductMongo(sku, product.getDesignation(), product.getDescription());

        final Optional<ProductMongo> productToUpdate = repository.findBySku(sku);

        if( productToUpdate.isEmpty() ) return null;

        productToUpdate.get().updateProduct(productMongo);

        ProductMongo productUpdatedToInsert= new ProductMongo(productToUpdate.get().sku,productToUpdate.get().getDesignation(),productToUpdate.get().getDescription());
        ProductMongo productUpdated = repository.save(productToUpdate.get());

        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {
        repository.deleteBySku(sku);
    }
}
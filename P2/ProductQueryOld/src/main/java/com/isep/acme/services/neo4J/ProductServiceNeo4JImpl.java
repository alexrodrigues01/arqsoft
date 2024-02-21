package com.isep.acme.services.neo4J;

import com.isep.acme.model.*;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ProductDetailDTO;
import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.repositories.neo4J.ProductRepositoryNeo4j;
import com.isep.acme.services.ProductService;
import com.isep.acme.services.SkuGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("ProductServiceNeo4JImpl")
public class ProductServiceNeo4JImpl implements ProductService {
    @Autowired
    private ProductRepositoryNeo4j repository;

    private SkuGeneration skuGeneration;

    @Autowired
    public void setSkuGeneration(@Value("${sku.generation}") String bean, ApplicationContext applicationContext){
        skuGeneration= (SkuGeneration) applicationContext.getBean(bean);
    }

    @Override
    public Optional<Product> getProductBySku( final String sku ) {
        ProductNeo4J productNeo4J = repository.findBySku(sku).get();
        Product product = new Product(productNeo4J.getId(), productNeo4J.getSku(), productNeo4J.getDesignation(), productNeo4J.getDescription());
        return Optional.of(product);
    }

    @Override
    public Optional<ProductDTO> findBySku(String sku) {
        final Optional<ProductNeo4J> product = repository.findBySku(sku);

        if( product.isEmpty() )
            return Optional.empty();
        else
            return Optional.of( product.get().toDto() );
    }

    @Override
    public Iterable<ProductDTO> findByDesignation(final String designation) {
        Iterable<ProductNeo4J> p = repository.findByDesignation(designation);
        List<ProductDTO> pDto = new ArrayList();
        for (ProductNeo4J pd:p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    @Override
    public Iterable<ProductDTO> getCatalog() {
        Iterable<ProductNeo4J> p = repository.findAll();
        List<ProductDTO> pDto = new ArrayList();
        for (ProductNeo4J pd:p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    public ProductDetailDTO getDetails(String sku) {

        Optional<ProductNeo4J> p = repository.findBySku(sku);

        if (p.isEmpty())
            return null;
        else
            return new ProductDetailDTO(p.get().getSku(), p.get().getDesignation(), p.get().getDescription());
    }


    @Override
    public ProductDTO create(final Product product) {
        final ProductNeo4J p = new ProductNeo4J(product.getSku(), product.getDesignation(), product.getDescription());
        if(repository.findBySku(product.getSku()).isEmpty()){
            return repository.save(p).toDto();
        }
        throw new RuntimeException("Duplicated product");
    }

    @Override
    public ProductDTO createWithoutSku(final ProductWithoutSkuDTO productWithoutSku) {
        final ProductNeo4J p = new ProductNeo4J(skuGeneration.getSkuAlgorithm(productWithoutSku), productWithoutSku.getDesignation(), productWithoutSku.getDescription());

        return repository.save(p).toDto();
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) {
        ProductNeo4J productNeo4J = new ProductNeo4J(sku, product.getDesignation(), product.getDescription());

        final Optional<ProductNeo4J> productToUpdate = repository.findBySku(sku);

        if( productToUpdate.isEmpty() ) return null;

        productToUpdate.get().updateProduct(productNeo4J);

        ProductNeo4J productUpdated = repository.save(productToUpdate.get());

        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {
        repository.deleteBySku(sku);
    }
}

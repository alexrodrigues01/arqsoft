package com.isep.acme.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isep.acme.model.product.ProductDTO;
import com.isep.acme.repositories.Idable;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Node;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

@Entity
@Node
@Data
@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Idable<Long>, Serializable {

    private static final int MIN_SKU_LENGHT = 8;
    private static final int MAX_SKU_LENGHT = 12;

    @Id
    @org.springframework.data.annotation.Id
    private Long productID;

    @Column(nullable = false, unique = true)
    @Indexed(unique = true)
    private String sku;

    public Product() {
        this.productID = generateId();
    }

    public Product(final Long productID, final String sku) {
        this.productID = Objects.requireNonNull(productID);
        setSku(sku);
    }

    public Product(final String sku) {
        this();
        setSku(sku);
    }


    public void setSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is a mandatory attribute of Product.");
        }
        if (sku.length() > MAX_SKU_LENGHT || sku.length() < MIN_SKU_LENGHT) {
            throw new IllegalArgumentException(String.format("SKU must be %d - %d characters long.", MIN_SKU_LENGHT, MAX_SKU_LENGHT));
        }

        this.sku = sku;
    }

    @Override
    public Long getId() {
        return productID;
    }

    @Override
    public Long generateId() {
        long value = new Random().nextInt();
        return value > 0 ? value : value * -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return sku.equals(product.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }

    public static int getMinSkuLenght() {
        return MIN_SKU_LENGHT;
    }

    public static int getMaxSkuLenght() {
        return MAX_SKU_LENGHT;
    }

}

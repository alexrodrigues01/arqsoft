package com.isep.acme.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isep.acme.model.dto.ProductDTO;
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

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String description;
    /*
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<Review>(); */

    public Product() {
        this.productID = generateId();
    }

    public Product(final Long productID, final String sku) {
        this.productID = Objects.requireNonNull(productID);
        setSku(sku);
    }

    public Product(final Long productID, final String sku, final String designation, final String description) {
        this(productID, sku);
        setDescription(description);
        setDesignation(designation);
    }

    public Product(final String sku) {
        this();
        setSku(sku);
    }

    public Product(final String sku, final String designation, final String description) {
        this(sku);
        setDescription(description);
        setDesignation(designation);
    }

    public Product(final String designation, final String description) {
        setDescription(description);
        setDesignation(designation);
    }

    public void setSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is a mandatory attribute of Product.");
        }

        this.sku = sku;
    }

    public void setDesignation(String designation) {
        if (designation == null || designation.isBlank()) {
            throw new IllegalArgumentException("Designation is a mandatory attribute of Product.");
        }
        if (designation.length() > 50) {
            throw new IllegalArgumentException("Designation must not be greater than 50 characters.");
        }
        this.designation = designation;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is a mandatory attribute of Product.");
        }

        if (description.length() > 1200) {
            throw new IllegalArgumentException("Description must not be greater than 1200 characters.");
        }

        this.description = description;
    }


    public void updateProduct(Product p) {
        setDesignation(p.designation);
        setDescription(p.description);
    }

    public ProductDTO toDto() {
        return new ProductDTO(this.sku, this.designation);
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

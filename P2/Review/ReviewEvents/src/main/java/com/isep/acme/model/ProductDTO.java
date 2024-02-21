package com.isep.acme.model;

import java.io.Serializable;

public class ProductDTO implements Serializable {
    private String sku;
    private String designation;

    private String description;

    private Long productId;

    public ProductDTO(String sku, String designation,Long productId,String description) {
        this.sku = sku;
        this.designation = designation;
        this.productId=productId;
        this.description=description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "sku='" + sku + '\'' +
                ", designation='" + designation + '\'' +
                ", productId=" + productId +
                '}';
    }
}
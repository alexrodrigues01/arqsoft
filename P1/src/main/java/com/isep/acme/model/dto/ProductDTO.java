package com.isep.acme.model.dto;

public class ProductDTO {
    private String sku;
    private String designation;


    private boolean published;

    public ProductDTO(String sku, String designation, boolean published) {
        this.sku = sku;
        this.designation = designation;
        this.published= published;
    }

    public ProductDTO(String sku, String designation) {
        this.sku = sku;
        this.designation = designation;
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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDTO that = (ProductDTO) o;

        if (!sku.equals(that.sku)) return false;
        return designation != null ? designation.equals(that.designation) : that.designation == null;
    }

    @Override
    public int hashCode() {
        int result = sku.hashCode();
        result = 31 * result + (designation != null ? designation.hashCode() : 0);
        return result;
    }
}

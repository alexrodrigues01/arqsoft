package com.isep.acme.model.product;

public class ProductDTO {
    private String sku;
    private String designation;

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

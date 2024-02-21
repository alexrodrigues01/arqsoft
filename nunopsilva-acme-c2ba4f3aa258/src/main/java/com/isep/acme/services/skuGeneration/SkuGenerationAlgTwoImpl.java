package com.isep.acme.services.skuGeneration;

import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.services.SkuGeneration;
import org.springframework.stereotype.Service;

@Service("SkuGenerationAlgTwoImpl")
public class SkuGenerationAlgTwoImpl implements SkuGeneration {

    @Override
    public String getSkuAlgorithm(ProductWithoutSkuDTO p) {
        String designation = p.getDesignation();

        int hashCode = designation.hashCode();

        String hexHashCode = Integer.toHexString(hashCode);

        while (hexHashCode.length() < 10) {
            hexHashCode = "0" + hexHashCode;
        }

        int start = (hexHashCode.length() - 10) / 2;
        String sku = hexHashCode.substring(start, start + 10);

        return sku;
    }
}

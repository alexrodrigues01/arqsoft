package com.isep.acme.services.skuGeneration;

import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.services.SkuGeneration;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service("SkuGenerationAlgOneImpl")
public class SkuGenerationAlgOneImpl implements SkuGeneration {

    @Override
    public String getSkuAlgorithm(ProductWithoutSkuDTO p) {
        StringBuilder sku = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                sku.append(random.nextInt(10));
            } else {
                sku.append((char) (random.nextInt(26) + 'A'));
            }
        }

        sku.append((char) (random.nextInt(26) + 'A'));

        sku.append(random.nextInt(10));

        sku.append((char) (random.nextInt(26) + 'A'));

        sku.append(random.nextInt(10));

        String specialCharacters = "!@#$%^&*()_+";
        char specialChar = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        sku.append(specialChar);

        return sku.toString();
    }
}

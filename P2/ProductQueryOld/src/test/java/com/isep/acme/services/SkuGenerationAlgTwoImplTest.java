package com.isep.acme.services;

import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.services.skuGeneration.SkuGenerationAlgTwoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SkuGenerationAlgTwoImplTest {

    private SkuGenerationAlgTwoImpl skuGeneration;

    private ProductWithoutSkuDTO p;

    @BeforeEach
    void setUp() {
        skuGeneration = new SkuGenerationAlgTwoImpl();
        p = Mockito.mock(ProductWithoutSkuDTO.class);
    }

    @Test
    void testGetSkuAlgorithm() {

        Mockito.when(p.getDesignation()).thenReturn("Sample Designation");

        String sku = skuGeneration.getSkuAlgorithm(p);

        int hashCode = "Sample Designation".hashCode();
        String hexHashCode = Integer.toHexString(hashCode);
        while (hexHashCode.length() < 10) {
            hexHashCode = "0" + hexHashCode;
        }
        int start = (hexHashCode.length() - 10) / 2;
        String expectedSku = hexHashCode.substring(start, start + 10);

        assertEquals(expectedSku, sku);
    }

    @Test
    void testGetSkuAlgorithmWithEmptyDesignation() {

        Mockito.when(p.getDesignation()).thenReturn("");

        String sku = skuGeneration.getSkuAlgorithm(p);

        assertEquals("0000000000", sku);
    }

}

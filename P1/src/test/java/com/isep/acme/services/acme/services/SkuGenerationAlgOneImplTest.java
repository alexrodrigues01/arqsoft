package com.isep.acme.services.acme.services;

import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.services.skuGeneration.SkuGenerationAlgOneImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SkuGenerationAlgOneImplTest {

    private SkuGenerationAlgOneImpl skuGeneration;

    private ProductWithoutSkuDTO p;


    @BeforeEach
    void setUp() {
        skuGeneration = new SkuGenerationAlgOneImpl();
        p = Mockito.mock(ProductWithoutSkuDTO.class);
    }

    @Test
    void testSkuLength() {
        String sku = skuGeneration.getSkuAlgorithm(p);
        assertEquals(11, sku.length(), "SKU length should be 11 characters");
    }

    @Test
    void testSkuFormat() {
        String sku = skuGeneration.getSkuAlgorithm(p);
        assertTrue(sku.matches("^[0-9A-Z]+[A-Z][0-9][A-Z][0-9][!@#$%^&*()_+]$"), "SKU format should match the pattern");
    }

    @Test
    public void testGetSkuAlgorithmOne() {
        String sku = skuGeneration.getSkuAlgorithm(p);

        // Check the length of the generated SKU
        assertEquals(11, sku.length(), "SKU length should be 11 characters");

        // Check that the SKU format is correct (alternating digits and uppercase letters)
        for (int i = 0; i < 5; i++) {
            char character = sku.charAt(i);
            if (i % 2 == 0) {
                assertTrue(Character.isDigit(character), "Character at index " + i + " should be a digit");
            } else {
                assertTrue(Character.isLetter(character), "Character at index " + i + " should be a letter");
                assertTrue(Character.isUpperCase(character), "Character at index " + i + " should be uppercase");
            }
        }

        char character6 = sku.charAt(6);
        assertTrue(Character.isLetter(character6), "Character at index " + 6 + " should be a letter");
        assertTrue(Character.isUpperCase(character6), "Character at index " + 6 + " should be uppercase");

        char character7 = sku.charAt(7);
        assertTrue(Character.isDigit(character7), "Character at index " + 7 + " should be a digit");

        char character8 = sku.charAt(8);
        assertTrue(Character.isLetter(character8), "Character at index " + 8 + " should be a letter");
        assertTrue(Character.isUpperCase(character8), "Character at index " + 8 + " should be uppercase");

        char character9 = sku.charAt(9);
        assertTrue(Character.isDigit(character9), "Character at index " + 9 + " should be a digit");


        // Check that the last character is a special character
        char lastCharacter = sku.charAt(sku.length() - 1);
        String specialCharacters = "!@#$%^&*()_+";
        assertTrue(specialCharacters.contains(String.valueOf(lastCharacter)), "Last character should be a special character");
    }

    @Test
    public void testGeneratedSKUsAreUnique() {

        // Generate multiple SKUs and check for uniqueness
        int numberOfSKUsToGenerate = 1000;
        String[] skus = new String[numberOfSKUsToGenerate];
        for (int i = 0; i < numberOfSKUsToGenerate; i++) {
            skus[i] = skuGeneration.getSkuAlgorithm(p);
        }

        // Ensure all generated SKUs are unique
        for (int i = 0; i < numberOfSKUsToGenerate; i++) {
            for (int j = i + 1; j < numberOfSKUsToGenerate; j++) {
                assertNotEquals(skus[i], skus[j]);
            }
        }
    }
}

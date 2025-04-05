package com.akazaki.api.application.commands.DeleteProduct;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.DeleteProductCommand;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;

public class DeleteProductUnitTest {
    private ProductRepository productRepository;
    private DeleteProductCommandHandler commandHandler;
    private Product expectedProduct;

    @Before
    public void setUp() {
        productRepository = new InMemoryProductRepository();

        commandHandler = new DeleteProductCommandHandler(productRepository);

        // Setup test data
        Category category1 = new Category(1L, "Limonade");
        Category category2 = new Category(2L, "Raisin");

        expectedProduct = Product.create(
                "Test Product",
                "Test Description",
                10.0,
                100,
                Arrays.asList(category1, category2)
        );
        expectedProduct.setId(1L);
        productRepository.save(expectedProduct);
    }

    @Test
    public void deleteAProductSuccessfully() {
        commandHandler.handle(new DeleteProductCommand(expectedProduct.getId()));
        assertFalse(productRepository.findById(expectedProduct.getId()).isPresent());

    }
}

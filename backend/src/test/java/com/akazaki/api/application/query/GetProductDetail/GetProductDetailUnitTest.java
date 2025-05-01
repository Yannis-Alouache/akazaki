package com.akazaki.api.application.query.GetProductDetail;

import com.akazaki.api.application.commands.CreateProduct.CreateProductCommandHandler;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.CreateProductCommand;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.persistence.Category.InMemoryCategoryRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.Assert.*;

@DisplayName("Get Product Detail Unit Tests")
class GetProductDetailUnitTest {
    private ProductRepository productRepository;

    private CreateProductCommandHandler handler;
    private Product expectedProduct;

    @BeforeEach
    public void setUp() {
        CategoryRepository categoryRepository = new InMemoryCategoryRepository();
        productRepository = new InMemoryProductRepository();

        handler = new CreateProductCommandHandler(productRepository, categoryRepository);

        // Setup test data
        Category category1 = new Category(1L, "Limonade");
        Category category2 = new Category(2L, "Raisin");

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        expectedProduct = Product.create(
                "Test Product",
                "Test Description",
                10.0,
                100,
                Arrays.asList(category1, category2)
        );
        expectedProduct.setId(1L);
    }

    @Test
    @DisplayName("Get Product Detail Successfully")
    void GetProductDetailSuccessfully() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
                "Test Product",
                "Test Description",
                10.0,
                100,
                Arrays.asList(1L, 2L)
        );

        // Act
        handler.handle(command);
        Product product = productRepository.findById(1L).orElseThrow();

        // Assert
        assertNotNull(product);
        assertEquals(product.getId(), expectedProduct.getId());
        assertEquals(product.getName(), expectedProduct.getName());
        assertEquals(product.getDescription(), expectedProduct.getDescription());
        assertEquals(product.getPrice(), expectedProduct.getPrice(), 0.0);
        assertEquals(product.getStock(), expectedProduct.getStock());
        assertEquals(product.getCategories(), expectedProduct.getCategories());
    }
}

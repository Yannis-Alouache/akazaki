package com.akazaki.api.application.commands.CreateProduct;

import java.util.Arrays;

import com.akazaki.api.config.fixtures.CategoryFixture;
import com.akazaki.api.config.fixtures.ProductFixture;
import com.akazaki.api.infrastructure.persistence.Category.InMemoryCategoryRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.akazaki.api.domain.exceptions.ProductAlreadyExistException;
import com.akazaki.api.domain.exceptions.UnableToFetchCategoriesException;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.CreateProductCommand;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Create Product Unit Tests")
class CreateProductUnitTest {
    private static final Logger log = LoggerFactory.getLogger(CreateProductUnitTest.class);
    private static ProductRepository productRepository;
    private static CreateProductCommandHandler handler;
    private static Product expectedProduct;
    private static CategoryFixture categoryFixture;

    @BeforeAll
    public static void setUp() {
        CategoryRepository categoryRepository = new InMemoryCategoryRepository();
        productRepository = new InMemoryProductRepository();
        categoryFixture = new CategoryFixture(categoryRepository);
        ProductFixture productFixture = new ProductFixture(productRepository, categoryFixture);

        handler = new CreateProductCommandHandler(productRepository, categoryRepository);

        // Setup test data
        expectedProduct = productFixture.drink;
        expectedProduct.setId(1L);
    }

    @Test
    @DisplayName("Create A Product Successfully")
    void createAProductSuccessfully() {
        Product basicProduct = Product.create(
                2L,
                "Test Product",
                "Test Description",
                10.0,
                10,
                null,
                Arrays.asList(categoryFixture.category)
        );

        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            basicProduct.getName(),
            basicProduct.getDescription(),
            basicProduct.getPrice(),
            basicProduct.getStock(),
            Arrays.asList(categoryFixture.category.getId())
        );

        // Act
        handler.handle(command);
        Product product = productRepository.findById(2L).orElseThrow();

        // Assert
        assertThat(product).usingRecursiveComparison().isEqualTo(basicProduct);
    }

    @Test
    @DisplayName("Prevent Product Creation When Category Not Found")
    void preventProductCreationWhenCategoryNotFound() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            expectedProduct.getName(),
            expectedProduct.getDescription(),
            expectedProduct.getPrice(),
            expectedProduct.getStock(),
            // Non existing categories
            Arrays.asList(999L)
        );
        log.info(categoryFixture.category.toString());

        // Act / Assert
        assertThrows(UnableToFetchCategoriesException.class, () -> handler.handle(command));
    }

    @Test
    @DisplayName("Prevent Product Creation When Already Existing")
    void preventProductCreationWhenAlreadyExisting() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            expectedProduct.getName(),
            expectedProduct.getDescription(),
            expectedProduct.getPrice(),
            expectedProduct.getStock(),
            Arrays.asList(categoryFixture.category.getId())
        );

        log.info(command.toString());

        handler.handle(command);

        // Act / Assert
        assertThrows(ProductAlreadyExistException.class, () -> handler.handle(command));
    }
}
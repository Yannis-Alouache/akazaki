package com.akazaki.api.application.query.ListProducts;

import com.akazaki.api.application.queries.ListProducts.ListProductsQueryHandler;
import com.akazaki.api.config.fixtures.ProductFixture;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.queries.ListProductsQuery;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("List Products Unit Tests")
class ListProductsQueryHandlerTest {
    private ListProductsQueryHandler listProductsQueryHandler;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        listProductsQueryHandler = new ListProductsQueryHandler(productRepository);
    }

    @Test
    @DisplayName("Should return all products when repository has products")
    void shouldReturnAllProductsWhenRepositoryHasProducts() {
        // Given
        productRepository.save(ProductFixture.ramuneFraise);
        productRepository.save(ProductFixture.ultraIceTea);

        // When
        List<Product> result = listProductsQueryHandler.handle(new ListProductsQuery());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(ProductFixture.ramuneFraise, ProductFixture.ultraIceTea);
    }

    @Test
    @DisplayName("Should return empty list when repository is empty")
    void shouldReturnEmptyListWhenRepositoryIsEmpty() {
        // When
        List<Product> result = listProductsQueryHandler.handle(new ListProductsQuery());

        // Then
        assertThat(result).isEmpty();
    }
} 
package com.akazaki.api.application.queries.ListProducts;

import com.akazaki.api.config.fixtures.ProductFixture;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.queries.ListProductsQuery;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
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
        Page<Product> result = listProductsQueryHandler.handle(new ListProductsQuery(1, 10, null));

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result).containsExactlyInAnyOrder(ProductFixture.ramuneFraise, ProductFixture.ultraIceTea);
    }

    @Test
    @DisplayName("Should return empty list when repository is empty")
    void shouldReturnEmptyListWhenRepositoryIsEmpty() {
        // When
        Page<Product> result = listProductsQueryHandler.handle(new ListProductsQuery(1, 10, null));

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return only products in the specified category")
    void shouldReturnOnlyProductsInSpecifiedCategory() {
        // Given
        productRepository.save(ProductFixture.ramuneFraise);       // Japan
        productRepository.save(ProductFixture.ultraIceTea);        // Japan
        productRepository.save(ProductFixture.pockyChocolat);      // Snack

        // When
        Page<Product> resultJapan = listProductsQueryHandler.handle(
                new ListProductsQuery(1, 10, List.of("Japan"))
        );

        Page<Product> resultSnack = listProductsQueryHandler.handle(
                new ListProductsQuery(1, 10, List.of("Snack"))
        );

        // Then
        assertThat(resultJapan).hasSize(2);
        assertThat(resultJapan.getContent()).extracting(Product::getName)
                .containsExactlyInAnyOrder("Ramune Fraise 🍓", "Ultra Ice Tea Dragon Ball Super Végéta 🥬");

        assertThat(resultSnack).hasSize(1);
        assertThat(resultSnack.getContent().get(0).getName()).isEqualTo("Pocky Chocolat 🍫");
    }

    @Test
    @DisplayName("Should return empty list when filtering by non-existent category")
    void shouldReturnEmptyListWhenFilteringByNonExistentCategory() {
        // Given
        productRepository.save(ProductFixture.ramuneFraise);
        productRepository.save(ProductFixture.ultraIceTea);

        // When
        Page<Product> result = listProductsQueryHandler.handle(
                new ListProductsQuery(1, 10, List.of("NonExistent"))
        );

        // Then
        assertThat(result).isEmpty();
    }
}

package com.akazaki.api.e2e.common;

import com.akazaki.api.config.fixtures.ProductFixture;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.infrastructure.web.dto.response.ProductResponse;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("prod")
public class GetProductDetailE2ETest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ProductFixture productFixture;

    @BeforeEach
    public void setup() {
        productFixture.saveProducts();
    }

    @Test
    void getAProductDetailSuccessfully() {
        Product product = productFixture.drink;
        ProductResponse expectedProductResponse = createExpectedResponse(product);
        ProductResponse productResponse = getProductDetail(product.getId());

        assertThat(productResponse).isEqualTo(expectedProductResponse);
    }

    @Test
    void getProductDetailWithInvalidIdShouldReturn404() {
        webClient
                .get().uri("/api/products/999")
                .exchange()
                .expectStatus().isNotFound();
    }


    private ProductResponse getProductDetail(Long id) {
        return webClient
                .get().uri("/api/products/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private ProductResponse createExpectedResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                product.getCategories()
        );
    }
}

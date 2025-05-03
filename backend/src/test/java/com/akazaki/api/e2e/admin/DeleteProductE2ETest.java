package com.akazaki.api.e2e.admin;

import com.akazaki.api.config.fixtures.ProductFixture;
import com.akazaki.api.config.fixtures.UserFixture;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("prod")
@DisplayName("Delete Product E2E Tests")
public class DeleteProductE2ETest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserFixture userFixture;

    @Autowired
    private ProductFixture productFixture;

    @BeforeEach
    public void setup() {
        userFixture.saveUsers();
        productFixture.saveProducts();
    }

    @Test
    @DisplayName("Delete A Product Successfully")
    void deleteAProductSuccessfully() {
        String token = jwtService.generateToken(userFixture.adminUser);
        Product product = productFixture.drink;

        deleteProduct(product.getId(), token);

        Optional<Product> deletedProduct = productRepository.findById(product.getId());
        assertThat(deletedProduct).isNotPresent();
    }

    void deleteProduct(Long productId, String token) {
        webClient
                .delete().uri("/api/admin/products/" + productId)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isNoContent();
    }
}

package com.akazaki.api.e2e.admin;

import com.akazaki.api.config.fixtures.UserFixture;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.security.JwtService;
import com.akazaki.api.infrastructure.web.dto.request.CreateCategoryRequest;
import com.akazaki.api.infrastructure.web.dto.response.CategoryResponse;
import com.akazaki.api.infrastructure.web.dto.response.ProductResponse;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Optional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("prod")
public class TestCreateProductE2E {

    @Autowired
    private WebTestClient webClient;
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserFixture userFixture;

    @BeforeEach
    public void setup() {
        userFixture.saveUsers();
    }

    @Test
    void createAProductSuccessfully() {
        String token = jwtService.generateToken(userFixture.adminUser);
        Long categoryId = createCategory(token);
        Long productId = createProduct(categoryId, token);
        
        Optional<Product> savedProduct = productRepository.findById(productId);
        assertThat(savedProduct).isPresent();

        Product product = savedProduct.get();
        assertThat(product.getId()).isNotNull();
    }

    Long createCategory(String token) {
        CreateCategoryRequest categoryRequest = new CreateCategoryRequest();
        categoryRequest.setName("Test Category");

        return webClient
                .post().uri("/api/admin/categories")
                .header("Authorization", "Bearer " + token)
                .bodyValue(categoryRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .returnResult()
                .getResponseBody()
                .id();
    }

    Long createProduct(Long categoryId, String token) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("name", "Product Name");
        builder.part("description", "Product Description");
        builder.part("price", 100);
        builder.part("stock", 10);
        builder.part("file", new FileSystemResource("src/test/resources/images/test-image.png"));
        builder.part("categoryIds", categoryId.toString());

        return webClient
                .post().uri("/api/admin/products")
                .header("Authorization", "Bearer " + token) // Add JWT token to request
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .returnResult()
                .getResponseBody()
                .id();
    }
}

package com.akazaki.api.e2e.admin;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.web.dto.auth.request.LoginRequest;
import com.akazaki.api.infrastructure.web.dto.auth.request.RegisterUserRequest;
import com.akazaki.api.infrastructure.web.dto.auth.response.LoginResponse;
import com.akazaki.api.infrastructure.web.dto.auth.response.RegisterUserResponse;
import com.akazaki.api.infrastructure.web.dto.request.CreateCategoryRequest;
import com.akazaki.api.infrastructure.web.dto.response.CategoryResponse;
import com.akazaki.api.infrastructure.web.dto.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestDeleteProductE2E {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void deleteAProductSuccessfully() {
        registerAdminUser();
        String token = loginAdminUser();
        Long categoryId = createCategory(token);
        Long productId = createProduct(categoryId, token);


        deleteProduct(productId, token);

        Optional<Product> deletedProduct = productRepository.findById(productId);
        assertThat(deletedProduct).isNotPresent();
    }

    void registerAdminUser() {
        RegisterUserRequest registerRequest = new RegisterUserRequest();
        registerRequest.setEmail("admin@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFirstName("Admin");
        registerRequest.setLastName("User");
        registerRequest.setPhoneNumber("+33612345678");
        webClient
                .post().uri("/api/auth/register")
                .bodyValue(registerRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RegisterUserResponse.class);
    }

    String loginAdminUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@example.com");
        loginRequest.setPassword("password");
        return webClient
                .post().uri("/api/auth/login")
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .returnResult(LoginResponse.class)
                .getResponseBody()
                .blockFirst()
                .token();
    }

    Long createCategory(String token) {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setName("Electronics");
        return webClient
                .post().uri("/api/admin/categories")
                .header("Authorization", "Bearer " + token)
                .bodyValue(createCategoryRequest)
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

    void deleteProduct(Long productId, String token) {
        webClient
                .delete().uri("/api/admin/products/" + productId)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isNoContent();
    }



}
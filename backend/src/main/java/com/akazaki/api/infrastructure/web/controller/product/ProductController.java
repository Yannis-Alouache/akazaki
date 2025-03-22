package com.akazaki.api.infrastructure.web.controller.product;

import com.akazaki.api.application.queries.GetProduct.GetProductQueryHandler;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.queries.GetProductQuery;
import com.akazaki.api.infrastructure.web.dto.response.ProductResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product public routes")
public class ProductController {
    private final GetProductQueryHandler getProductQueryHandler;

    @Operation(
        summary = "Get product by ID",
        description = "Retrieves a product by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product found",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        GetProductQuery query = new GetProductQuery(id);
        Product product = getProductQueryHandler.handle(query);
        
        return ResponseEntity.ok(new ProductResponse(
            product.getId(), 
            product.getName(), 
            product.getDescription(), 
            product.getPrice(), 
            product.getStock(), 
            product.getImageUrl(), 
            product.getCategories()
        ));
    }
}
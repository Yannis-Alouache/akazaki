package com.akazaki.api.infrastructure.web.controller.product;

import com.akazaki.api.application.queries.GetProduct.GetProductQueryHandler;
import com.akazaki.api.application.queries.ListProducts.ListProductsQueryHandler;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.queries.GetProductQuery;
import com.akazaki.api.domain.ports.in.queries.ListProductsQuery;
import com.akazaki.api.infrastructure.web.dto.request.ProductListRequest;
import com.akazaki.api.infrastructure.web.dto.response.ErrorResponse;
import com.akazaki.api.infrastructure.web.dto.response.ProductListResponse;
import com.akazaki.api.infrastructure.web.dto.response.ProductResponse;
import com.akazaki.api.infrastructure.web.mapper.product.ProductResponseMapper;
import com.akazaki.api.infrastructure.web.mapper.productList.ProductListMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {
    private final GetProductQueryHandler getProductQueryHandler;
    private final ListProductsQueryHandler listProductsQueryHandler;
    private final ProductResponseMapper productMapper;
    private final ProductListMapper productListMapper;

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
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        GetProductQuery query = new GetProductQuery(id);
        Product product = getProductQueryHandler.handle(query);
        
        return ResponseEntity.ok(productMapper.toResponse(product));
    }

    @Operation(
        summary = "List products with pagination",
        description = "Retrieves a paginated list of products"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Products found",
            content = @Content(schema = @Schema(implementation = ProductListResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<ProductListResponse> listProducts(@Valid ProductListRequest listProductsRequest) {
        ListProductsQuery query = new ListProductsQuery(listProductsRequest.getPage(), listProductsRequest.getSize());
        Page<Product> products = listProductsQueryHandler.handle(query);
        return ResponseEntity.ok(productListMapper.toResponse(products));
    }
}
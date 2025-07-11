package com.akazaki.api.infrastructure.web.controller.admin;

import com.akazaki.api.application.commands.CreateProduct.CreateProductCommandHandler;
import com.akazaki.api.application.commands.DeleteProduct.DeleteProductCommandHandler;
import com.akazaki.api.application.commands.StoreImage.StoreImageCommandHandler;
import com.akazaki.api.application.commands.UpdateProduct.UpdateProductCommandHandler;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.CreateProductCommand;
import com.akazaki.api.domain.ports.in.commands.DeleteProductCommand;
import com.akazaki.api.domain.ports.in.commands.StoreImageCommand;
import com.akazaki.api.domain.ports.in.commands.UpdateProductCommand;
import com.akazaki.api.infrastructure.exceptions.UnableToSaveFileException;
import com.akazaki.api.infrastructure.web.dto.request.CreateProductRequest;
import com.akazaki.api.infrastructure.web.dto.request.UpdateProductRequest;
import com.akazaki.api.infrastructure.web.dto.response.ProductResponse;

import com.akazaki.api.infrastructure.web.mapper.product.ProductResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product administration routes")
public class AdminProductController {

    private static final Logger logger = LoggerFactory.getLogger(AdminProductController.class);
    private final CreateProductCommandHandler createProductCommandHandler;
    private final StoreImageCommandHandler storeImageCommandHandler;
    private final DeleteProductCommandHandler deleteProductCommandHandler;
    private final UpdateProductCommandHandler updateProductCommandHandler;
    private final ProductResponseMapper productMapper;

    @Operation(
        summary = "Create a new product",
        description = "Creates a new product with the provided information including an image file"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product successfully created",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content
        )
    })
    @PostMapping
    @Transactional
    public ResponseEntity<ProductResponse> createProduct(@Valid @ModelAttribute CreateProductRequest request) {
        logger.info("Received create product request for: {}", request.toString());

        CreateProductCommand productCommand = new CreateProductCommand(
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getStock(),
            request.getCategoryIds()
        );
        Product product = createProductCommandHandler.handle(productCommand);

        MultipartFile imageFile = request.getFile();
        StoreImageCommand imageCommand = null;
        String imageUrl = null;

        try {
            imageCommand = new StoreImageCommand(
                product.getId(),
                imageFile.getOriginalFilename(),
                imageFile.getContentType(),
                imageFile.getInputStream()
            );
            imageUrl = storeImageCommandHandler.handle(imageCommand);
        } catch (IOException e) {
            throw new UnableToSaveFileException();
        }

        product.setImageUrl(imageUrl);

        return ResponseEntity.ok(productMapper.toResponse(product));
    }

    @Operation(
        summary = "Delete a product",
        description = "Deletes a product by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Product successfully deleted"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Product not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid product ID",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        DeleteProductCommand deleteProductCommand = new DeleteProductCommand(id);
        deleteProductCommandHandler.handle(deleteProductCommand);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProductById(
            @PathVariable Long id,
            @ModelAttribute UpdateProductRequest request
    ) {
        logger.info("Received update product request for ID: {}", id);

        UpdateProductCommand command = new UpdateProductCommand(
                id,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getCategoryIds(),
                request.getFile()
        );
        updateProductCommandHandler.handle(command);
        return ResponseEntity.noContent().build();
    }

}
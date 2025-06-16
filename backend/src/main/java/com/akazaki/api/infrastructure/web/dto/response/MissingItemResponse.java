package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Missing item response information")
public record MissingItemResponse(
    @Schema(description = "Product ID", example = "123")
    Long productId,

    @Schema(description = "Product name", example = "Product Name")
    String productName,

    @Schema(description = "Requested quantity", example = "10")
    int requestedQuantity,

    @Schema(description = "Available quantity", example = "5")
    int availableQuantity
) {}
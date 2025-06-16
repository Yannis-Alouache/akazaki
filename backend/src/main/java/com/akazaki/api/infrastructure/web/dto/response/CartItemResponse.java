package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cart item response information")
public record CartItemResponse(
    @Schema(description = "Cart item ID", example = "789")
    Long id,
    
    @Schema(description = "Quantity of the product in cart", example = "3")
    int quantity,
    
    @Schema(description = "Product information")
    ProductResponse product
) {}

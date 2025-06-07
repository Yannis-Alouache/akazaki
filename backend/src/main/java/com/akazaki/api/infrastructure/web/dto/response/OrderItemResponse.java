package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Order item response information")
public record OrderItemResponse(
    @Schema(description = "Order item ID", example = "789")
    Long id,
    
    @Schema(description = "Quantity of the product", example = "2")
    int quantity,
    
    @Schema(description = "Price per unit", example = "4.80")
    double price,
    
    @Schema(description = "Product information")
    ProductResponse product
) {}

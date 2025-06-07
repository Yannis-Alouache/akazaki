package com.akazaki.api.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request to add a product to cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    
    @Schema(description = "Product ID to add to cart", example = "123")
    @NotBlank(message = "Product ID is required")
    private Long productId;
}

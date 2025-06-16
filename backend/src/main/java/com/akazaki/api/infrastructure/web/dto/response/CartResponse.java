package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Shopping cart response information")
public record CartResponse(
        @Schema(description = "Cart ID", example = "456")
        Long id,
        
        @Schema(description = "User ID who owns the cart", example = "123")
        Long userId,
        
        @Schema(description = "List of cart items")
        List<CartItemResponse> cartItems
) {}

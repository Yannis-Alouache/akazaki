package com.akazaki.api.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to update cart item quantity")
public record UpdateCartItemQuantityRequest(
    @Schema(description = "New quantity for the cart item", example = "3")
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be at least 0")
    int quantity
) {} 
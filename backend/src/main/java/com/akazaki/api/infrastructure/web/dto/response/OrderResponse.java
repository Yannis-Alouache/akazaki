package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Order response information")
public record OrderResponse(
    @Schema(description = "Order ID", example = "123")
    Long id,
    
    @Schema(description = "User ID who placed the order", example = "456")
    Long userId,
    
    @Schema(description = "Order creation date", example = "2024-01-15T10:30:00")
    LocalDateTime date,
    
    @Schema(description = "Order status")
    OrderStatusResponse status,
    
    @Schema(description = "List of order items")
    List<OrderItemResponse> items,
    
    @Schema(description = "Total price of the order", example = "29.99")
    double totalPrice,
    
    @Schema(description = "Billing address")
    AddressResponse billingAddress,
    
    @Schema(description = "Shipping address")
    AddressResponse shippingAddress
) {}

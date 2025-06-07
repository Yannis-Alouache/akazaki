package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Order status enumeration")
public enum OrderStatusResponse {
    @Schema(description = "Order is pending payment")
    PENDING, 
    
    @Schema(description = "Order has been paid")
    PAID, 
    
    @Schema(description = "Order has been shipped")
    SHIPPED, 
    
    @Schema(description = "Order has been delivered")
    DELIVERED, 
    
    @Schema(description = "Order has been cancelled")
    CANCELLED;
}
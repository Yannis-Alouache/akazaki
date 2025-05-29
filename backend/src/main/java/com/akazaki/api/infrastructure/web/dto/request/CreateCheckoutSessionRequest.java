package com.akazaki.api.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCheckoutSessionRequest(
    @NotBlank(message = "Order ID is required")
    Long orderId
) {}
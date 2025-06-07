package com.akazaki.api.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to create a checkout session")
public record CreateCheckoutSessionRequest(
    @Schema(description = "Order ID", example = "123")
    @NotBlank(message = "Order ID is required")
    Long orderId
) {}
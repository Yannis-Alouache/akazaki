package com.akazaki.api.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request for create payment intent")
public record CreatePaymentIntentRequest(
    @Schema(description = "Order id")
    @NotBlank(message = "Order id is required")
    Long orderId
) {}

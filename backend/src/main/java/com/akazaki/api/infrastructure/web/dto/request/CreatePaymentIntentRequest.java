package com.akazaki.api.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request for create payment intent")
public record CreatePaymentIntentRequest(
    @Schema(description = "Order id")
    @NotNull(message = "orderId is a required field")
    Long orderId
) {}

package com.akazaki.api.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request for create payment intent")
public record CreatePaymentIntentRequest(
    @Schema(description = "Order id")
    Long orderId
) {}

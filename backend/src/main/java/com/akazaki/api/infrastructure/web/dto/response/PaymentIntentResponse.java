package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for payment intent")
public record PaymentIntentResponse(
    @Schema(description = "Client secret")
    String clientSecret
) {}

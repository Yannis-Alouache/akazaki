package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Available payment methods")
public enum PaymentMethodResponse {
    @Schema(description = "Credit card payment")
    CREDIT_CARD,
    
    @Schema(description = "PayPal payment")
    PAYPAL,
    
    @Schema(description = "Other payment methods")
    OTHER
}

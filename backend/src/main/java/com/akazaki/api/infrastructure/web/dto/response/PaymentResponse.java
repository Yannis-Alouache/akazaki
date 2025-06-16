package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Payment response information")
public record PaymentResponse(
    @Schema(description = "Payment ID", example = "789")
    Long id,
    
    @Schema(description = "Payment amount", example = "29.99")
    double amount,
    
    @Schema(description = "Payment method used")
    PaymentMethodResponse method,
    
    @Schema(description = "Payment date", example = "2024-01-15T10:30:00")
    LocalDateTime date
) {}

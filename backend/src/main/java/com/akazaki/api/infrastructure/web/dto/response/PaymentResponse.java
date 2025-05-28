package com.akazaki.api.infrastructure.web.dto.response;

import java.time.LocalDateTime;

public record PaymentResponse(
    Long id,
    int amount,
    String method,
    LocalDateTime date
) {}

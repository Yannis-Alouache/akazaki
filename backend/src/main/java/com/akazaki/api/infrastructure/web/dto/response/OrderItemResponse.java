package com.akazaki.api.infrastructure.web.dto.response;

public record OrderItemResponse(
    Long id,
    int quantity,
    double price,
    ProductResponse product
) {}

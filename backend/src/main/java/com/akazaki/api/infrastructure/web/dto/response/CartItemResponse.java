package com.akazaki.api.infrastructure.web.dto.response;

public record CartItemResponse(
    Long id,
    int quantity,
    ProductResponse product
) {}

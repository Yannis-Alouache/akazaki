package com.akazaki.api.infrastructure.web.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long id,
    Long userId,
    LocalDateTime date,
    OrderStatusResponse status,
    List<OrderItemResponse> items,
    double totalPrice,
    AddressResponse billingAddress,
    AddressResponse shippingAddress
) {}

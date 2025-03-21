package com.akazaki.api.domain.ports.in.commands;

import java.time.LocalDateTime;

public record CreateOrderCommand(
        String status,
        LocalDateTime date,
        int totalPrice,
        String billingAddress,
        String shippingAddress
) {}

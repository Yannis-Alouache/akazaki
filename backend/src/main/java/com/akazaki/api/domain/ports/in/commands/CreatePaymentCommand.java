package com.akazaki.api.domain.ports.in.commands;

public record CreatePaymentCommand(
    Long orderId,
    String paymentMethodId
) {}

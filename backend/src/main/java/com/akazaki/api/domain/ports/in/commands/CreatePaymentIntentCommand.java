package com.akazaki.api.domain.ports.in.commands;

public record CreatePaymentIntentCommand(Long userId, Long orderId) {}

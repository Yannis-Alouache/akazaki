package com.akazaki.api.domain.ports.in.commands;

public record UpdateCartItemQuantityCommand(
    Long userId,
    Long productId,
    int quantity
) {} 
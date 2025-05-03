package com.akazaki.api.domain.ports.in.commands;

public record AddToCartCommand(
    Long userId,
    Long productId
) {}

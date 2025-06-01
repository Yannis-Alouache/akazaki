package com.akazaki.api.domain.ports.in.commands;

import com.akazaki.api.domain.model.Address;

public record UpdateOrderAddressesCommand(
    Long orderId,
    Address billingAddress,
    Address shippingAddress,
    Long userId
) {} 
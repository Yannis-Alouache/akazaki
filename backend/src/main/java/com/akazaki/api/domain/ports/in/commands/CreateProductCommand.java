package com.akazaki.api.domain.ports.in.commands;

import java.util.List;

public record CreateProductCommand(
        String name,
        String description,
        double price,
        int stock,
        List<Long> categoryIds
        //Long promotionId
) {}

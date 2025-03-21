package com.akazaki.api.infrastructure.web.dto.response;

import java.util.List;

import com.akazaki.api.domain.model.Category;

public record ProductResponse (
        Long id,
        String name,
        String description,
        double price,
        int stock,
        String imageUrl,
        List<Category> categories
) {}

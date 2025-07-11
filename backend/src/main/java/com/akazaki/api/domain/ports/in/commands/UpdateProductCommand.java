package com.akazaki.api.domain.ports.in.commands;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdateProductCommand (
        Long productId,
        String name,
        String description,
        double price,
        int stock,
        List<Long> categoryIds,
        MultipartFile imageFile
) {}

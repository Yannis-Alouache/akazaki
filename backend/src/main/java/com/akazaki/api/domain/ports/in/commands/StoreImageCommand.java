package com.akazaki.api.domain.ports.in.commands;

import java.io.InputStream;

public record StoreImageCommand(
        Long productId,
        String fileName,
        String contentType,
        InputStream imageStream
) {}

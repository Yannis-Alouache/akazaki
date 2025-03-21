package com.akazaki.api.domain.model;

import java.io.InputStream;

public record Image(
        String fileName,
        InputStream imageStream
) {}

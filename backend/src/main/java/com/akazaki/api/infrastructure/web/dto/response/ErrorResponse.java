package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        @Schema(description = "The reason of the error", example = "Product Not Found")
        String reason,
        @Schema(description = "Status message", example = "NOT_FOUND")
        HttpStatus status
) {}

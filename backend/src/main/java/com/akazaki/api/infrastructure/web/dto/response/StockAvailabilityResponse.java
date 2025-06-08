package com.akazaki.api.infrastructure.web.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stock availability response information")
public record StockAvailabilityResponse(
    @Schema(description = "Order ID", example = "123")
    Long orderId,

    @Schema(description = "Stock availability status", example = "true")
    boolean isStockAvailable,

    @Schema(description = "List of missing items", implementation = MissingItemResponse.class)
    List<MissingItemResponse> missingItems
) {}
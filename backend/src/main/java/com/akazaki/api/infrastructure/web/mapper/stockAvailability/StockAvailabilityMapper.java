package com.akazaki.api.infrastructure.web.mapper.stockAvailability;

import com.akazaki.api.domain.model.MissingItem;
import com.akazaki.api.domain.model.StockAvailability;
import com.akazaki.api.infrastructure.web.dto.response.MissingItemResponse;
import com.akazaki.api.infrastructure.web.dto.response.StockAvailabilityResponse;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockAvailabilityMapper {

    public StockAvailabilityResponse toResponse(StockAvailability stockAvailability) {
        List<MissingItemResponse> missingItemResponses = stockAvailability.getMissingItems()
                .stream()
                .map(this::toMissingItemResponse)
                .collect(Collectors.toList());

        return new StockAvailabilityResponse(
                stockAvailability.getOrderId(),
                stockAvailability.isStockAvailable(),
                missingItemResponses
        );
    }

    private MissingItemResponse toMissingItemResponse(MissingItem missingItem) {
        return new MissingItemResponse(
                missingItem.getProductId(),
                missingItem.getProductName(),
                missingItem.getRequestedQuantity(),
                missingItem.getAvailableQuantity()
        );
    }
} 
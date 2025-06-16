package com.akazaki.api.domain.model;

import java.util.List;

public class StockAvailability {
    private final Long orderId;
    private final boolean isStockAvailable;
    private final List<MissingItem> missingItems;

    private StockAvailability(Long orderId, boolean isStockAvailable, List<MissingItem> missingItems) {
        this.orderId = orderId;
        this.isStockAvailable = isStockAvailable;
        this.missingItems = missingItems;
    }

    public static StockAvailability available(Long orderId) {
        return new StockAvailability(orderId, true, List.of());
    }

    public static StockAvailability unavailable(Long orderId, List<MissingItem> missingItems) {
        return new StockAvailability(orderId, false, missingItems);
    }

    public Long getOrderId() {return orderId;}
    public boolean isStockAvailable() {return isStockAvailable;}
    public List<MissingItem> getMissingItems() {return missingItems;}

    @Override
    public String toString() {
        return "StockAvailability{" +
                "orderId=" + orderId +
                ", isStockAvailable=" + isStockAvailable +
                ", missingItems=" + missingItems +
                '}';
    }
} 
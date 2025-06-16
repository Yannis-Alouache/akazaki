package com.akazaki.api.domain.model;

public class MissingItem {
    private final Long productId;
    private final String productName;
    private final int requestedQuantity;
    private final int availableQuantity;

    private MissingItem(Long productId, String productName, int requestedQuantity, int availableQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public static MissingItem create(Long productId, String productName, int requestedQuantity, int availableQuantity) {
        return new MissingItem(productId, productName, requestedQuantity, availableQuantity);
    }

    public Long getProductId() {return productId;}
    public String getProductName() {return productName;}
    public int getRequestedQuantity() {return requestedQuantity;}
    public int getAvailableQuantity() {return availableQuantity;}

    @Override
    public String toString() {
        return "MissingItem{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", requestedQuantity=" + requestedQuantity +
                ", availableQuantity=" + availableQuantity +
                '}';
    }
} 
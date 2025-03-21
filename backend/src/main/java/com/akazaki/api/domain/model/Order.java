package com.akazaki.api.domain.model;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private LocalDateTime date;
    private String status;
    private int totalPrice;
    private String billingAddress;
    private String shippingAddress;

    public Order(LocalDateTime date, String status, int totalPrice, String billingAddress, String shippingAddress) {
        this.date = date;
        this.status = status;
        this.totalPrice = totalPrice;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }




}

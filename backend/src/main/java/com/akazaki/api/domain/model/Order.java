package com.akazaki.api.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private User user;
    private LocalDateTime date;
    private OrderStatus status;
    private List<OrderItem> items;
    private double totalPrice;
    private Address billingAddress;
    private Address shippingAddress;

    private Order(
        Long id,
        User user,
        LocalDateTime date,
        OrderStatus status,
        List<OrderItem> items,
        double totalPrice,
        Address billingAddress,
        Address shippingAddress
    ) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.status = status;
        this.items = items;
        this.totalPrice = totalPrice;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }

    public static Order draft(
        User user,
        LocalDateTime date,
        OrderStatus status,
        List<OrderItem> items,
        double totalPrice
    ) {
        return new Order(null, user, date, status, items, totalPrice, null, null);
    }

    public void markAsPaid() {
        this.status = OrderStatus.PAID;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                    "id=" + id +
                    ", user=" + user +
                    ", date=" + date +
                    ", status=" + status +
                    ", items=" + items +
                    ", totalPrice=" + totalPrice +
                    ", billingAddress=" + billingAddress +
                    ", shippingAddress=" + shippingAddress +
                '}';
    }

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public LocalDateTime getDate() { return date; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public Address getBillingAddress() { return billingAddress; }
    public Address getShippingAddress() { return shippingAddress; }

    // Setters
    public void setId(Long id) { this.id = id; }

    public static class Builder {
        private Long id;
        private User user;
        private LocalDateTime date;
        private OrderStatus status;
        private List<OrderItem> items;
        private double totalPrice;
        private Address billingAddress;
        private Address shippingAddress;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder totalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder items(List<OrderItem> items) {
            this.items = items;
            return this;
        }

        public Builder billingAddress(Address billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public Builder shippingAddress(Address shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Order build() {
            return new Order(id, user, date, status, items, totalPrice, billingAddress, shippingAddress);
        }
    }

    public double calculateTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public static Builder builder() {
        return new Builder();
    }
}
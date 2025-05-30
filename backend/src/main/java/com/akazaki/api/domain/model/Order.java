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
    private Payment payment;

    private Order(
        Long id,
        User user,
        LocalDateTime date,
        OrderStatus status,
        List<OrderItem> items,
        double totalPrice,
        Address billingAddress,
        Address shippingAddress,
        Payment payment
    ) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.status = status;
        this.items = items;
        this.totalPrice = totalPrice;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.payment = payment;
    }

    public static Order draft(
        User user,
        LocalDateTime date,
        OrderStatus status,
        List<OrderItem> items,
        double totalPrice
    ) {
        return new Order(null, user, date, status, items, totalPrice, null, null, null);
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
                    ", payment=" + payment +
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
    public Payment getPayment() { return payment; }

    public static class Builder {
        private Long id;
        private User user;
        private LocalDateTime date;
        private OrderStatus status;
        private List<OrderItem> items;
        private double totalPrice;
        private Address billingAddress;
        private Address shippingAddress;
        private Payment payment;

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

        public Order build() {
            return new Order(id, user, date, status, items, totalPrice, billingAddress, shippingAddress, payment);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
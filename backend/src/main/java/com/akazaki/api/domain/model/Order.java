package com.akazaki.api.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private LocalDateTime date;
    private OrderStatus status;
    private List<OrderItem> items;
    private int totalPrice;
    private Address billingAddress;
    private Address shippingAddress;
    private Payment payment;

    private Order(
        Long id,
        LocalDateTime date,
        OrderStatus status,
        List<OrderItem> items,
        int totalPrice,
        Address billingAddress,
        Address shippingAddress,
        Payment payment
    ) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.items = items;
        this.totalPrice = totalPrice;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.payment = payment;
    }

    public static Order create(
        LocalDateTime date,
        OrderStatus status,
        List<OrderItem> items,
        int totalPrice,
        Address billingAddress,
        Address shippingAddress,
        Payment payment
    ) {
        return new Order(null, date, status, items, totalPrice, billingAddress, shippingAddress, payment);
    }
    
    @Override
    public String toString() {
        return "Order{" +
                    "id=" + id +
                    ", date=" + date +
                    ", status=" + status +
                    ", items=" + items +
                    ", totalPrice=" + totalPrice +
                    ", billingAddress=" + billingAddress +
                    ", shippingAddress=" + shippingAddress +
                    ", payment=" + payment +
                '}';
    }

}
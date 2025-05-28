package com.akazaki.api.domain.model;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItem {
    private Long id;
    private int quantity;
    private double price;
    private Product product;


    private OrderItem(
        Long id,
        int quantity,
        double price,
        Product product
    ) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
    }

    public static OrderItem create(
        int quantity,
        double price,
        Product product
    ) {
        return new OrderItem(null, quantity, price, product);
    }

    public static OrderItem restore(
        Long id,
        int quantity,
        double price,
        Product product
    ) {
        return new OrderItem(id, quantity, price, product);
    }

    public static List<OrderItem> fromCartItems(List<CartItem> cartItems) {
        return cartItems.stream()
            .map(cartItem -> OrderItem.create(cartItem.getQuantity(), cartItem.getProduct().getPrice(), cartItem.getProduct()))
            .collect(Collectors.toList());
    }

    // Getters
    public Long getId() { return id; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public Product getProduct() { return product; }

    @Override
    public String toString() {
        return "OrderItem{" +
                    "id=" + id +
                    ", quantity=" + quantity +
                    ", price=" + price +
                    ", product=" + product +
                '}';
    }
}
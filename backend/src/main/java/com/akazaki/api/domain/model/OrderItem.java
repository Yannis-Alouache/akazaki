package com.akazaki.api.domain.model;

public class OrderItem {
    private Long id;
    private int quantity;
    private int price;
    private Order order;
    private Product product;


    private OrderItem(
        Long id,
        int quantity,
        int price,
        Order order,
        Product product
    ) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.product = product;
    }

    public static OrderItem create(
        int quantity,
        int price,
        Order order,
        Product product
    ) {
        return new OrderItem(null, quantity, price, order, product);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                    "id=" + id +
                    ", quantity=" + quantity +
                    ", price=" + price +
                    ", order=" + order +
                    ", product=" + product +
                '}';
    }
    
}
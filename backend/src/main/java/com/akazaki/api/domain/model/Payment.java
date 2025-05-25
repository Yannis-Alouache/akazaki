package com.akazaki.api.domain.model;

import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private Order order;
    private int amount;
    private String method;
    private LocalDateTime date;

    private Payment(
        Long id,
        Order order,
        int amount,
        String method,
        LocalDateTime date
    ) {
        this.id = id;
        this.order = order;
        this.amount = amount;
        this.method = method;
        this.date = date;
    }

    public static Payment create(
        Order order,
        int amount,
        String method,
        LocalDateTime date
    ) {
        return new Payment(null, order, amount, method, date);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", order=" + order +
                ", amount=" + amount +
                ", method=" + method +
                ", date=" + date +
                '}';
    }
    
}
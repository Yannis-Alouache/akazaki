package com.akazaki.api.domain.model;

import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private Order order;
    private double amount;
    private PaymentMethodEnum method;
    private LocalDateTime date;

    private Payment(
        Long id,
        Order order,
        double amount,
        PaymentMethodEnum method,
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
        double amount,
        PaymentMethodEnum method,
        LocalDateTime date
    ) {
        return new Payment(null, order, amount, method, date);
    }

    // Getters
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public double getAmount() { return amount; }
    public PaymentMethodEnum getMethod() { return method; }
    public LocalDateTime getDate() { return date; }

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

    public static class Builder {
        private Long id;
        private Order order;
        private double amount;
        private PaymentMethodEnum method;
        private LocalDateTime date;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder order(Order order) {
            this.order = order;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder method(PaymentMethodEnum method) {
            this.method = method;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Payment build() {
            return new Payment(id, order, amount, method, date);
        }
    }

    public static Builder builder() {
        return new Builder();
    }    
}
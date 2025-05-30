package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Order;

public interface PaymentGateway {
    String createPaymentIntent(Order order);
    boolean confirmPayment(String paymentIntentId);
}
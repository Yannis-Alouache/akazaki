package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.PaymentMethodEnum;

public interface PaymentGateway {
    String createPaymentIntent(Order order);
    PaymentMethodEnum getPaymentMethod(String paymentMethodId);
}
package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}

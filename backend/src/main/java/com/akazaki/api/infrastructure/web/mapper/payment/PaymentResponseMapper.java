package com.akazaki.api.infrastructure.web.mapper.payment;

import com.akazaki.api.domain.model.Payment;
import com.akazaki.api.infrastructure.web.dto.response.PaymentResponse;
import com.akazaki.api.infrastructure.web.mapper.paymentMethod.PaymentMethodResponseMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentResponseMapper {

    @Autowired
    private PaymentMethodResponseMapper paymentMethodResponseMapper;

    public PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }
        return new PaymentResponse(
            payment.getId(),
            payment.getAmount(),
            paymentMethodResponseMapper.toResponse(payment.getMethod()),
            payment.getDate()
        );
    }

}

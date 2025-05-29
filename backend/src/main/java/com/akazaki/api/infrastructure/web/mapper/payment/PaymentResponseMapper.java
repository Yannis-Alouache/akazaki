package com.akazaki.api.infrastructure.web.mapper.payment;

import com.akazaki.api.domain.model.Payment;
import com.akazaki.api.infrastructure.web.dto.response.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentResponseMapper {

    public PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }
        return new PaymentResponse(
            payment.getId(),
            payment.getAmount(),
            payment.getMethod(),
            payment.getDate()
        );
    }

}

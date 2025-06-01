package com.akazaki.api.infrastructure.web.mapper.paymentMethod;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.PaymentMethodEnum;
import com.akazaki.api.infrastructure.web.dto.response.PaymentMethodResponse;

@Component
public class PaymentMethodResponseMapper {
    public PaymentMethodResponse toResponse(PaymentMethodEnum paymentMethod) {
        return switch (paymentMethod) {
            case CREDIT_CARD -> PaymentMethodResponse.CREDIT_CARD;
            case PAYPAL -> PaymentMethodResponse.PAYPAL;
            case OTHER -> PaymentMethodResponse.OTHER;
        };
    }
}

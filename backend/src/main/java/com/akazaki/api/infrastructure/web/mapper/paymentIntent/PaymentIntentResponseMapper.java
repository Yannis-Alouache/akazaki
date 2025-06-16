package com.akazaki.api.infrastructure.web.mapper.paymentIntent;

import org.springframework.stereotype.Component;

import com.akazaki.api.infrastructure.web.dto.response.PaymentIntentResponse;


@Component
public class PaymentIntentResponseMapper {
    public PaymentIntentResponse toResponse(String clientSecret) {
        return new PaymentIntentResponse(clientSecret);
    }
}

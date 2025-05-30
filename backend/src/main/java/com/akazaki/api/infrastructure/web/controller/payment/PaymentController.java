package com.akazaki.api.infrastructure.web.controller.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akazaki.api.application.commands.CreatePaymentIntentCommandHandler;
import com.akazaki.api.domain.ports.in.commands.CreatePaymentIntentCommand;
import com.akazaki.api.infrastructure.persistence.User.UserEntity;
import com.akazaki.api.infrastructure.web.dto.request.CreatePaymentIntentRequest;
import com.akazaki.api.infrastructure.web.dto.request.PaymentIntentResponse;
import com.akazaki.api.infrastructure.web.mapper.paymentIntent.PaymentIntentResponseMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final CreatePaymentIntentCommandHandler createPaymentIntentCommandHandler;
    private final PaymentIntentResponseMapper paymentIntentResponseMapper;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentIntentResponse> createPaymentIntent(@Valid @RequestBody CreatePaymentIntentRequest body) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Long userId = userEntity.getId();

        String clientSecret = createPaymentIntentCommandHandler.handle(
            new CreatePaymentIntentCommand(userId, body.orderId())
        );

        return ResponseEntity.ok(paymentIntentResponseMapper.toResponse(clientSecret));
    }
}
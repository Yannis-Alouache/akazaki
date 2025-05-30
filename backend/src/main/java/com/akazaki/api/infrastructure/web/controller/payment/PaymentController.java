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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment management APIs")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final CreatePaymentIntentCommandHandler createPaymentIntentCommandHandler;
    private final PaymentIntentResponseMapper paymentIntentResponseMapper;

    @Operation(
        summary = "Create payment intent",
        description = "Creates a payment intent for a specific order"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment intent created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
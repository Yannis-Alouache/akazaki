package com.akazaki.api.infrastructure.web.controller.stripeCheckoutSession;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akazaki.api.application.commands.CreateCheckoutSession.CreateCheckoutSessionCommandHandler;
import com.akazaki.api.domain.ports.in.commands.CreateCheckoutSessionCommand;
import com.akazaki.api.infrastructure.persistence.User.UserEntity;
import com.akazaki.api.infrastructure.web.dto.request.CreateCheckoutSessionRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Stripe Checkout Session", description = "Stripe checkout session APIs")
public class StripeCheckoutSessionController {

    private final CreateCheckoutSessionCommandHandler createCheckoutSessionCommandHandler;
    @PostMapping("/create-stripe-checkout-session")
    @Operation(summary = "Create a Stripe checkout session", description = "Create a Stripe checkout session")
    @ApiResponse(responseCode = "200", description = "Stripe checkout session created successfully")
    public ResponseEntity<String> createStripeCheckoutSession(@Valid @RequestBody CreateCheckoutSessionRequest body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Long userId = userEntity.getId();

        String clientSecret = createCheckoutSessionCommandHandler.handle(
            new CreateCheckoutSessionCommand(userId, body.orderId())
        );

        return ResponseEntity.ok(clientSecret);
    }
}

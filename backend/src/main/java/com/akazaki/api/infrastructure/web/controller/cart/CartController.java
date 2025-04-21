package com.akazaki.api.infrastructure.web.controller.cart;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akazaki.api.application.commands.AddToCart.AddToCartCommandHandler;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.infrastructure.web.mapper.cart.CartResponseMapper;
import com.akazaki.api.domain.ports.in.commands.AddToCartCommand;
import com.akazaki.api.infrastructure.persistence.User.UserEntity;
import com.akazaki.api.infrastructure.web.dto.response.CartResponse;
import com.akazaki.api.infrastructure.web.dto.response.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart management APIs")
public class CartController {
    private final CartResponseMapper cartMapper;
    private final AddToCartCommandHandler addToCartCommandHandler;

    @Operation(
        summary = "Adds a product to the cart",
        description = "Adds a product to the cart"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Added to cart",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "200",
            description = "Product out of stock",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/add/{productId}")
    public ResponseEntity<CartResponse> addToCart(@PathVariable("productId") Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Long userId = userEntity.getId();
        
        Cart cart = addToCartCommandHandler.handle(
            new AddToCartCommand(userId, productId)
        );

        CartResponse cartResponse = cartMapper.toResponse(cart);
        return ResponseEntity.ok(cartResponse);
    }
}

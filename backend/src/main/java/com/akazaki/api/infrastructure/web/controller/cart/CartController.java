package com.akazaki.api.infrastructure.web.controller.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.akazaki.api.application.commands.AddToCart.AddToCartCommandHandler;
import com.akazaki.api.application.queries.GetCart.GetCartQueryHandler;
import com.akazaki.api.application.commands.UpdateCartItemQuantityCommandHandler;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.ports.in.commands.AddToCartCommand;
import com.akazaki.api.domain.ports.in.commands.UpdateCartItemQuantityCommand;
import com.akazaki.api.domain.ports.in.queries.GetCartQuery;
import com.akazaki.api.infrastructure.persistence.User.UserEntity;
import com.akazaki.api.infrastructure.web.dto.request.UpdateCartItemQuantityRequest;
import com.akazaki.api.infrastructure.web.dto.response.CartResponse;
import com.akazaki.api.infrastructure.web.dto.response.ErrorResponse;
import com.akazaki.api.infrastructure.web.mapper.cart.CartResponseMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart management APIs")
public class CartController {
    private final CartResponseMapper cartMapper;
    private final AddToCartCommandHandler addToCartCommandHandler;
    private final UpdateCartItemQuantityCommandHandler updateCartItemQuantityCommandHandler;
    private final GetCartQueryHandler getCartQueryHandler;

    @Operation(
        summary = "Get user's cart",
        description = "Retrieves the current user's cart"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cart retrieved successfully",
            content = @Content(schema = @Schema(implementation = CartResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Long userId = userEntity.getId();

        Cart cart = getCartQueryHandler.handle(new GetCartQuery(userId));
        CartResponse cartResponse = cartMapper.toResponse(cart);
        return ResponseEntity.ok(cartResponse);
    }

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

    @Operation(
        summary = "Updates the quantity of a product in the cart",
        description = "Updates the quantity of a product in the cart"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Quantity updated",
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
            responseCode = "400",
            description = "Insufficient stock or invalid quantity",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateCartItemQuantity(
            @PathVariable("productId") Long productId,
            @Valid @ModelAttribute UpdateCartItemQuantityRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Long userId = userEntity.getId();

        Cart cart = updateCartItemQuantityCommandHandler.handle(
            new UpdateCartItemQuantityCommand(userId, productId, request.quantity())
        );

        CartResponse cartResponse = cartMapper.toResponse(cart);
        return ResponseEntity.ok(cartResponse);
    }
}

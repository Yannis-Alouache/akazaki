package com.akazaki.api.infrastructure.web.controller.order;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akazaki.api.application.commands.CreateOrder.CreateOrderCommandHandler;
import com.akazaki.api.application.commands.UpdateOrderAddressesCommandHandler;
import com.akazaki.api.application.queries.CheckStockAvailability.CheckStockAvailabilityQueryHandler;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.StockAvailability;
import com.akazaki.api.domain.ports.in.commands.CreateOrderCommand;
import com.akazaki.api.domain.ports.in.commands.UpdateOrderAddressesCommand;
import com.akazaki.api.domain.ports.in.queries.CheckStockAvailabilityQuery;
import com.akazaki.api.infrastructure.persistence.User.UserEntity;
import com.akazaki.api.infrastructure.web.dto.request.UpdateOrderAddressesRequest;
import com.akazaki.api.infrastructure.web.dto.response.OrderResponse;
import com.akazaki.api.infrastructure.web.dto.response.StockAvailabilityResponse;
import com.akazaki.api.infrastructure.web.mapper.order.OrderResponseMapper;
import com.akazaki.api.infrastructure.web.mapper.stockAvailability.StockAvailabilityMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order management APIs")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    private final CreateOrderCommandHandler createOrderCommandHandler;
    private final UpdateOrderAddressesCommandHandler updateOrderAddressesCommandHandler;
    private final CheckStockAvailabilityQueryHandler checkStockAvailabilityQueryHandler;
    private final OrderResponseMapper orderMapper;
    private final StockAvailabilityMapper stockAvailabilityMapper;
    
    @Operation(
        summary = "Create a new order",
        description = "Creates a new order from the current user's cart"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Order successfully created",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Cart is empty or invalid data",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Long userId = userEntity.getId();

        Order order = createOrderCommandHandler.handle(new CreateOrderCommand(userId));
        OrderResponse orderResponse = orderMapper.toResponse(order);
        return ResponseEntity.ok(orderResponse);
    }
    
    @Operation(
        summary = "Update order addresses",
        description = "Updates the billing and shipping addresses for an existing order"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Order addresses successfully updated",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Order not found",
            content = @Content
        )
    })
    @PutMapping("/{orderId}/address")
    public ResponseEntity<OrderResponse> updateOrderAddresses(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderAddressesRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Long userId = userEntity.getId();

        UpdateOrderAddressesCommand command = orderMapper.toCommand(orderId, userId, request);
        
        Order updatedOrder = updateOrderAddressesCommandHandler.handle(command);
        
        OrderResponse orderResponse = orderMapper.toResponse(updatedOrder);
        
        return ResponseEntity.ok(orderResponse);
    }

    @Operation(
        summary = "Check stock availability for an order",
        description = "Verifies if all products in the order have sufficient stock available"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock availability checked successfully",
            content = @Content(schema = @Schema(implementation = StockAvailabilityResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Order not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content
        )
    })
    @GetMapping("/{orderId}/stock-availability")
    public ResponseEntity<StockAvailabilityResponse> checkStockAvailability(@PathVariable Long orderId) {
        CheckStockAvailabilityQuery query = new CheckStockAvailabilityQuery(orderId);
        StockAvailability stockAvailability = checkStockAvailabilityQueryHandler.handle(query);
        StockAvailabilityResponse response = stockAvailabilityMapper.toResponse(stockAvailability);
        
        return ResponseEntity.ok(response);
    }
}

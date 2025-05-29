package com.akazaki.api.infrastructure.web.controller.order;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akazaki.api.application.commands.CreateOrder.CreateOrderCommandHandler;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.in.commands.CreateOrderCommand;
import com.akazaki.api.infrastructure.persistence.User.UserEntity;
import com.akazaki.api.infrastructure.web.dto.response.OrderResponse;
import com.akazaki.api.infrastructure.web.mapper.order.OrderResponseMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrderCommandHandler createOrderCommandHandler;
    private final OrderResponseMapper orderMapper;
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Long userId = userEntity.getId();

        Order order = createOrderCommandHandler.handle(new CreateOrderCommand(userId));
        OrderResponse orderResponse = orderMapper.toResponse(order);
        return ResponseEntity.ok(orderResponse);
    }
}

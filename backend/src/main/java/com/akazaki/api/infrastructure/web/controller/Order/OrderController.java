package com.akazaki.api.infrastructure.web.controller.Order;


import com.akazaki.api.application.commands.CreateOrder.CreateOrderCommandHandler;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.in.commands.CreateOrderCommand;
import com.akazaki.api.infrastructure.web.dto.request.CreateOrderRequest;
import com.akazaki.api.infrastructure.web.dto.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderCommandHandler createOrderCommandHandler;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        CreateOrderCommand command = new CreateOrderCommand(
                request.getStatus(),
                request.getDate(),
                        request.getTotalPrice(),
                        request.getBillingAddress(),
                        request.getShippingAddress()
                );

        Order order = createOrderCommandHandler.handle(command);

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setDate(order.getDate());
        response.setStatus(order.getStatus());
        response.setTotalPrice(order.getTotalPrice());
        response.setBillingAddress(order.getBillingAddress());
        response.setShippingAddress(order.getShippingAddress());

        return ResponseEntity.ok(response);
    }
}
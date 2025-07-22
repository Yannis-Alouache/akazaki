package com.akazaki.api.application.commands;

import org.springframework.stereotype.Service;

import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.exceptions.UnauthorizedAccessException;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.in.commands.UpdateOrderAddressesCommand;
import com.akazaki.api.domain.ports.out.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateOrderAddressesCommandHandler {
    
    private final OrderRepository orderRepository;
    
    public Order handle(UpdateOrderAddressesCommand command) {
        Order existingOrder = orderRepository.findById(command.orderId())
            .orElseThrow(() -> new OrderNotFoundException());
        
        if (!existingOrder.getUser().getId().equals(command.userId())) {
            throw new UnauthorizedAccessException("Vous n'êtes pas autorisé à modifier cette commande");
        }
        
        Order updatedOrder = Order.builder()
            .id(existingOrder.getId())
            .user(existingOrder.getUser())
            .date(existingOrder.getDate())
            .status(existingOrder.getStatus())
            .items(existingOrder.getItems())
            .totalPrice(existingOrder.calculateTotalPrice())
            .billingAddress(command.billingAddress())
            .shippingAddress(command.shippingAddress())
            .build();

        return orderRepository.save(updatedOrder);
    }
} 
package com.akazaki.api.application.commands;

import org.springframework.stereotype.Service;

import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.in.commands.MarkOrderAsPaidCommand;
import com.akazaki.api.domain.ports.out.OrderRepository;

@Service
public class MarkOrderAsPaidCommandHandler {
    private final OrderRepository orderRepository;

    public MarkOrderAsPaidCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void handle(MarkOrderAsPaidCommand command) {
        Order order = orderRepository.findById(command.orderId())
            .orElseThrow(() -> new OrderNotFoundException());
        
        order.markAsPaid();
        orderRepository.save(order);
    }
}

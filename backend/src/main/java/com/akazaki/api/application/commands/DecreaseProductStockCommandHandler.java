package com.akazaki.api.application.commands;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.OrderItem;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.DecreaseProductStockCommand;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;

@Component
public class DecreaseProductStockCommandHandler {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public DecreaseProductStockCommandHandler(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public void handle(DecreaseProductStockCommand command) {
        // Get the order
        Order order = orderRepository.findById(command.orderId())
            .orElseThrow(() -> new OrderNotFoundException());

        // For each item in the order, decrease the product stock
        for (OrderItem orderItem : order.getItems()) {
            Product product = orderItem.getProduct();
            int quantityToDecrease = orderItem.getQuantity();
            
            // Decrease the stock
            product.decreaseStock(quantityToDecrease);
            
            // Save the updated product
            productRepository.save(product);
        }
    }
}

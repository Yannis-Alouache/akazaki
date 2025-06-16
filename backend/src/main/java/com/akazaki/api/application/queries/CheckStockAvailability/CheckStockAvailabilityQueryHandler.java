package com.akazaki.api.application.queries.CheckStockAvailability;

import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.model.MissingItem;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.OrderItem;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.model.StockAvailability;
import com.akazaki.api.domain.ports.in.queries.CheckStockAvailabilityQuery;
import com.akazaki.api.domain.ports.out.OrderRepository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CheckStockAvailabilityQueryHandler {
    private final OrderRepository orderRepository;

    public CheckStockAvailabilityQueryHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public StockAvailability handle(CheckStockAvailabilityQuery query) {
        Order order = orderRepository.findById(query.orderId())
                .orElseThrow(() -> new OrderNotFoundException());

        List<MissingItem> missingItems = new ArrayList<>();
        
        // Vérifier le stock pour chaque item de la commande
        for (OrderItem orderItem : order.getItems()) {
            Product product = orderItem.getProduct();
            
            int availableQuantity = product.getStock();
            int requestedQuantity = orderItem.getQuantity();
            
            if (availableQuantity < requestedQuantity) {
                MissingItem missingItem = MissingItem.create(
                    product.getId(),
                    product.getName(),
                    requestedQuantity,
                    availableQuantity
                );
                missingItems.add(missingItem);
            }
        }

        // Retourner le résultat
        if (missingItems.isEmpty()) {
            return StockAvailability.available(order.getId());
        } else {
            return StockAvailability.unavailable(order.getId(), missingItems);
        }
    }
} 
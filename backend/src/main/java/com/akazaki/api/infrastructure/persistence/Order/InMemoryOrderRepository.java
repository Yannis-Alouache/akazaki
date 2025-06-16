package com.akazaki.api.infrastructure.persistence.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.out.OrderRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
public class InMemoryOrderRepository implements OrderRepository {

    private final List<Order> orders = new ArrayList<>();
    
    @Override
    public Order save(Order order) {
        if (order.getId() != null) {
            orders.stream()
                .filter(o -> o.getId().equals(order.getId()))
                .findFirst()
                .ifPresent(o -> orders.remove(o));
        } else {
            order.setId((long) (orders.size() + 1));
        }
        
        orders.add(order);
        return order;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orders.stream()
            .filter(order -> order.getId().equals(orderId))
            .findFirst();
    }
    
}

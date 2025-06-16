package com.akazaki.api.infrastructure.persistence.Order;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.out.OrderRepository;

import lombok.RequiredArgsConstructor;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {
    private final JpaOrderRepository orderRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = mapper.toEntity(order);
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        return mapper.toDomain(savedOrderEntity);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findByIdWithItems(orderId);
        return orderEntity.map(mapper::toDomain);
    }
}

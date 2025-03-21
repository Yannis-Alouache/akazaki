package com.akazaki.api.infrastructure.persistence.Order;

import com.akazaki.api.domain.model.Order;;
import com.akazaki.api.domain.ports.out.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderMapper mapper;
    private final JpaOrderRepository repository;

    @Override
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}

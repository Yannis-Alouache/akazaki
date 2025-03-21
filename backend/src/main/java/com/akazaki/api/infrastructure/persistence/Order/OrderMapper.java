package com.akazaki.api.infrastructure.persistence.Order;

import com.akazaki.api.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderEntity toEntity(Order domain) {
        return OrderEntity.fromDomain(domain);
    }

    public Order toDomain(OrderEntity entity) {
        return entity.toDomain();
    }
}

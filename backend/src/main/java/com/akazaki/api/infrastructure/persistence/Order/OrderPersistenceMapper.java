package com.akazaki.api.infrastructure.persistence.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.infrastructure.persistence.OrderItem.OrderItemPersistenceMapper;
import com.akazaki.api.infrastructure.persistence.User.UserPersistenceMapper;

@Component
public class OrderPersistenceMapper {
    @Autowired
    private OrderItemPersistenceMapper orderItemMapper;

    @Autowired
    private UserPersistenceMapper userMapper;

    public OrderEntity toEntity(Order order) {
        return OrderEntity.builder()
            .id(order.getId())
            .user(userMapper.toEntity(order.getUser()))
            .date(order.getDate())
            .status(order.getStatus())
            .items(orderItemMapper.toEntityList(order.getItems()))
            .totalPrice(order.getTotalPrice())
            .build();
    }

    public Order toDomain(OrderEntity entity) {
        return Order.builder()
            .id(entity.getId())
            .user(userMapper.toDomain(entity.getUser()))
            .date(entity.getDate())
            .status(entity.getStatus())
            .items(orderItemMapper.toDomainList(entity.getItems()))
            .totalPrice(entity.getTotalPrice())
            .build();
    }
}

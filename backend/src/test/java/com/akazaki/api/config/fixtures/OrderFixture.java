package com.akazaki.api.config.fixtures;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.OrderItem;
import com.akazaki.api.domain.model.OrderStatus;

@Component
public class OrderFixture {
    public static final Order orderWithItems = Order.builder()
        .id(1L)
        .user(UserFixture.basicUser)
        .date(LocalDateTime.now())
        .status(OrderStatus.PENDING)
        .items(List.of(OrderItem.create(10, 100.0, ProductFixture.ramuneFraise)))
        .totalPrice(100.0)
        .build();
}

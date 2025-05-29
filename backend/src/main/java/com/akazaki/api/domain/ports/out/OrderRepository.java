package com.akazaki.api.domain.ports.out;

import java.util.Optional;

import com.akazaki.api.domain.model.Order;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByUserId(Long userId);
}
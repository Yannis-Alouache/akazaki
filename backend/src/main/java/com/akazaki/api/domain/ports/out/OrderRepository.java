package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Order;

public interface OrderRepository {
    Order save(Order order);
}

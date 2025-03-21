package com.akazaki.api.infrastructure.persistence.Order;

import org.springframework.data.repository.CrudRepository;

public interface JpaOrderRepository extends CrudRepository<OrderEntity, Long> {
}

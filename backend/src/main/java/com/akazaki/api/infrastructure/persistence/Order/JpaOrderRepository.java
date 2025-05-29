package com.akazaki.api.infrastructure.persistence.Order;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface JpaOrderRepository extends CrudRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByUserId(Long userId);

}
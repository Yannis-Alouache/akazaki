package com.akazaki.api.infrastructure.persistence.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface JpaOrderRepository extends CrudRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByUserId(Long userId);
    
    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithItems(@Param("id") Long id);
}
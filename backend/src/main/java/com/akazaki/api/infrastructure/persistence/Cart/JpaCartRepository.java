package com.akazaki.api.infrastructure.persistence.Cart;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


public interface JpaCartRepository extends CrudRepository<CartEntity, Long> {
    Optional<CartEntity> findByUserId(Long userId);
}

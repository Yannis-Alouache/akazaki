package com.akazaki.api.infrastructure.persistence.Product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends CrudRepository<ProductEntity, Long> {
    boolean existsByName(String name);
}

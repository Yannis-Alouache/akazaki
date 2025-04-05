package com.akazaki.api.infrastructure.persistence.Product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends CrudRepository<ProductEntity, Long> {
    boolean existsByName(String name);
    void deleteById(@NonNull Long id);
}

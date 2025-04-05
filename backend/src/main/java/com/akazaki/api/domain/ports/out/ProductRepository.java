package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Product;

import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    boolean existsByName(String name);
    void deleteById(Long productId);
}

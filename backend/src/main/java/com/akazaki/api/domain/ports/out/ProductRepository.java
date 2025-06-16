package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    boolean existsByName(String name);
    void deleteById(Long productId);
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByCategories(List<String> categoryName, Pageable pageable);
}

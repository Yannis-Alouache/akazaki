package com.akazaki.api.infrastructure.persistence.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.out.ProductRepository;

@Repository
@Profile("test")
public class InMemoryProductRepository implements ProductRepository {
    private final List<Product> products = new ArrayList<>();

    @Override
    public Product save(Product product) {
        product.setId((long) (products.size() + 1));
        products.add(product);
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return products.stream()
            .filter(product -> product.getId().equals(id))
            .findFirst();
    }

    @Override
    public boolean existsByName(String name) {
        return products.stream()
            .anyMatch(product -> product.getName().equals(name));
    }

    @Override
    public void deleteById(Long productId) {
        products.removeIf(product -> product.getId().equals(productId));
    }
}

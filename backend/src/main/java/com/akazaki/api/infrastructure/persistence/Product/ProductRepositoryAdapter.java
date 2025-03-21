package com.akazaki.api.infrastructure.persistence.Product;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.out.ProductRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
         Optional<ProductEntity> productEntity = repository.findById(id);
         return productEntity.map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
}

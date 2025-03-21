package com.akazaki.api.infrastructure.persistence.Product;

import java.util.List;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.infrastructure.persistence.Category.CategoryEntity;
import com.akazaki.api.infrastructure.persistence.Category.CategoryMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryMapper categoryMapper;

    public ProductEntity toEntity(Product domain) {
        List<CategoryEntity> categoryEntities = categoryMapper.toEntityList(domain.getCategories());

        return new ProductEntity(
            domain.getId(),
            domain.getName(),
            domain.getDescription(),
            domain.getPrice(),
            domain.getStock(),
            domain.getImageUrl(),
            categoryEntities
        );
    }

    public Product toDomain(ProductEntity entity) {
        List<Category> categories = categoryMapper.toDomainList(entity.getCategories());
        return Product.create(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                categories
        );
    }
}

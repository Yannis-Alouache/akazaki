package com.akazaki.api.infrastructure.persistence.Category;

import com.akazaki.api.domain.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryEntity toEntity(Category domain) {
        return CategoryEntity.fromDomain(domain);
    }

    public Category toDomain(CategoryEntity entity) {
        return entity.toDomain();
    }
}
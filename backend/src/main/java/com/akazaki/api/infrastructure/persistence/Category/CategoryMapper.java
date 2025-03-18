package com.akazaki.api.infrastructure.persistence.Category;

import com.akazaki.api.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryEntity toEntity(Category domain) {
        return CategoryEntity.fromDomain(domain);
    }

    public Category toDomain(CategoryEntity entity) {
        return entity.toDomain();
    }

    public List<Category> toDomainList(Iterable<CategoryEntity> categoriesEntity) {
        List<Category> categories = new ArrayList<>();
        categoriesEntity.forEach(categoryEntity -> categories.add(toDomain(categoryEntity)));
        return categories;
    }

    public List<CategoryEntity> toEntityList(Iterable<Category> categoriesDomain) {
        List<CategoryEntity> categories = new ArrayList<>();
        categoriesDomain.forEach(categoryDomain -> categories.add(toEntity(categoryDomain)));
        return categories;
    }
}
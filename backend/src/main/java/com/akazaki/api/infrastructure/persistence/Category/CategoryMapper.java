package com.akazaki.api.infrastructure.persistence.Category;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.infrastructure.web.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {
    public CategoryEntity toEntity(Category domain) {
        return CategoryEntity.fromDomain(domain);
    }

    public Category toDomain(CategoryEntity entity) {
        return entity.toDomain();
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

    public List<CategoryResponse> toResponseList(List<Category> categories) {
        List<CategoryResponse> responses = new ArrayList<>();
        categories.forEach(category -> responses.add(toResponse(category)));
        return responses;
    }
}
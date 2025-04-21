package com.akazaki.api.infrastructure.web.mapper.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.infrastructure.web.dto.response.CategoryResponse;

@Component
public class CategoryResponseMapper {
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

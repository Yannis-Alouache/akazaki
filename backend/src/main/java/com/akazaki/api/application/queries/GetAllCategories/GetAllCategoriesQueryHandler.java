package com.akazaki.api.application.queries.GetAllCategories;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.in.queries.GetAllCategoriesQuery;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllCategoriesQueryHandler {

    private final CategoryRepository categoryRepository;

    public GetAllCategoriesQueryHandler(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> handle(GetAllCategoriesQuery query) {
        return categoryRepository.findAll();
    }
}

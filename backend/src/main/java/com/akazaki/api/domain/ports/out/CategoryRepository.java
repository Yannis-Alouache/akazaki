package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.infrastructure.persistence.Category.CategoryEntity;

import java.util.List;

public interface CategoryRepository {
    Category save(Category category);
    boolean existsByName(String name);
    List<Category> findAll();
}
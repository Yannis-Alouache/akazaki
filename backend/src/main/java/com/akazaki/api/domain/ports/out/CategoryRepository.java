package com.akazaki.api.domain.ports.out;

import java.util.List;

import com.akazaki.api.domain.model.Category;

public interface CategoryRepository {
    Category save(Category category);
    boolean existsByName(String name);
    List<Category> findAllById(List<Long> categoryIds);
}
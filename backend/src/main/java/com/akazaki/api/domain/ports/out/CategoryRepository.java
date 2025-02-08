package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Category;

public interface CategoryRepository {
    Category save(Category category);
    boolean existsByName(String name);
}
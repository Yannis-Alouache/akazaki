package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CategoryFixture {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryFixture(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category category = new Category("Drink");

    public void saveCategories() {
       category = categoryRepository.save(category);
    }
}

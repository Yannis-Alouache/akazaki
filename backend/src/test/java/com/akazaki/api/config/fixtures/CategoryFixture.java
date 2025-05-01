package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CategoryFixture {
    private final CategoryRepository categoryRepository;
    
    public Category category;

    public CategoryFixture(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

        this.category = new Category("Drink");
    }

    public void saveCategories() {
       this.category = categoryRepository.save(category);
    }
}

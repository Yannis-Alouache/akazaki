package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CategoryFixture {
    private final CategoryRepository categoryRepository;
    
    public Category drink;
    public Category japan;

    public CategoryFixture(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

        this.drink = new Category("Drink");
        this.japan = new Category("Japan");
    }

    public void saveCategories() {
       this.drink = categoryRepository.save(drink);
       this.japan = categoryRepository.save(japan);
    }
}

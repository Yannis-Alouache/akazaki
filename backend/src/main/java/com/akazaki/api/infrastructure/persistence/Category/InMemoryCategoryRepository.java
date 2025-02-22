package com.akazaki.api.infrastructure.persistence.Category;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("test")
public class InMemoryCategoryRepository implements CategoryRepository {
    private final List<Category> categories = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(InMemoryCategoryRepository.class);

    @Override
    public Category save(Category category) {
        category.setId((long) (categories.size() + 1));
        categories.add(category);
        logger.debug("trying to save category : {}", category);
        return category;
    }

    @Override
    public boolean existsByName(String name) {
        return categories.stream()
            .anyMatch(category -> category.getName().equals(name));
    }

    @Override
    public List<Category> findAll() {
        return List.of();
    }
}
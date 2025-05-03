package com.akazaki.api.application.query.GetAllCategories;

import com.akazaki.api.application.queries.GetAllCategories.GetAllCategoriesQueryHandler;
import com.akazaki.api.config.fixtures.CategoryFixture;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.in.queries.GetAllCategoriesQuery;
import com.akazaki.api.infrastructure.persistence.Category.InMemoryCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Get All Categories Unit Tests")
class GetAllCategoriesUnitTest {

    private GetAllCategoriesQueryHandler queryHandler;
    private InMemoryCategoryRepository repository;
    private CategoryFixture categoryFixture;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCategoryRepository();
        queryHandler = new GetAllCategoriesQueryHandler(repository);
        categoryFixture = new CategoryFixture(repository);
        categoryFixture.saveCategories();
    }

    @Test
    @DisplayName("Get All Categories Successfully")
    void getAllCategories() {
        // Given
        Category category1 = categoryFixture.drink;
        Category category2 = categoryFixture.japan;

        // When
        List<Category> categories = queryHandler.handle(new GetAllCategoriesQuery());

        // Then
        assertEquals(2, categories.size());
        assertEquals(category1.getName(), categories.get(0).getName());
        assertEquals(category2.getName(), categories.get(1).getName());
    }
}
package com.akazaki.api.application.query;

import com.akazaki.api.application.queries.getAllCategories.GetAllCategoriesQueryHandler;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.in.queries.GetAllCategoriesQuery;
import com.akazaki.api.infrastructure.persistence.Category.InMemoryCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetAllCategoriesQueryHandlerTest {

    private GetAllCategoriesQueryHandler queryHandler;
    private InMemoryCategoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCategoryRepository();
        queryHandler = new GetAllCategoriesQueryHandler(repository);
    }

    @Test
    void getAllCategories() {
        // Given
        Category category1 = new Category(1L, "Category1");
        Category category2 = new Category(2L, "Category2");
        repository.save(category1);
        repository.save(category2);

        // When
        List<Category> categories = queryHandler.handle(new GetAllCategoriesQuery());

        // Then
        assertEquals(2, categories.size());
        assertEquals("Category1", categories.get(0).getName());
        assertEquals("Category2", categories.get(1).getName());
    }
}
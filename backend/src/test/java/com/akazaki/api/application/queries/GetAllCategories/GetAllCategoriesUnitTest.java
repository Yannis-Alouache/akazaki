package com.akazaki.api.application.queries.GetAllCategories;

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

    @BeforeEach
    void setUp() {
        InMemoryCategoryRepository repository = new InMemoryCategoryRepository();
        queryHandler = new GetAllCategoriesQueryHandler(repository);

        repository.save(CategoryFixture.japan);
        repository.save(CategoryFixture.drink);
    }

    @Test
    @DisplayName("Get All Categories Successfully")
    void getAllCategories() {
        // Given
        Category japan = CategoryFixture.japan;
        Category drink = CategoryFixture.drink;


        // When
        List<Category> categories = queryHandler.handle(new GetAllCategoriesQuery());

        // Then
        assertEquals(2, categories.size());
        assertEquals(japan.getName(), categories.get(0).getName());
        assertEquals(drink.getName(), categories.get(1).getName());
    }
}
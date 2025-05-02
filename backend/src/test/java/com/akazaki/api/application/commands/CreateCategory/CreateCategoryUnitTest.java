package com.akazaki.api.application.commands.CreateCategory;

import com.akazaki.api.config.fixtures.CategoryFixture;
import com.akazaki.api.domain.exceptions.CategoryAlreadyExistException;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.in.commands.CreateCategoryCommand;
import com.akazaki.api.infrastructure.persistence.Category.InMemoryCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Create Category Unit Tests")
class CreateCategoryUnitTest {

    private CreateCategoryCommandHandler handler;
    private CategoryFixture categoryFixture;

    @BeforeEach
    void setUp() {
        InMemoryCategoryRepository repository = new InMemoryCategoryRepository();
        handler = new CreateCategoryCommandHandler(repository);
        categoryFixture = new CategoryFixture(repository);
    }

    @Test
    @DisplayName("Create A Category Successfully")
    void createACategory() {
        // Given
        CreateCategoryCommand command = new CreateCategoryCommand(categoryFixture.category.getName());

        // When
        Category result = handler.handle(command);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(categoryFixture.category.getName());
    }

    @Test
    @DisplayName("Prevent Duplicate Categories")
    void preventDuplicateCategories() {
        // Given
        CreateCategoryCommand command = new CreateCategoryCommand(categoryFixture.category.getName());
        
        // First creation should succeed
        handler.handle(command);

        // When/Then - Second creation should fail
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(CategoryAlreadyExistException.class);
    }
}
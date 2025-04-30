package com.akazaki.api.application.commands.CreateCategory;

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

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Create Category Unit Tests")
class CreateCategoryUnitTest {

    private CreateCategoryCommandHandler handler;
    private InMemoryCategoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCategoryRepository();
        handler = new CreateCategoryCommandHandler(repository);
    }

    @Test
    @DisplayName("Create A Category Successfully")
    void createACategory() {
        // Given
        String categoryName = "Electronics";
        CreateCategoryCommand command = new CreateCategoryCommand(categoryName);

        // When
        Category result = handler.handle(command);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(categoryName);
        assertThat(repository.existsByName(categoryName)).isTrue();
    }

    @Test
    @DisplayName("Prevent Duplicate Categories")
    void preventDuplicateCategories() {
        // Given
        String categoryName = "Electronics";
        CreateCategoryCommand command = new CreateCategoryCommand(categoryName);
        
        // First creation should succeed
        handler.handle(command);

        // When/Then - Second creation should fail
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(CategoryAlreadyExistException.class);
    }
}
package com.akazaki.api.infrastructure.web.controller.admin;

import com.akazaki.api.application.queries.GetAllCategories.GetAllCategoriesQueryHandler;
import com.akazaki.api.domain.ports.in.commands.CreateCategoryCommand;
import com.akazaki.api.application.commands.CreateCategory.CreateCategoryCommandHandler;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.in.queries.GetAllCategoriesQuery;
import com.akazaki.api.infrastructure.web.dto.request.CreateCategoryRequest;
import com.akazaki.api.infrastructure.web.dto.response.CategoryResponse;
import com.akazaki.api.infrastructure.web.mapper.category.CategoryResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CreateCategoryCommandHandler createCategoryCommandHandler;
    private final GetAllCategoriesQueryHandler categoriesQueryHandler;
    private final CategoryResponseMapper categoryMapper;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        CreateCategoryCommand command = new CreateCategoryCommand(request.getName());
        Category category = createCategoryCommandHandler.handle(command);
        return ResponseEntity.ok(new CategoryResponse(category.getId(), category.getName()));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category>categories = categoriesQueryHandler.handle(new GetAllCategoriesQuery());
        List<CategoryResponse> response = categoryMapper.toResponseList(categories);
        return ResponseEntity.ok(response);

    }
}
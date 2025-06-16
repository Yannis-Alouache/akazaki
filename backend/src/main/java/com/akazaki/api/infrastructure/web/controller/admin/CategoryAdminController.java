package com.akazaki.api.infrastructure.web.controller.admin;

import com.akazaki.api.application.queries.GetAllCategories.GetAllCategoriesQueryHandler;
import com.akazaki.api.domain.ports.in.commands.CreateCategoryCommand;
import com.akazaki.api.application.commands.CreateCategory.CreateCategoryCommandHandler;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.in.queries.GetAllCategoriesQuery;
import com.akazaki.api.infrastructure.web.dto.request.CreateCategoryRequest;
import com.akazaki.api.infrastructure.web.dto.response.CategoryResponse;
import com.akazaki.api.infrastructure.web.mapper.category.CategoryResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Category Admin", description = "Category administration routes")
public class CategoryAdminController {
    private final CreateCategoryCommandHandler createCategoryCommandHandler;
    private final GetAllCategoriesQueryHandler categoriesQueryHandler;
    private final CategoryResponseMapper categoryMapper;

    @Operation(
        summary = "Create a new category",
        description = "Creates a new product category with the provided name"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Category successfully created",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        CreateCategoryCommand command = new CreateCategoryCommand(request.getName());
        Category category = createCategoryCommandHandler.handle(command);
        return ResponseEntity.ok(new CategoryResponse(category.getId(), category.getName()));
    }

    @Operation(
        summary = "Get all categories",
        description = "Retrieves a list of all available categories"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Categories successfully retrieved",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category>categories = categoriesQueryHandler.handle(new GetAllCategoriesQuery());
        List<CategoryResponse> response = categoryMapper.toResponseList(categories);
        return ResponseEntity.ok(response);

    }
}
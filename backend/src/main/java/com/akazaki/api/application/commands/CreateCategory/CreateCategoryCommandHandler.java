package com.akazaki.api.application.commands.CreateCategory;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.in.commands.CreateCategoryCommand;
import com.akazaki.api.domain.ports.out.*;
import com.akazaki.api.domain.exceptions.CategoryAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCategoryCommandHandler {
    private final CategoryRepository categoryRepository;

    public Category handle(CreateCategoryCommand command) {
        if (categoryRepository.existsByName(command.name())) {
            throw new CategoryAlreadyExistException();
        }

        Category category = new Category(command.name());
        return categoryRepository.save(category);
    }
}
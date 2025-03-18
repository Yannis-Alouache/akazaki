package com.akazaki.api.application.commands.CreateProduct;

import com.akazaki.api.domain.exceptions.ProductAlreadyExistException;
import com.akazaki.api.domain.exceptions.UnableToFetchCategoriesException;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.CreateProductCommand;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateProductCommandHandler {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Product handle(CreateProductCommand command) {
        var categories = categoryRepository.findAllById(command.categoryIds());

        if (categories.size() != command.categoryIds().size())
            throw new UnableToFetchCategoriesException();

        if (productRepository.existsByName(command.name()))
            throw new ProductAlreadyExistException();

        Product product = Product.create(
            command.name(),
            command.description(),
            command.price(),
            command.stock(),
            categories
        );

        return productRepository.save(product);
    }
}

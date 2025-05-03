package com.akazaki.api.infrastructure.web.mapper.product;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.infrastructure.web.dto.response.ProductResponse;
import com.akazaki.api.infrastructure.web.mapper.category.CategoryResponseMapper;

import org.springframework.stereotype.Component;

@Component
public class ProductResponseMapper {

    private final CategoryResponseMapper categoryMapper;

    ProductResponseMapper(CategoryResponseMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.getId(), 
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getImageUrl(),
            categoryMapper.toResponseList(product.getCategories())
        );
    }
}
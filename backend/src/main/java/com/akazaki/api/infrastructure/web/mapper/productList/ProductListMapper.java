package com.akazaki.api.infrastructure.web.mapper.productList;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.infrastructure.web.dto.response.ProductListResponse;
import com.akazaki.api.infrastructure.web.mapper.product.ProductResponseMapper;

@Component
public class ProductListMapper {

    private final ProductResponseMapper productMapper;

    ProductListMapper(ProductResponseMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ProductListResponse toResponse(Page<Product> products) {
        return new ProductListResponse(
            products.getContent().stream().map(productMapper::toResponse).toList(),
            products.getNumber(),
            products.getSize(),
            products.getTotalElements(),
            products.getTotalPages(),
            products.isLast(),
            products.isFirst()
        );
    }
}

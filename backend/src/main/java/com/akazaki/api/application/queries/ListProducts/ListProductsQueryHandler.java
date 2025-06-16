package com.akazaki.api.application.queries.ListProducts;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.queries.ListProductsQuery;
import com.akazaki.api.domain.ports.out.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class ListProductsQueryHandler {
    private final ProductRepository productRepository;

    public ListProductsQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> handle(ListProductsQuery query) {
        int page = query.getPage() < 0 ? 0 : query.getPage();
        int size = query.getSize() <= 0 ? 10 : query.getSize();
        page = page != 0 ? page - 1 : page;

        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findAll(pageRequest);
    }
} 
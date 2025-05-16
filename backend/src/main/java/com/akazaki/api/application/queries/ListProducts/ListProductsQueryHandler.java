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
        if (query.getPage() < 0) query.setPage(0);
        if (query.getSize() <= 0) query.setSize(10);
        if (query.getPage() != 0) query.setPage(query.getPage() - 1);

        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getSize());
        return productRepository.findAll(pageRequest);
    }
} 
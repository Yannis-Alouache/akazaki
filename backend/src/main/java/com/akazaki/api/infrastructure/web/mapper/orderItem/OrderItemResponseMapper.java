package com.akazaki.api.infrastructure.web.mapper.orderItem;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.akazaki.api.domain.model.OrderItem;
import com.akazaki.api.infrastructure.web.dto.response.OrderItemResponse;
import com.akazaki.api.infrastructure.web.mapper.product.ProductResponseMapper;

@Component
public class OrderItemResponseMapper {
    @Autowired
    private ProductResponseMapper productMapper;

    public List<OrderItemResponse> toResponseList(List<OrderItem> items) {
        return items.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public OrderItemResponse toResponse(OrderItem item) {
        return new OrderItemResponse(
            item.getId(),
            item.getQuantity(),
            item.getPrice(),
            productMapper.toResponse(item.getProduct())
        );
    }
}

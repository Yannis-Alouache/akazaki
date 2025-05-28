package com.akazaki.api.infrastructure.persistence.OrderItem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.OrderItem;
import com.akazaki.api.infrastructure.persistence.Product.ProductPersistenceMapper;

@Component
public class OrderItemPersistenceMapper {
    @Autowired
    private ProductPersistenceMapper productMapper;

    public List<OrderItemEntity> toEntityList(List<OrderItem> items) {
        List<OrderItemEntity> entityList = new ArrayList<>();
        items.forEach(item -> entityList.add(toEntity(item)));
        return entityList;
    }

    public OrderItemEntity toEntity(OrderItem item) {
        return OrderItemEntity.builder()
            .id(item.getId())
            .quantity(item.getQuantity())
            .price(item.getPrice())
            .product(productMapper.toEntity(item.getProduct()))
            .build();
    }

    public OrderItem toDomain(OrderItemEntity item) {
        return OrderItem.restore(
            item.getId(),
            item.getQuantity(),
            item.getPrice(),
            productMapper.toDomain(item.getProduct())
        );
    }

    public List<OrderItem> toDomainList(List<OrderItemEntity> items) {
        List<OrderItem> domainList = new ArrayList<>();
        items.forEach(item -> domainList.add(toDomain(item)));
        return domainList;
    }
}

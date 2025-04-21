package com.akazaki.api.infrastructure.persistence.CartItem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.CartItem;
import com.akazaki.api.infrastructure.persistence.Product.ProductPersistenceMapper;

@Component
public class CartItemPersistenceMapper {
    @Autowired
    ProductPersistenceMapper productMapper;

    public CartItemEntity toEntity(CartItem domain) {
        return CartItemEntity.builder()
           .id(domain.getId())
           .quantity(domain.getQuantity())
           .product(productMapper.toEntity(domain.getProduct()))
           .build();
    }

    public CartItem toDomain(CartItemEntity entity) {
        return CartItem.builder()
          .id(entity.getId())
          .quantity(entity.getQuantity())
          .product(productMapper.toDomain(entity.getProduct()))
          .build();
    }

    public List<CartItem> toDomainList(List<CartItemEntity> entityList) {
        List<CartItem> domainList = new ArrayList<>();
        entityList.forEach(entity -> domainList.add(toDomain(entity)));
        return domainList;
    }

    public List<CartItemEntity> toEntityList(List<CartItem> domainList) {
        List<CartItemEntity> entityList = new ArrayList<>();
        domainList.forEach(domain -> entityList.add(toEntity(domain)));
        return entityList;
    }
}

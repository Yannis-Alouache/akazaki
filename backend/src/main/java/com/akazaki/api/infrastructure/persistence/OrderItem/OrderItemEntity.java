package com.akazaki.api.infrastructure.persistence.OrderItem;

import com.akazaki.api.infrastructure.persistence.Order.OrderEntity;
import com.akazaki.api.infrastructure.persistence.Product.ProductEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    private OrderEntity order;

    @ManyToOne
    private ProductEntity product;
}

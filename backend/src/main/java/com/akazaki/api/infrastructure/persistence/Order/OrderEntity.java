package com.akazaki.api.infrastructure.persistence.Order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.akazaki.api.domain.model.OrderStatus;
import com.akazaki.api.infrastructure.persistence.Address.AddressEntity;
import com.akazaki.api.infrastructure.persistence.OrderItem.OrderItemEntity;
import com.akazaki.api.infrastructure.persistence.User.UserEntity;

@Entity
@Table(name = "`order`")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false, name = "order_date")
    private LocalDateTime date;

    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    @Column(nullable = false)
    private double totalPrice;

    @ManyToOne
    private AddressEntity billingAddress;

    @ManyToOne
    private AddressEntity shippingAddress;
}
package com.akazaki.api.infrastructure.persistence.Order;

import com.akazaki.api.domain.model.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "order_date")
    private LocalDateTime date;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private String billingAddress;

    @Column(nullable = false)
    private String shippingAddress;

    public static OrderEntity fromDomain(Order order) {
        return new OrderEntity(
            order.getId(),
            order.getDate(),
            order.getStatus(),
            order.getTotalPrice(),
            order.getBillingAddress(),
            order.getShippingAddress()
        );
    }

    public Order toDomain() {
        return new Order(date, status, totalPrice, billingAddress, shippingAddress);
    }
}
package com.akazaki.api.infrastructure.persistence.Payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.akazaki.api.domain.model.PaymentMethodEnum;
import com.akazaki.api.infrastructure.persistence.Order.OrderEntity;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private OrderEntity order;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private PaymentMethodEnum method;
}

package com.akazaki.api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private String status;
    private BigDecimal totalPrice;

    @ManyToOne
    private Address billingAddress;

    @ManyToOne
    private Address shippingAddress;
    @OneToOne
    private Payment payment;
}

package com.akazaki.api.infrastructure.persistence.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Payment;
import com.akazaki.api.infrastructure.persistence.Order.OrderPersistenceMapper;

@Component
public class PaymentPersistenceMapper {
    @Autowired
    private OrderPersistenceMapper orderMapper;


    public PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
            .id(payment.getId())
            .order(orderMapper.toEntity(payment.getOrder()))
            .date(payment.getDate())
            .amount(payment.getAmount())
            .method(payment.getMethod())
            .build();
    }

    public Payment toDomain(PaymentEntity entity) {
        return Payment.builder()
            .id(entity.getId())
            .order(orderMapper.toDomain(entity.getOrder()))
            .date(entity.getDate())
            .amount(entity.getAmount())
            .method(entity.getMethod())
            .build();
    }
}

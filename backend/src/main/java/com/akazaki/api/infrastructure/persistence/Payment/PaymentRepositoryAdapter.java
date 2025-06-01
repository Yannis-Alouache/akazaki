package com.akazaki.api.infrastructure.persistence.Payment;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Payment;
import com.akazaki.api.domain.ports.out.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {
    private final JpaPaymentRepository paymentRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity paymentEntity = mapper.toEntity(payment);
        PaymentEntity savedPaymentEntity = paymentRepository.save(paymentEntity);
        return mapper.toDomain(savedPaymentEntity);
    }
    
}

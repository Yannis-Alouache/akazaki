package com.akazaki.api.infrastructure.persistence.Payment;

import org.springframework.data.repository.CrudRepository;

public interface JpaPaymentRepository extends CrudRepository<PaymentEntity, Long> {
    
}

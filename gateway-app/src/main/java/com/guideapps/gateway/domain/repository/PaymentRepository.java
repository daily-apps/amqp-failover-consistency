package com.guideapps.gateway.domain.repository;

import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.domain.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Long countByStatus(final PaymentStatus status);

}
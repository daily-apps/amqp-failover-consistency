package com.guideapps.checkout.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guideapps.checkout.domain.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> { 
	
}
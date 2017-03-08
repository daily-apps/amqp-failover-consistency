package com.guideapps.gateway.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guideapps.gateway.domain.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> { 
	
}
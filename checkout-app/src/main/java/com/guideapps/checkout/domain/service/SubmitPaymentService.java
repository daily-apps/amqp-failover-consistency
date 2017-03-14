package com.guideapps.checkout.domain.service;

import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guideapps.checkout.domain.model.Payment;
import com.guideapps.checkout.domain.repository.PaymentRepository;

import lombok.extern.java.Log;

@Log
@Service
public class SubmitPaymentService {

	@Autowired
	private PaymentRepository repository;
	
	@Lazy // to be not autowired during test execution
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Transactional
	public Payment process(Payment paymentNew) throws Exception {
		log.info(String.format("Init async process for: %s", paymentNew));
		final Payment paymentSaved = Optional.ofNullable(repository.save(paymentNew))
				.orElseThrow(() -> new Exception(String.format("Payment not saved: %s", paymentNew)));
				
		rabbitTemplate.convertAndSend("payment-topic", "payment.submitted", paymentSaved);
		return paymentSaved;
	}
	
}

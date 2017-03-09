package com.guideapps.checkout.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.guideapps.checkout.domain.model.Payment;
import com.guideapps.checkout.domain.repository.PaymentRepository;
import com.guideapps.checkout.domain.service.SubmitPaymentService;
import com.guideapps.checkout.util.OptionalResource;

import lombok.extern.java.Log;

@Log
@RequestMapping("payments")
@RestController
public class PaymentController {

	@Autowired
	private PaymentRepository repository;
	@Autowired
	private SubmitPaymentService submitPayment;
	
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Payment> all() {
		log.info("Consulting all payments registered...");
		return repository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Payment> create(@Valid @RequestBody final Payment paymentForm) throws Exception {
		log.info(String.format("Creating payment: %s", paymentForm));
		
		// Use PaymetForm or another builder helper that supports copy properties from reference object		
		final Payment paymentSubmitted = Payment.builder()
				.productId(paymentForm.getProductId())
				.sellerId(paymentForm.getSellerId())
				.quantity(paymentForm.getQuantity())
				.build();
		
		final Payment paymentSaved = submitPayment.process(paymentSubmitted);		
		return new ResponseEntity<Payment>(paymentSaved, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Payment> show(@PathVariable("id") final Long id) throws Exception {
		log.info(String.format("Find payment with id:%d...", id));
		
		final Payment payment = Optional.ofNullable(repository.findOne(id))
				.orElseThrow(() -> new Exception(String.format("Payment with id:%d not found.", id)));
		
		return new ResponseEntity<Payment>(payment, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public Payment update(@PathVariable final Long id, @RequestBody final Payment paymentForm) throws Exception {
		log.info(String.format("Update payment with id:%d...", id));
		
		// Use PaymetForm or another builder helper that supports copy properties from reference object		
		final Payment paymentToUpdate = Payment.builder()
				.id(id)
				.productId(paymentForm.getProductId())
				.sellerId(paymentForm.getSellerId())
				.quantity(paymentForm.getQuantity())
				.build();
		
		return OptionalResource
				.from(Optional.ofNullable(repository.findOne(paymentToUpdate.getId())))
				.ifPresent(p -> repository.save(paymentToUpdate));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Payment destroy(@PathVariable("id") final Long id) throws Exception {		
		return OptionalResource.from(Optional.ofNullable(repository.findOne(id))).ifPresent(p -> {
			repository.delete(p.getId());
			return p;
		});
	}
}

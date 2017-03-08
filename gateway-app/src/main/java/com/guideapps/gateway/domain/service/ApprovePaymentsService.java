package com.guideapps.gateway.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guideapps.gateway.client.PaypalPaymentClient;
import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.domain.model.PaymentStatus;
import com.guideapps.gateway.domain.repository.PaymentRepository;

@AllArgsConstructor
@Service
public class ApprovePaymentsService {

	@Autowired
	private PaypalPaymentClient paypalPaymentClient;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * Approve payments using paypal payment gateway.
	 * This flow could throw exceptions if some authorization or capture fail. 
	 * Some improvements like transaction rollback will not covered too.
	 *  
	 * @param paymentTemp
	 * @return paymentApprovedSaved 	
	 */
	public Payment approve(final Payment paymentTemp) {
		
		// Save payment with SUBMITTED and paypal AuthorizationToken
		final Payment paymentSubmittedToSave = Payment.builder()
		.productId(paymentTemp.getProductId())
		.sellerId(paymentTemp.getSellerId())
		.quantity(paymentTemp.getQuantity())
		.status(PaymentStatus.SUBMITTED)
		.build();
		
		final Payment paymentSubmittedSaved = paymentRepository.save(paymentSubmittedToSave);

        // Paypal Client is fake, and will return random tokens based on paymentId
		final String transactionToken = paypalPaymentClient.capture(paymentSubmittedSaved);
		
		// Save payment with APPROVED and paypal tokens
		final Payment paymentApprovedToSave = Payment.builder()
			.id(paymentSubmittedSaved.getId())
			.productId(paymentSubmittedSaved.getProductId())
			.sellerId(paymentSubmittedSaved.getSellerId())
			.quantity(paymentSubmittedSaved.getQuantity())
			.gatewayTransactionToken(transactionToken)
			.status(PaymentStatus.APPROVED)
			.build();
		
		final Payment paymentApprovedSaved = paymentRepository.save(paymentApprovedToSave);
		
		// Send notification to payment-topic with approve notification
		rabbitTemplate.convertAndSend("payment-topic", "payment.approved", paymentApprovedSaved);
		
		return paymentApprovedSaved;
	}
	
}

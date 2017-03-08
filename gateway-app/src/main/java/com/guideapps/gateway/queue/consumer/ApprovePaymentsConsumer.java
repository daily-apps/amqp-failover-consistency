package com.guideapps.gateway.queue.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.domain.service.ApprovePaymentsService;

import lombok.extern.java.Log;

@Log
@RabbitListener(queues={"${messaging.amqp.queues.payment-gateway}"})
public class ApprovePaymentsConsumer {

	@Autowired
	private ApprovePaymentsService approvePaymentsService;
	
	@RabbitHandler
	public void receiveMessage(Payment paymentSubmitted) throws Exception {
		log.info(String.format("Starting payment approvement for: %s", paymentSubmitted));
        
		approvePaymentsService.approve(paymentSubmitted);
    }

}

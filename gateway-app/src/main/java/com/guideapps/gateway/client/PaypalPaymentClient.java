package com.guideapps.gateway.client;

import org.springframework.stereotype.Service;

import com.guideapps.gateway.domain.model.Payment;

@Service
public class PaypalPaymentClient {

	public String capture(final Payment paymentToCapture) {
		return String.format("paypalTransactionPaymentToken:%d:%s", paymentToCapture.getId(), System.currentTimeMillis());
	}
	
}

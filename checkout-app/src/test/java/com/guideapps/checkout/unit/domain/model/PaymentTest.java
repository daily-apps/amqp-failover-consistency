package com.guideapps.checkout.unit.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.guideapps.checkout.domain.model.Payment;
import com.guideapps.checkout.domain.model.PaymentStatus;

public class PaymentTest {

	@Test
	public void builder() {
		Payment paymentOne = Payment.builder().id(1l).status(PaymentStatus.SUBMITTED).productId(1l).sellerId(1l).build();
		Payment paymentTwo = Payment.builder().id(2l).status(PaymentStatus.SUBMITTED).productId(2l).sellerId(2l).build();
		assertThat(paymentOne).isNotEqualTo(paymentTwo);
		
		Payment paymentOneEqual = Payment.builder().id(1l).status(PaymentStatus.SUBMITTED).productId(1l).sellerId(1l).build();
		assertThat(paymentOne).isEqualTo(paymentOneEqual);
		assertThat(paymentOne).isNotSameAs(paymentOneEqual);
		
		assertThat(paymentOne.getId()).isEqualTo(1);
		assertThat(paymentOne.getStatus()).isEqualTo(PaymentStatus.SUBMITTED);
		assertThat(paymentOne.getStatus().toString()).isEqualTo("SUBMITTED");
		assertThat(paymentOne.getProductId()).isEqualTo(1);
		assertThat(paymentOne.getSellerId()).isEqualTo(1);
	}
	
}

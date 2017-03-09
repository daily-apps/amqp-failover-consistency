package com.guideapps.checkout.unit.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.guideapps.checkout.domain.model.Payment;

public class PaymentTest {

	@Test
	public void builder() {
		Payment paymentOne = Payment.builder().id(1l).productId(1l).sellerId(1l).quantity(5).build();
		Payment paymentTwo = Payment.builder().id(2l).productId(2l).sellerId(2l).quantity(10).build();
		assertThat(paymentOne).isNotEqualTo(paymentTwo);
		
		Payment paymentOneEqual = Payment.builder().id(1l).productId(1l).sellerId(1l).quantity(5).build();
		assertThat(paymentOne).isEqualTo(paymentOneEqual);
		assertThat(paymentOne).isNotSameAs(paymentOneEqual);
		
		assertThat(paymentOne.getId()).isEqualTo(1);
		assertThat(paymentOne.getProductId()).isEqualTo(1);
		assertThat(paymentOne.getSellerId()).isEqualTo(1);
		assertThat(paymentOne.getQuantity()).isEqualTo(5);
	}
	
}

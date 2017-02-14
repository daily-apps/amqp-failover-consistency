package com.guideapps.checkout.unit.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.guideapps.checkout.domain.model.PaymentStatus;

public class PaymentStatusTest {

	@Test
	public void builder() {
		assertThat(PaymentStatus.values()).contains(PaymentStatus.SUBMITTED, PaymentStatus.APPROVED, PaymentStatus.NOT_APPROVED);
	}
	
}

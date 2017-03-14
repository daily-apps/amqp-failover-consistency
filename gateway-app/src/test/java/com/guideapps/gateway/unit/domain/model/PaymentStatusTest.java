package com.guideapps.gateway.unit.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.guideapps.gateway.domain.model.PaymentStatus;

public class PaymentStatusTest {

	@Test
	public void builder() {
		assertThat(PaymentStatus.values()).containsExactly(PaymentStatus.SUBMITTED, PaymentStatus.APPROVED);
	}
	
}

package com.guideapps.checkout.integration.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.guideapps.checkout.config.TestingDatabaseContext;
import com.guideapps.checkout.domain.model.Payment;
import com.guideapps.checkout.domain.repository.PaymentRepository;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=TestingDatabaseContext.class, initializers=ConfigFileApplicationContextInitializer.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // for isolated/clean database tests 
public class PaymentRepositoyTest {

	@Autowired
    private PaymentRepository repository;
	
	@BeforeClass
	public static void setUpAll() {
		FixtureFactoryLoader.loadTemplates("com.guideapps.checkout.fixture");
	}
	
	@Before
	public void setUp() {
		prepareDatabase();
	}
	
	@Test
	public void findAll() {
		final List<Payment> payments = repository.findAll();
		assertThat(payments.size()).isEqualTo(2);		
	}
	
	@Test
	public void save() {
		final Payment payment = Fixture.from(Payment.class).gimme("new");
		final Payment paymentSaved = repository.save(payment);
		
		assertThat(paymentSaved.getId()).isNotNull();
		assertThat(paymentSaved.getProductId()).isEqualTo(payment.getProductId());
		assertThat(paymentSaved.getSellerId()).isEqualTo(payment.getSellerId());
		assertThat(paymentSaved.getQuantity()).isEqualTo(payment.getQuantity());
	}
	
	private void prepareDatabase() {
		final Payment paymentOne = Fixture.from(Payment.class).gimme("new");
		final Payment paymentTwo = Fixture.from(Payment.class).gimme("new");
		final Payment paymentOneSaved = repository.save(paymentOne);
		final Payment paymentTwoSaved = repository.save(paymentTwo);
		
		assertThat(paymentOne).isEqualTo(paymentOneSaved);
		assertThat(paymentTwo).isEqualTo(paymentTwoSaved);
	}
	
}

package com.guideapps.gateway.integration.domain.repository;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.guideapps.gateway.config.TestingDatabaseContext;
import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.domain.repository.PaymentRepository;
import com.guideapps.gateway.fixture.PaymentFixture;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestingDatabaseContext.class, initializers = ConfigFileApplicationContextInitializer.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class PaymentRepositoyTest {

    @Autowired
    private PaymentRepository repository;

    @BeforeClass
    public static void setUpAll() {
        FixtureFactoryLoader.loadTemplates(PaymentFixture.class.getPackage().getName());
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
        final Payment payment = Fixture.from(Payment.class).gimme("paypal");
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

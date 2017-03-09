package com.guideapps.gateway.integration.domain.repository;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.guideapps.gateway.config.TestingDatabaseContext;
import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.domain.model.PaymentStatus;
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

import java.util.ArrayList;
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
        assertThat(payments.size()).isEqualTo(5);
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

    @Test
    public void countByStatus() {
        assertThat(repository.countByStatus(PaymentStatus.SUBMITTED)).isEqualTo(2);
        assertThat(repository.countByStatus(PaymentStatus.APPROVED)).isEqualTo(3);
    }

    private void prepareDatabase() {
        final List<Payment> payments = new ArrayList<>();
        payments.addAll(Fixture.from(Payment.class).gimme(2, "submitted"));
        payments.addAll(Fixture.from(Payment.class).gimme(3, "approved"));

        assertThat(payments.size()).isEqualTo(5);

        repository.save(payments);
    }

}

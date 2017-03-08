package com.guideapps.gateway.unit.client;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.guideapps.gateway.client.PaypalPaymentClient;
import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.fixture.PaymentFixture;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by gprado on 07/03/17.
 */
public class PaypalPaymentClientTest {

    @BeforeClass
    public static void setUpAll() {
        FixtureFactoryLoader.loadTemplates(PaymentFixture.class.getPackage().getName());
    }

    @Test
    public void capture() throws Exception {
        final Payment payment = Fixture.from(Payment.class).gimme("valid");
        final String paypalTransactionToken = new PaypalPaymentClient().capture(payment);
        assertThat(paypalTransactionToken).isNotBlank();
        assertThat(paypalTransactionToken).containsIgnoringCase(String.format("paypalTransactionPaymentToken:", payment.getId()));
        assertThat(paypalTransactionToken.contains("null")).isFalse();
    }

}
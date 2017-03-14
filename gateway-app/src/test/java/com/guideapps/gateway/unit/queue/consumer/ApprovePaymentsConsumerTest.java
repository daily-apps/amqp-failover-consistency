package com.guideapps.gateway.unit.queue.consumer;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.domain.service.ApprovePaymentsService;
import com.guideapps.gateway.fixture.PaymentFixture;
import com.guideapps.gateway.queue.consumer.ApprovePaymentsConsumer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gprado on 24/02/17.
 */
@RunWith(SpringRunner.class)
public class ApprovePaymentsConsumerTest {

    @MockBean
    private ApprovePaymentsService approvePaymentsServiceMock;
    @SpyBean
    private ApprovePaymentsConsumer approvePaymentsSubmittedConsumer;

    @BeforeClass public static void setUpAll() {
        FixtureFactoryLoader.loadTemplates(PaymentFixture.class.getPackage().getName());
    }

    @Test
    public void consumeApprovedPayments() throws Exception {
        final Payment payment = Fixture.from(Payment.class).gimme("valid");
        final Payment paymentApproved = Fixture.from(Payment.class).gimme("paypal");

        when(approvePaymentsServiceMock.approve(payment)).thenReturn(paymentApproved);

        approvePaymentsSubmittedConsumer.receiveMessage(payment);

        verify(approvePaymentsServiceMock).approve(payment);
    }

}
package com.guideapps.gateway.unit.domain.service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.guideapps.gateway.client.PaypalPaymentClient;
import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.domain.repository.PaymentRepository;
import com.guideapps.gateway.domain.service.ApprovePaymentsService;
import com.guideapps.gateway.fixture.PaymentFixture;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gprado on 24/02/17.
 */
@RunWith(SpringRunner.class)
public class ApprovePaymentsServiceTest {

    @SpyBean
    private ApprovePaymentsService approvePaymentsService;

    @MockBean
    private PaypalPaymentClient paypalPaymentClientMock;
    @MockBean
    private PaymentRepository paymentRepositoryMock;
    @MockBean
    private RabbitTemplate rabbitTemplateMock;

    @BeforeClass
    public static void setUpAll() {
        FixtureFactoryLoader.loadTemplates(PaymentFixture.class.getPackage().getName());
    }

    @Test
    public void approve() throws Exception {
        final Payment paymentTemp = Fixture.from(Payment.class).gimme("new");
        final Payment paymentSubmitted = Fixture.from(Payment.class).gimme("paypal");
        final Payment paymentApproved = Fixture.from(Payment.class).gimme("paypal");

        when(paymentRepositoryMock.save(paymentTemp)).thenReturn(paymentSubmitted);
        when(paypalPaymentClientMock.capture(paymentSubmitted))
            .thenReturn(paymentApproved.getGatewayTransactionToken());
        when(paymentRepositoryMock.save(paymentApproved)).thenReturn(paymentApproved);

        final Payment paymentApprovedSaved = approvePaymentsService.approve(paymentTemp);

        verify(paymentRepositoryMock).save(paymentSubmitted);
        verify(paypalPaymentClientMock).capture(paymentSubmitted);
        verify(paymentRepositoryMock).save(paymentApproved);
        verify(rabbitTemplateMock).convertAndSend("payment-topic", "payment.approved", paymentApproved);

        assertThat(paymentApprovedSaved).isEqualTo(paymentApproved);
    }

}
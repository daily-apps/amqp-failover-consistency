package com.guideapps.gateway.unit.domain.service;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.domain.model.PaymentStatus;
import com.guideapps.gateway.domain.model.Report;
import com.guideapps.gateway.domain.repository.PaymentRepository;
import com.guideapps.gateway.domain.service.ReportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gprado on 09/03/17.
 */
@RunWith(SpringRunner.class)
public class ReportServiceTest {

    @SpyBean
    private ReportService reportService;

    @MockBean
    private PaymentRepository paymentRepositoryMock;

    @Before
    public void setUp() throws Exception {
        FixtureFactoryLoader.loadTemplates(Payment.class.getPackage().getName());
    }

    @Test
    public void create() throws Exception {
        when(paymentRepositoryMock.countByStatus(PaymentStatus.SUBMITTED)).thenReturn(2l);
        when(paymentRepositoryMock.countByStatus(PaymentStatus.APPROVED)).thenReturn(3l);

        final Report report = reportService.create();

        verify(paymentRepositoryMock).countByStatus(PaymentStatus.SUBMITTED);
        verify(paymentRepositoryMock).countByStatus(PaymentStatus.SUBMITTED);

        assertThat(report.getPaymentsSubmitted()).isEqualTo(2);
        assertThat(report.getPaymentsApproved()).isEqualTo(3);
    }

}
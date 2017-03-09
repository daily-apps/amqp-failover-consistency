package com.guideapps.gateway.domain.service;

import com.guideapps.gateway.domain.model.PaymentStatus;
import com.guideapps.gateway.domain.model.Report;
import com.guideapps.gateway.domain.repository.PaymentRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by gprado on 09/03/17.
 */
@Log
@Service
public class ReportService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Report create() {
        log.info("Generating payments report...");

        final Report report = Report.builder()
            .paymentsSubmitted(paymentRepository.countByStatus(PaymentStatus.SUBMITTED))
            .paymentsApproved(paymentRepository.countByStatus(PaymentStatus.APPROVED))
            .build();

        log.info(String.format("Report generated: %s", report));

        return report;
    }
}

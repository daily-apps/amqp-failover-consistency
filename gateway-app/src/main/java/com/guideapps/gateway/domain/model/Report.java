package com.guideapps.gateway.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by gprado on 09/03/17.
 */
@Data
@Builder
public class Report {

    private Long paymentsApproved;
    private Long paymentsSubmitted;

}

package com.guideapps.gateway.controller;

import com.guideapps.gateway.domain.model.Report;
import com.guideapps.gateway.domain.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gprado on 08/03/17.
 */
@RequestMapping("reports")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(method = RequestMethod.GET)
    public Report index() {
        return reportService.create();
    }

}

package com.guideapps.gateway.unit.controller;

import com.guideapps.gateway.controller.ReportController;
import com.guideapps.gateway.domain.model.Report;
import com.guideapps.gateway.domain.service.ReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Created by gprado on 08/03/17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ReportController.class)
@AutoConfigureJson
@AutoConfigureJsonTesters
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;
    @Autowired
    private JacksonTester<Report> jsonReport;

    @Test
    public void index() throws Exception {
        final Report report = Report.builder().paymentsApproved(5l).paymentsSubmitted(5l).build();

        when(reportService.create()).thenReturn(report);

        this.mockMvc.perform(get("/reports").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(jsonReport.write(report).getJson()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(reportService).create();
    }

}
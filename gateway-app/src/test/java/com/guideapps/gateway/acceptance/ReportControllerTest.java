package com.guideapps.gateway.acceptance;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.domain.model.Report;
import com.guideapps.gateway.domain.repository.PaymentRepository;
import com.guideapps.gateway.fixture.PaymentFixture;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by gprado on 09/03/17.
 */
@ActiveProfiles("ci")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureJson
@AutoConfigureJsonTesters
public class ReportControllerTest {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private JacksonTester<Report> jsonReport;

    @BeforeClass
    public static void setUpAll() {
        FixtureFactoryLoader.loadTemplates(PaymentFixture.class.getPackage().getName());
    }

    @Before
    public void setUp() {
        prepareDatabase();
    }

    @Test
    public void index() {
        given()
            .when()
                .contentType(ContentType.JSON)
                .get("/reports/")
            .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("paymentsSubmitted", equalTo(2))
                .body("paymentsApproved", equalTo(3));
    }

    private void prepareDatabase() {
        final List<Payment> payments = new ArrayList<>();
        payments.addAll(Fixture.from(Payment.class).gimme(2, "submitted"));
        payments.addAll(Fixture.from(Payment.class).gimme(3, "approved"));

        assertThat(payments.size()).isEqualTo(5);

        paymentRepository.save(payments);
    }

}
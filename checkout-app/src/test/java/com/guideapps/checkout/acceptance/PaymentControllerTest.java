package com.guideapps.checkout.acceptance;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.guideapps.checkout.domain.model.Payment;
import com.guideapps.checkout.domain.model.PaymentStatus;
import com.guideapps.checkout.domain.repository.PaymentRepository;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("ci")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureJson
@AutoConfigureJsonTesters
public class PaymentControllerTest {
		
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
    private JacksonTester<List<Payment>> jsonPayments;
	@Autowired
    private JacksonTester<Payment> jsonPayment;
	
	final Payment newPayment = Fixture.from(Payment.class).gimme("new");
	final List<Payment> payments = Fixture.from(Payment.class).gimme(5, "new");
	
	@BeforeClass
	public static void setUpAll() {
		FixtureFactoryLoader.loadTemplates("com.guideapps.checkout.fixture");
	}
	
	@Before
	public void setUp() {
		paymentRepository.deleteAll();
		paymentRepository.save(payments);
	}
	
	@After
	public void cleanUp() {
		paymentRepository.deleteAll();
	}
	
	@Test
	public void all() throws Exception {
		final Response response = given()
		.when()
			.contentType(ContentType.JSON)
			.get("/payments");
				
		response.then()
			.contentType(ContentType.JSON)
			.statusCode(HttpStatus.SC_OK);
		
		
		assertThat(response.asString()).isEqualTo(jsonPayments.write(payments).getJson());
	}
	
	@Test
	public void create() throws Exception {
		given()
		.when()
			.contentType(ContentType.JSON)
			.body(jsonPayment.write(newPayment).getJson())
			.post("/payments/")
		.then()
			.contentType(ContentType.JSON)
			.statusCode(HttpStatus.SC_CREATED)
			.body("productId", equalTo(Integer.valueOf(newPayment.getProductId().toString())))
			.body("sellerId", equalTo(Integer.valueOf(newPayment.getSellerId().toString())))
			.body("quantity", equalTo(Integer.valueOf(newPayment.getQuantity().toString())))
			.body("status", equalTo(newPayment.getStatus().toString()));
	}
	
	@Test
	public void show() throws Exception {
		final Response response = given()
			.when()
				.contentType(ContentType.JSON)
				.get("/payments/{id}", payments.get(0).getId());
		
		response.then()
			.contentType(ContentType.JSON)
			.statusCode(HttpStatus.SC_OK);
		
		assertThat(response.asString()).isEqualTo(jsonPayment.write(payments.get(0)).getJson());
	}
	
	@Test
	public void update() throws Exception {
		final Payment paymentDB = payments.get(0);
		final Payment paymentToUpdate = Payment.builder()
				.productId(1234l)
				.sellerId(1234l)
				.quantity(1234)
				.status(PaymentStatus.SUBMITTED)
				.build();	
		
		given()
		.when()
			.contentType(ContentType.JSON)
			.body(jsonPayment.write(paymentToUpdate).getJson())
			.patch("/payments/{id}", paymentDB.getId())
		.then()
			.contentType(ContentType.JSON)
			.statusCode(HttpStatus.SC_OK)
			.body("id", equalTo(Integer.valueOf(paymentDB.getId().toString())))
			.body("productId", equalTo(Integer.valueOf(paymentToUpdate.getProductId().toString())))
			.body("sellerId", equalTo(Integer.valueOf(paymentToUpdate.getSellerId().toString())))
			.body("quantity", equalTo(Integer.valueOf(paymentToUpdate.getQuantity().toString())))
			.body("status", equalTo(paymentToUpdate.getStatus().toString()));
	}
	
	@Test
	public void delete() throws Exception {
		final Long paymentIdToDelete = payments.get(0).getId();
		
		given()
		.when()
			.contentType(ContentType.JSON)
			.delete("/payments/{id}", paymentIdToDelete)
		.then()
			.contentType(ContentType.JSON)
			.statusCode(HttpStatus.SC_OK);
		
		assertThat(paymentRepository.findOne(paymentIdToDelete)).isNull();
	}
	
}

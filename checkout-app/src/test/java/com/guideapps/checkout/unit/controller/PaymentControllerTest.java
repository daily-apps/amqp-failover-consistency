package com.guideapps.checkout.unit.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.guideapps.checkout.controller.PaymentController;
import com.guideapps.checkout.domain.model.Payment;
import com.guideapps.checkout.domain.model.PaymentStatus;
import com.guideapps.checkout.domain.repository.PaymentRepository;
import com.guideapps.checkout.domain.service.SubmitPaymentService;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(PaymentController.class)
@AutoConfigureJson
@AutoConfigureJsonTesters
public class PaymentControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private PaymentRepository paymentRepositoryMock;
	@MockBean
	private SubmitPaymentService submitPaymentMock;
	
	@Autowired
    private JacksonTester<List<Payment>> jsonPayments;
	@Autowired
    private JacksonTester<Payment> jsonPayment;
	
	@BeforeClass
	public static void setUp() {
		FixtureFactoryLoader.loadTemplates("com.guideapps.checkout.fixture");
	}
	
	@Test
	public void all() throws Exception {
		final List<Payment> payments = Fixture.from(Payment.class).gimme(5, "valid");
		when(paymentRepositoryMock.findAll()).thenReturn(payments);
		
		this.mvc.perform(get("/payments").contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().json(jsonPayments.write(payments).getJson())).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void create() throws Exception {
		final Payment paymentNew = Fixture.from(Payment.class).gimme("new");
		final Payment paymentSaved = Fixture.from(Payment.class).gimme("valid");
		
		when(submitPaymentMock.process(paymentNew)).thenReturn(paymentSaved);
		
		this.mvc.perform(post("/payments")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(jsonPayment.write(paymentNew).getJson()))
			.andExpect(status().isCreated())
			.andExpect(content().json(jsonPayment.write(paymentSaved).getJson()))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	public void show() throws Exception {
		final Payment paymentDB = Fixture.from(Payment.class).gimme("valid");
		when(paymentRepositoryMock.findOne(paymentDB.getId())).thenReturn(paymentDB);
		
		this.mvc.perform(get("/payments/{id}", paymentDB.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())
			.andExpect(content().json(jsonPayment.write(paymentDB).getJson()))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void update() throws Exception {
		final Payment paymentDB = Fixture.from(Payment.class).gimme("valid");
		final Payment paymentUpdated = Fixture.from(Payment.class).gimme("valid", new Rule(){{
			add("status", PaymentStatus.APPROVED);
		}});
		
		when(paymentRepositoryMock.findOne(paymentDB.getId())).thenReturn(paymentDB);
		when(paymentRepositoryMock.save(paymentDB)).thenReturn(paymentUpdated);
		
		this.mvc.perform(patch("/payments/{id}", paymentDB.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(jsonPayment.write(paymentDB).getJson()))
			.andExpect(status().isOk())
			.andExpect(content()
			.contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void destroy() throws Exception {
		final Payment paymentDB = Fixture.from(Payment.class).gimme("valid");
		
		when(paymentRepositoryMock.findOne(paymentDB.getId())).thenReturn(paymentDB);
		doNothing().when(paymentRepositoryMock).delete(paymentDB);
		
		this.mvc.perform(delete("/payments/{id}", paymentDB.getId())
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(jsonPayment.write(paymentDB).getJson()))		
		.andExpect(status().isOk())
		.andExpect(content()
		.contentType(MediaType.APPLICATION_JSON_UTF8));
	}

}

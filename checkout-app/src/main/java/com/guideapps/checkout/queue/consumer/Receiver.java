package com.guideapps.checkout.queue.consumer;

import com.guideapps.checkout.domain.model.Payment;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

@Log
@Component
//@RabbitListener(queues={"${messaging.amqp.gateway.queue.payment-received}"})
public class Receiver {

	@Lazy
	@Autowired
    private JavaMailSender javaMailSender;
	
	@RabbitHandler
	public void receiveMessage(Payment paymentDelivered) throws Exception {
		log.info(String.format("Init async report process for: %s", paymentDelivered));
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom("trickapps@example.com");
        mimeMessage.setRecipients(RecipientType.TO, "gabrielmassote@example.com");
        mimeMessage.setSubject(paymentDelivered.toString());
        mimeMessage.setText("Received reports <" + paymentDelivered + ">");
        javaMailSender.send(mimeMessage);
    }

}

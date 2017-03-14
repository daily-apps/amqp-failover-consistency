package com.guideapps.checkout.queue.consumer;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.guideapps.checkout.domain.model.Payment;

import lombok.extern.java.Log;

@Log
@Component
public class ReceiverNotifications {
	
	@Lazy
	@Autowired
    private JavaMailSender javaMailSender;
	
	public void receiveMessage(Payment paymentDelivered) throws Exception {
		log.info(String.format("Init async notification process for: %s", paymentDelivered));
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom("trickapps@example.com");
        mimeMessage.setRecipients(RecipientType.TO, "gabrielmassote@example.com");
        mimeMessage.setSubject(paymentDelivered.toString());
        mimeMessage.setText("Received notification <" + paymentDelivered + ">");
        javaMailSender.send(mimeMessage);
    }

}

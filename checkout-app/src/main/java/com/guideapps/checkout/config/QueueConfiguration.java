package com.guideapps.checkout.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import com.guideapps.checkout.queue.consumer.Receiver;
import com.guideapps.checkout.queue.consumer.ReceiverNotifications;

@Profile("!test")
@Configuration
public class QueueConfiguration {

	@Value("${rabbitmq.host}")
	private String host;
	
	@Value("${rabbitmq.port}")
	private String port;
	
	@Value("${rabbitmq.username}")
	private String username;
	
	@Value("${rabbitmq.password}")
	private String password;
	
	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		final CachingConnectionFactory factory = new CachingConnectionFactory();
		
		factory.setHost(host);
		factory.setPort(Integer.valueOf(port));
		factory.setUsername(username);
		factory.setPassword(password);
		
		return factory;
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate() {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
	
	@Bean
	public RabbitAdmin rabbitAdmin() {
		final RabbitAdmin admin = new RabbitAdmin(cachingConnectionFactory());
		
		final Map<String, Object> dlqConfig = new HashMap<>();
		dlqConfig.put("x-dead-letter-exchange", "");
		dlqConfig.put("x-dead-letter-routing-key", "payment-report-flow-dlq");
		
		admin.declareQueue(new Queue("payment-report-flow", true, false, false, dlqConfig));
		admin.declareQueue(new Queue("payment-report-flow-dlq"));
		
		dlqConfig.put("x-dead-letter-routing-key", "payment-notification-flow-dlq");
		admin.declareQueue(new Queue("payment-notification-flow", true, false, false, dlqConfig));
		admin.declareQueue(new Queue("payment-notification-flow-dlq"));
		
		admin.declareExchange(new TopicExchange("payment-topic", true, false));
		admin.declareBinding(new Binding("payment-report-flow", DestinationType.QUEUE, "payment-topic", "payment.approved", null));
		admin.declareBinding(new Binding("payment-notification-flow", DestinationType.QUEUE, "payment-topic", "payment.submitted", null));
		
		return admin;
	}
	
	@Bean
	public SimpleMessageListenerContainer reportsMessageListenerContainer(MessageListenerAdapter reportConsumer, RabbitAdmin rabbitAdmin) {
	    final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	    container.setConnectionFactory(cachingConnectionFactory());
	    container.setQueueNames("payment-report-flow");
	    container.setRabbitAdmin(rabbitAdmin);
	    container.setMessageListener(rabbitTemplate());
	    container.setMessageConverter(jsonMessageConverter());
	    container.setAdviceChain(interceptor());
	    container.setMessageListener(reportConsumer);
	    return container;
	}
	
	@Bean
	public SimpleMessageListenerContainer notificationsMessageListenerContainer(MessageListenerAdapter notificationConsumer) {
	    final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	    container.setConnectionFactory(cachingConnectionFactory());
	    container.setQueueNames("payment-notification-flow");
	    container.setMessageListener(rabbitTemplate());
	    container.setMessageConverter(jsonMessageConverter());
	    container.setAdviceChain(interceptor());
	    container.setMessageListener(notificationConsumer);
	    return container;
	}
	
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter(); 
	}
	
	@Bean
	RetryOperationsInterceptor interceptor() {
		return RetryInterceptorBuilder.stateless()
				.maxAttempts(5)
				.backOffOptions(1000, 2.0, 10000) // initialInterval, multiplier, maxInterval	
				.recoverer(new RejectAndDontRequeueRecoverer()) // for RabbitMQ default DLQ routing logic
//				.recoverer(new RepublishMessageRecoverer(rabbitTemplate(), "payment-topic")) // for custom Topic Exchange routing logic created by user
				.build();
	}
	
	@Bean(name="reportConsumer")
    MessageListenerAdapter listenerReports(Receiver receiver) {
        final MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
        messageListenerAdapter.setMessageConverter(jsonMessageConverter());
        return messageListenerAdapter;
    }
	
	@Bean(name="notificationConsumer")
    MessageListenerAdapter listenerNotifications(ReceiverNotifications receiverNotifications) {
        final MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiverNotifications, "receiveMessage");
        messageListenerAdapter.setMessageConverter(jsonMessageConverter());
        return messageListenerAdapter;
    }
}

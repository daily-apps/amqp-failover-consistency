package com.guideapps.checkout.queue.consumer;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ReceiverConfiguration {

//	@Bean(name="paymentsQueue")
//	Queue paymentsQueue() {
//		return QueueBuilder.durable(queueName)
//				.withArgument("x-dead-letter-exchange", "")
//				.withArgument("x-dead-letter-routing-key", queueDlqName)
//				.build();
//	}
//	
//	@Bean(name="paymentsQueueDlq")
//	Queue paymentsQueueDlq() {
//		return QueueBuilder.durable(queueDlqName).build();
//	}
//
//	@Bean
//	TopicExchange exchange() {
//		return new TopicExchange(exchangeName);
//	}
//
//	@Bean
//	Binding binding(Queue paymentsQueue, TopicExchange exchange) {
//		return BindingBuilder.bind(paymentsQueue).to(exchange).with(bindingName);
//	}

//	@Bean
//	public StatefulRetryOperationsInterceptor interceptor() {
//		return RetryInterceptorBuilder.stateful()
//				.maxAttempts(Integer.valueOf(env.getProperty("spring.rabbitmq.listener.retry.max-attempts")))
//				.backOffOptions(1000, 2.0, 10000) // initialInterval, multiplier, maxInterval
//				.build();
//	}
//	
//	@Bean
//	ConnectionFactory connectionFactory() {
//		CachingConnectionFactory caching = new CachingConnectionFactory();
//		caching.setHost(env.getProperty("spring.rabbitmq.host"));
//		caching.setPort(Integer.valueOf(env.getProperty("spring.rabbitmq.port")));
//		caching.setUsername(env.getProperty("spring.rabbitmq.username"));
//		caching.setPassword(env.getProperty("spring.rabbitmq.password"));
//		caching.setChannelCacheSize(5);
//		return caching;
//	}
//
//	@Bean
//	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//			MessageListenerAdapter listenerAdapter) {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//		container.setConnectionFactory(connectionFactory);
//		container.setQueueNames(queueName);
//		container.setMessageListener(listenerAdapter);
////		container.setAdviceChain(new Advice [] { interceptor() });
////		container.setRecoveryBackOff(
////			new FixedBackOff(
////				Integer.valueOf(env.getProperty("spring.rabbitmq.listener.retry.initial-interval")), 
////				Integer.valueOf(env.getProperty("spring.rabbitmq.listener.retry.max-attempts")
////			)
////		));
//		container.setConcurrentConsumers(5);
//		return container;
//	}
	
}

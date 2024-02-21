package com.isep.acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;



import java.awt.image.BufferedImage;

@SpringBootApplication
public class ACMEApplication {

	public static final String fanoutExchangeName = "my-fanout-exchange";
	static final String queue1Name = "queue-bootstrap-prodcuts";
	static final String queue2Name = "queue-get-products";
	static final String queue3Name = "queue-products-reviews";

	@Bean
	Queue queue1() {
		return new Queue(queue1Name, false);
	}

	@Bean
	Queue queue2() {
		return new Queue(queue2Name, false);
	}

	@Bean
	Queue queue3() {
		return new Queue(queue3Name, false);
	}
	@Bean
	FanoutExchange fanoutExchange() {
		return new FanoutExchange(fanoutExchangeName);
	}

	@Bean
	Binding binding1(Queue queue1, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(queue1).to(fanoutExchange);
	}

	@Bean
	Binding binding2(Queue queue2, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(queue2).to(fanoutExchange);
	}

	@Bean
	Binding binding3(Queue queue3, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(queue3).to(fanoutExchange);
	}

//	@Autowired
//	private Receiver receiver;

//	@Bean
//	MessageListenerAdapter listenerAdapter() {
//		return new MessageListenerAdapter(receiver, "receiveMessage");
//	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}

	public static void main(String[] args) {
		SpringApplication.run(ACMEApplication.class, args);
	}

	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}
}

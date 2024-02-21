package com.isep.acme;

import com.isep.acme.rabbitmq.Receiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;

@SpringBootApplication
public class ACMEApplication {

	public static void main(String[] args) {
		SpringApplication.run(ACMEApplication.class, args);
	}

	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}

	public static final String fanoutExchangeName = "my-fanout-exchange2-votes";


	//recebo votos
	static final String queue2Name = "queue-votes-query";

	//mando votos do bootstrap
	static final String queue1Name = "queue-votes-bootstrap";

	@Bean
	Queue queue1() {
		return new Queue(queue1Name, false);
	}

	@Bean
	Queue queue2() {
		return new Queue(queue2Name, false);
	}

	@Bean
	FanoutExchange fanoutExchange() {
		return new FanoutExchange(fanoutExchangeName);
	}

	@Bean
	Binding binding1(Queue queue1, FanoutExchange fanoutExchange2) {
		return BindingBuilder.bind(queue1).to(fanoutExchange2);
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(queue2());
		container.setMessageListener(listenerAdapter);
		return container;
	}
}

package com.sb.demo.pub.rabbit;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ消息队列配置
 * <ul>
 * <li>1.配置RabbitAdmin和RabbitTemplate</li>
 * <li>2.配置死信交换机和延迟队列,消息消费异常时请调用
 * {@link com.rabbitmq.client.Channel类中的basicReject方法}将消息路由到死信队列中排队</li>
 * </ul>
 * 
 * @author xg
 * @date 2019-10-31
 */
@Configuration
@AutoConfigureAfter(RabbitAutoConfiguration.class)
public class RabbitConfig {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String DEAD_LETTER_EXCHANGE = "xxy.deadletter.exchange";
	public static final String DEAD_LETTER_QUEUE = "xxy.deadletter.queue";
	public static final String DEAD_LETTER_ROUTING_KEY = "deadletter";

	public static final String STUDENT_EXCHANGE = "xxy.student.exchange";
	public static final String STUDENT_QUEUE = "xxy.student.queue";
	public static final String STUDENT_ROUTING_KEY = "stu";

	@Bean
	public Queue studentQueue() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		// arguments.put("x-message-ttl", 5 * 1000);
		arguments.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
		arguments.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
		return new Queue(STUDENT_QUEUE, true, false, false, arguments);
	}

	@Bean
	public TopicExchange studentExchange() {
		return new TopicExchange(STUDENT_EXCHANGE);
	}
	
	@Bean
	public Binding studentBingding(){
		return BindingBuilder.bind(studentQueue()).to(studentExchange()).with(STUDENT_ROUTING_KEY);
	}
	
	/**
	 * 定义死信队列
	 * @return
	 */
	@Bean
	@Qualifier(DEAD_LETTER_QUEUE)
	public Queue deadLetterQueue(){
		return new Queue(DEAD_LETTER_QUEUE);
	}

	/**
	 * 定义死信交换机
	 * 
	 * @return
	 */
	@Bean
	@Qualifier(DEAD_LETTER_EXCHANGE)
	public DirectExchange deadLetterExchange() {
		return new DirectExchange(DEAD_LETTER_EXCHANGE);
	}
	
	/**
	 * 绑定死信队列到死信交换机上
	 * 
	 * @return
	 */
	@Bean
	public Binding dlxBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_LETTER_ROUTING_KEY);
	}
	

	@Bean
	public RabbitAdmin getRabbitAdmin(CachingConnectionFactory connectionFactory) {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
		return rabbitAdmin;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
		connectionFactory.setPublisherConfirms(true);
		connectionFactory.setPublisherReturns(true);
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(new FastJsonMessageConverter());
		template.setMandatory(true);
		template.setConfirmCallback((correlationData, ack, cause) -> {
			logger.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
		});
		template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
			logger.info("消息退回:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange,
					routingKey, replyCode, replyText, message);
		});
		return template;
	}
}

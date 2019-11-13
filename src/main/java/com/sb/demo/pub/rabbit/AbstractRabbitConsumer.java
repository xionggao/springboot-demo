package com.sb.demo.pub.rabbit;

import javax.persistence.MappedSuperclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitmq.client.Channel;

/**
 * RabbitMq消费者基类
 * 
 * @author xg
 * @date 2019-11-6
 */
@MappedSuperclass
public abstract class AbstractRabbitConsumer implements InitializingBean, ChannelAwareMessageListener, DisposableBean {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Override
	public void destroy() throws Exception {
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("-----------afterPropertiesSet----------");
	}

	/**
	 * 消费消息(返回状态true:业务处理成功,false业务处理失败)
	 * 
	 * @param message
	 * @return
	 */
	protected abstract boolean doConsumeMsg(Message message);

	/**
	 * 定义交换机的名称
	 * 
	 * @return
	 */
	protected abstract String getExchangeName();

	/**
	 * 定义消息队列的名称
	 * 
	 * @return
	 */
	protected abstract String[] getQueueNames();

	/**
	 * 定义路由的键值
	 * 
	 * @return
	 */
	protected abstract String getRouteKey();

}

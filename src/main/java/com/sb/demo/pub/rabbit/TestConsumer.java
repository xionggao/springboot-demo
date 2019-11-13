package com.sb.demo.pub.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
public class TestConsumer extends AbstractRabbitConsumer{

	@Override
	public void onMessage(Message message) {
		
	}

	@Override
	protected boolean doConsumeMsg(Message message) {
		return false;
	}

	@Override
	protected String getExchangeName() {
		return null;
	}

	@Override
	protected String[] getQueueNames() {
		return null;
	}

	@Override
	protected String getRouteKey() {
		return null;
	}

}

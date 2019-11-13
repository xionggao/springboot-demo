package com.sb.demo.pub.rabbit;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import com.alibaba.fastjson.JSON;

/**
 * 基于FastJson对MQ消息内容进行转换
 * 
 * @author xg
 * @date 2019-10-31
 */
public class FastJsonMessageConverter extends AbstractMessageConverter {

	private volatile String defaultCharset = "UTF-8"; // 默认的字符集

	public FastJsonMessageConverter() {
		super();
	}

	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = (defaultCharset == null || defaultCharset.isEmpty() ? defaultCharset : "UTF-8");
	}

	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
		String json = null;
		try {
			json = new String(message.getBody(), defaultCharset);
		} catch (UnsupportedEncodingException e) {
			throw new MessageConversionException("Failed to convert Message to Object", e);
		}
		return json;
	}

	@Override
	protected Message createMessage(Object object, MessageProperties messageProperties) {
		byte[] bytes = null;
		try {
			String jsonString = JSON.toJSONString(object);
			bytes = jsonString.getBytes(this.defaultCharset);
		} catch (UnsupportedEncodingException e) {
			throw new MessageConversionException("Failed to convert Object to Message", e);
		}
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		messageProperties.setContentEncoding(this.defaultCharset);
		if (bytes != null) {
			messageProperties.setContentLength(bytes.length);
		}
		return new Message(bytes, messageProperties);
	}

}

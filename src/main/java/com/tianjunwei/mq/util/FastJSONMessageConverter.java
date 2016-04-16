/** 
*@ProjectName:TTRabbitMQ 
*@FileName: FastJSONMessageConverter.java
*@Date: 2016年4月16日下午4:48:51
*@Copyright: 2016 tianjunwei All rights reserved. 
*/ 
package com.tianjunwei.mq.util;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author tianjunwei
 * @date 2016年4月16日下午5:03:18
 * @modify by user: tianjunwei
 * @modify by reason: TODO
 * @version V1.0
 */
public class FastJSONMessageConverter extends AbstractMessageConverter {
	


	private static Logger log = LoggerFactory.getLogger(FastJSONMessageConverter.class);
	 
    public static final String DEFAULT_CHARSET = "UTF-8";
 
    private volatile String defaultCharset = DEFAULT_CHARSET;
     
   
    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = (defaultCharset != null) ? defaultCharset
                : DEFAULT_CHARSET;
    }
     
    public Object fromMessage(Message message)
            throws MessageConversionException {
        return null;
    }
     
    @SuppressWarnings("unchecked")
	public <T> T fromMessage(Message message,T t) {
        String json = "";
        try {
            json = new String(message.getBody(),defaultCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (T)JSON.parseObject(json,t.getClass());
    }   
     
 
    protected Message createMessage(Object objectToConvert,
            MessageProperties messageProperties)
            throws MessageConversionException {
        byte[] bytes = null;
        try {
            String jsonString = JSON.toJSONString(objectToConvert);
            bytes = jsonString.getBytes(this.defaultCharset);
        } catch (UnsupportedEncodingException e) {
            throw new MessageConversionException(
                    "Failed to convert Message content", e);
        } 
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(this.defaultCharset);
        if (bytes != null) {
            messageProperties.setContentLength(bytes.length);
        }
        return new Message(bytes, messageProperties);
 
    }
}
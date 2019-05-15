package com.scor.ea.salesfoce.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EaMessage implements MessageListener {

	private static final Logger LOGGER = Logger.getLogger(EaMessage.class);

	@Autowired
	private EaMessageReader eaMessageReader;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Receive new Message : " + message);
		TextMessage textMessage = (TextMessage) message;
		try {
			eaMessageReader.doAction(textMessage);
		} catch (JMSException e) {
			try {
				LOGGER.info(
						"error encoutred when saving message : " + textMessage.getText() + "\n" + e.getStackTrace());
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
		}
	}
}

package com.scor.ea.salesfoce.service;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConsumer {

	private final static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(JmsConsumer.class);

	private final static String JMS_BROKER_URL = "${jms.salesforce.broker.url}";
	private final static String JMS_BROKER_USERNAME = "${jms.salesforce.broker.username}";
	private final static String JMS_BROKER_PASSWORD = "${jms.salesforce.broker.password}";
	private final static String JMS_QUEUE = "${jms.salesforce.queue.basename}";

	@Value(JMS_BROKER_URL)
	private String jmsBrokerUrl;

	@Value(JMS_BROKER_USERNAME)
	private String jmsBrokerUsername;

	@Value(JMS_BROKER_PASSWORD)
	private String jmsBrokerPassword;

	@Value(JMS_QUEUE)
	private String jmsQueue;

	@Autowired
	private EaMessage eaMessageListner;

	@Bean
	public MessageConsumer activeMqConnection() throws JMSException {
		LOGGER.info("Opening Jms Connection with " + jmsBrokerUrl);
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(jmsBrokerUrl);
		connectionFactory.setUserName(jmsBrokerUsername);
		connectionFactory.setPassword(jmsBrokerPassword);

		Connection connection = connectionFactory.createConnection();
		connection.start();

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(session.createQueue(jmsQueue));
		consumer.setMessageListener(this.eaMessageListner);
		LOGGER.info("Connection opened with " + jmsBrokerUrl + " Listening to queue " + jmsQueue);
		return consumer;
	}

}

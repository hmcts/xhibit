package uk.gov.courtservice.framework.services.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.JMSServices;
import uk.gov.courtservice.framework.services.XSLServices;

/**
 * <p>
 * Title: JMSServicesImpl
 * </p>
 * <p>
 * Description: Implementation of the <code>JMSServices</code> interface.
 * Provides a range of utilites for sending messages
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell
 * @version $Id: JMSServicesImpl.java,v 1.2 2006/07/14 10:26:06 bzjrnl Exp $
 */
public class JMSServicesImpl implements JMSServices {

	/**
	 * Logger
	 */
	private static final Logger log = CSServices.getLogger(XSLServices.class);

	/**
	 * Singleton instance
	 */
	private static JMSServicesImpl instance;

	/**
	 * Get the singleton instance
	 */
	public static synchronized JMSServicesImpl getInstance() {
		if (instance == null) {
			instance = new JMSServicesImpl();
		}
		return instance;
	}

	/**
	 * Send the message as part of the current XA User Transaction.
	 * 
	 * @param jndiName
	 *            the queue to send the message to.
	 * @param factory
	 *            the message factory to use to create and populate the message.
	 */
	public void send(MessageFactory... factories) {
		try {
			// Use the default XA JMS connection factory
			ConnectionFactory connectionFactory = getConnectionFactory("weblogic.jms.XAConnectionFactory");
			Connection connection = connectionFactory.createConnection();
			try {
				connection.start();
				send(connection, factories);
			} finally {
				close(connection);
			}

		} catch (JMSException jmse) {
			throw new JMSServicesException(jmse);
		}
	}

	/**
	 * Send the messages created by the factories to the current connection
	 * 
	 * @param session
	 * @param factory
	 * @throws JMSException
	 */
	static void send(Connection connection, MessageFactory... factories)
			throws JMSException {
		// Note that we are taking part in the XA User Transaction so
		// this should not be marked as transacted.
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		try {
			send(session, factories);
		} finally {
			close(session);
		}
	}

	/**
	 * Send the messages created by the factories using the session
	 * 
	 * @param session
	 * @param factory
	 * @throws JMSException
	 */
	static void send(Session session, MessageFactory... factories)
			throws JMSException {
		for (MessageFactory factory : factories) {
			send(session, factory);
		}
	}

	/**
	 * Send the message created by the factory using the session
	 * 
	 * @param session
	 * @param factory
	 * @throws JMSException
	 */
	static void send(Session session, MessageFactory factory)
			throws JMSException {
		Message message = factory.create(session);
		Destination destination = getDestination(factory.getDestination());
		MessageProducer producer = session.createProducer(destination);
		producer.send(message);
	}

	private static Destination getDestination(String name) {
		// There is a bug in service locator that restricts it to names in the
		// root context.
		// return CSServices.getServiceLocator().getDestination(name);
		return (Destination)lookup(name);
	}

	private static ConnectionFactory getConnectionFactory(String name) {
		// There is a bug in service locator that restricts it to names in the
		// root context.
		// return CSServices.getServiceLocator().getConnectionFactory(name);
		return (ConnectionFactory)lookup(name);
	}

	private static Object lookup(String name) {
		try {
			Context context = new InitialContext();
			try {
				return context.lookup(name);
			} finally {
				try {
					context.close();
				} catch (NamingException ne) {
					log.error("An error occured closing the naming context.",
							ne);
				}
			}
		} catch (NamingException ne) {
			throw new JMSServicesException(ne);
		}
	}

	private static void close(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (JMSException jmse) {
			log.error("An error occured closing the jms connection.", jmse);
		}
	}

	private static void close(Session session) {
		try {
			if (session != null) {
				session.close();
			}
		} catch (JMSException jmse) {
			log.error("An error occured closing the jms session.", jmse);
		}
	}

}

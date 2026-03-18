package uk.gov.courtservice.framework.business.services;

import java.io.Serializable;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.XASession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import weblogic.transaction.TxHelper;

/**
 * A template class extracted to the framework to ease development of message
 * driven bean notifiers.
 * 
 * Class based upon original version (written by Meeraj) of
 * uk.gov.courtservice.xhibit.common.publicdisplay.jms.MessageSender.
 * 
 * WARNING
 * 
 * This class should no longer be used, use JMSServices instead. Will Fardell.
 * 11/07/2006.
 * 
 * WARNING
 * 
 * @author tz0d5m
 * @version $Id: CSMessageBeanNotifier.java,v 1.8 2006/07/12 15:52:35 bzjrnl Exp $
 */
public abstract class CSMessageBeanNotifier {
	/** Log4J Logger instance for this class. */
	protected final Logger log = CSServices.getLogger(getClass());

	/** Topic connection factory that is used */
	private final TopicConnection topicConnection;

	/** Topic that is used */
	private final Topic topic;

	/**
	 * @see CSMessageBeanNotifier#CSMessageBeanNotifier (Hashtable, String,
	 *      String)
	 */
	protected CSMessageBeanNotifier(String connectionFactoryName,
			String topicName) {
		this(null, connectionFactoryName, topicName);
	}

	/**
	 * Initializes the JNDI properties, topic name and connection factory.
	 * 
	 * @param environment
	 *            The environment for the <code>InitialContext</code>.
	 * @param connectionFactoryName
	 *            The JNDI name of the <code>TopicConnectionFactory</code>.
	 * @param topicName
	 *            The JNDI name of the <code>Topic</code>.
	 * 
	 * @throws IllegalArgumentException
	 *             if any of the passed in <code>String</code> values are
	 *             <i>null</i> or zero-lengthed.
	 * @throws CSUnrecoverableException
	 *             if any other error occurs.
	 */
	protected CSMessageBeanNotifier(Hashtable environment,
			String connectionFactoryName, String topicName) {
		if (log.isDebugEnabled()) {
			log.debug("Constructor()::environment = " + environment
					+ "; connectionFactoryName = \"" + connectionFactoryName
					+ "\"; topicName = \"" + topicName + "\"");
		}

		validateStringParameter("connectionFactoryName", connectionFactoryName);
		validateStringParameter("topicName", topicName);

		Context ctx = null;

		try {
			ctx = new InitialContext(environment);

			// Lookup the topic connection factory...
			TopicConnectionFactory tcf = (TopicConnectionFactory) ctx
					.lookup(connectionFactoryName);

			if (log.isDebugEnabled()) {
				log
						.debug("Topic connection factory created: "
								+ tcf.getClass());
			}

			// Create the topic connection...
			this.topicConnection = tcf.createTopicConnection();
			log.debug("Topic connection created");

			// Lookup the topic...
			this.topic = (Topic) ctx.lookup(topicName);
			log.debug("Topic retrieved");
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
			throw new CSUnrecoverableException(e);
		} catch (JMSException e) {
			log.error(e.getMessage(), e);
			throw new CSUnrecoverableException(e);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					log.error("Error whilst closing context", e);
				}
			}
		}
	}

	/**
	 * Method to close this notifier instances <code>TopicConnection</code>,
	 * no exceptions will be closed, any errors will only be logged.
	 * 
	 * @see javax.jms.TopicConnection#close()
	 */
	public final void close() {
		log.debug("close() - Attempting to close the topic connection");

		if (this.topicConnection != null) {
			try {
				this.topicConnection.close();
				log.debug("close() - Topic connection closed");
			} catch (Throwable t) {
				// ignore all errors whilst closing the connection...
				log.error("Error whilst closing the topic connection", t);
			}
		}
	}

	/**
	 * Send the message passed in to the required queue.
	 * 
	 * @param message
	 *            A <code>Serializable</code> message.
	 * @throws <code>IllegalArgumentException</code> if the passed in message is
	 *             <i>null</i>.
	 * @throws <code>CSUnrecoverableException</code> if any other error occurs.
	 */
	protected final void sendMessage(Serializable message) {
		if (message == null) {
			throw new IllegalArgumentException("message cannot be null");
		}

		TopicSession topicSession = null;

		try {
			// The message needs to be transacted so that it will not be
			// delivered before the database changes it signals have gone
			// through...
			topicSession = this.topicConnection.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);
			enlistXAResource(topicSession);

			// Create the object message...
			ObjectMessage msg = topicSession.createObjectMessage(message);

			// Set up the message headers...
			setMessageHeader(msg);

			// Create the publisher...
			TopicPublisher tp = topicSession.createPublisher(topic);

			// Publish the message...
			tp.publish(msg);
			log.debug("sendMessage - published.");
		} catch (JMSException e) {
			log.error(e.getMessage(), e);
			throw new CSUnrecoverableException(e);
		} finally {
			closeSession(topicSession);
		}
	}

	/**
	 * Set any headers on the message.
	 * 
	 * @param message
	 *            The message on which to set headers.
	 * @throws JMSException
	 *             When there is a problem with setting the header.
	 */
	protected abstract void setMessageHeader(ObjectMessage message)
			throws JMSException;

	/**
	 * Private helper method used to validate that the passed in
	 * <code>String</code> parameters are not <i>null</i> and not
	 * zero-length.
	 * 
	 * @param name
	 *            The name of the parameter we are testing.
	 * @param value
	 *            The value of the parameter we are testing.
	 */
	private void validateStringParameter(String name, String value) {
		if ((value == null) || (value.trim().length() == 0)) {
			throw new IllegalArgumentException(name + " is invalid: \"" + value
					+ "\"");
		}
	}

	/**
	 * Extracted helper method used to enlist the <code>TopicSession</code>
	 * with the transaction if it should be handled as an XA resource (which is
	 * deemed so if the transaction is an instance of
	 * <code>javax.xml.XASession</code>.
	 * 
	 * @param topicSession
	 *            The <code>TopicSession</code> that we want to enlist as an
	 *            XA resource.
	 * @throws CSUnrecoverableException
	 *             if any error occurs.
	 */
	private void enlistXAResource(TopicSession topicSession) {
		// not sure about this particular implementation, however, it is
		// copied from a working version, except that this is made more
		// general (to not use weblogic specific classes)...
		if (topicSession instanceof XASession) {
			log.debug("Attempting to register with the XA transaction...");
			final Transaction tx = TxHelper.getTransaction();

			try {
				tx.enlistResource(((XASession) topicSession).getXAResource());
			} catch (RollbackException e) {
				log.error("Failure in enlisting with transaction.", e);
				throw new CSUnrecoverableException(e);
			} catch (IllegalStateException e) {
				log.error("Failure in enlisting with transaction.", e);
				throw new CSUnrecoverableException(e);
			} catch (SystemException e) {
				log.error("Failure in enlisting with transaction.", e);
				throw new CSUnrecoverableException(e);
			}
		}
	}

	/**
	 * Extracted helper method used to handle the closing of a
	 * <code>Session</code>
	 * 
	 * @param session
	 *            The <code>Session</code> to close.
	 */
	private void closeSession(Session session) {
		if (session != null) {
			try {
				session.close();
			} catch (Throwable t) {
				// ignore all errors whilst closing the session...
				log.error("Error whilst closing the topic session", t);
			}
		}
	}
}

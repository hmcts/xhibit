/**
 * 
 */
package uk.gov.courtservice.framework.services.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * <p>
 * Title: MessageFactory
 * </p>
 * <p>
 * Description: A factory for producing messages, also includes their
 * destination.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell
 * @version $Id: MessageFactory.java,v 1.1 2006/07/11 10:10:14 bzjrnl Exp $
 */
public abstract class MessageFactory {
	private final String destination;

	/**
	 * Construct a new message factory
	 * 
	 * @param destination
	 *            the destination to send the messages to
	 */
	public MessageFactory(String destination) {
		if (destination == null) {
			throw new IllegalArgumentException("destination: null");
		}
		this.destination = destination;
	}

	public String getDestination() {
		return destination;
	}

	/**
	 * Create the message to be sent
	 * 
	 * @param session
	 *            the session used to create the message
	 */
	public abstract Message create(Session session) throws JMSException;

}
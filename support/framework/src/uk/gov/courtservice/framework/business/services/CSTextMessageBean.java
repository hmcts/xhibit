package uk.gov.courtservice.framework.business.services;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * Message driven bean template class. Removes the common code of all text
 * message beans into a well-defined framework class.
 * 
 * @author Will Fardell
 * @version $Id: CSTextMessageBean.java,v 1.1 2006/07/11 10:10:14 bzjrnl Exp $
 */
public abstract class CSTextMessageBean extends CSMessageBean {
	/** The log4j <code>Logger</code> instance */
	protected final Logger log = CSServices.getLogger(getClass());

	/**
	 * Called when a message is delivered.
	 * 
	 * @param message
	 */
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				onTextMessage((TextMessage) message);
			} else {
				getMessageDrivenContext().setRollbackOnly();
				log.info("Ignoring message of type " + message.getJMSType()
						+ ", this bean only handles text messages.");
			}
		} catch (Exception e) {
			getMessageDrivenContext().setRollbackOnly();
			throw new CSMessageBeanException(e);
		}
	}

	/**
	 * Called when a text message is delivered.
	 * 
	 * @param message
	 *            the message delivered
	 * @throws Exception
	 *             if an error occures
	 */
	protected abstract void onTextMessage(TextMessage message) throws Exception;
}

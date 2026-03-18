package uk.gov.courtservice.framework.services;

import uk.gov.courtservice.framework.services.jms.MessageFactory;

/**
 * <p>
 * Title: JMSServices
 * </p>
 * <p>
 * Description: Service used for sending messages.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell
 * @version $Id: JMSServices.java,v 1.1 2006/07/11 10:10:14 bzjrnl Exp $
 */
public interface JMSServices {
	/**
	 * Send the messages created from the factories
	 * @param factories the factories used to produce the messages
	 */
	public void send(MessageFactory... factories);
}

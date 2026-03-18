/**
 * 
 */
package uk.gov.courtservice.framework.services.jms;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: JMSServicesException
 * </p>
 * <p>
 * Description: Thrown when an error occures in the JMSServices
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell
 * @version $Id: JMSServicesException.java,v 1.1 2006/07/11 10:10:14 bzjrnl Exp $
 */
public class JMSServicesException extends CSUnrecoverableException {

	private static final long serialVersionUID = 1L;

	public JMSServicesException(Throwable cause) {
		super(cause);
	}

	public JMSServicesException(String msg) {
		super(msg);
	}
}
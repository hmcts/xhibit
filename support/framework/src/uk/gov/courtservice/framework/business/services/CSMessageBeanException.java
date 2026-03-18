/**
 * 
 */
package uk.gov.courtservice.framework.business.services;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

public class CSMessageBeanException extends CSUnrecoverableException {
	private static final long serialVersionUID = 1L;

	public CSMessageBeanException(Throwable cause) {
		super(cause);
	}

	public CSMessageBeanException(String msg) {
		super(msg);
	}
}
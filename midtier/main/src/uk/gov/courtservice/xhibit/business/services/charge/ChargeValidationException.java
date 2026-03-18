package uk.gov.courtservice.xhibit.business.services.charge;

/**
 * Exception designed to encapsulate problems in charge validation.
 * 
 * @author uphillj
 *
 */
public class ChargeValidationException extends ChargeControllerException {

	public ChargeValidationException() {
		super();
	}

	public ChargeValidationException(String errorKey, String logMessage, Throwable cause) {
		super(errorKey, logMessage, cause);
	}

	public ChargeValidationException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
		super(errorKey, parameters, logMessage, cause);
	}

	public ChargeValidationException(String errorKey, String logMessage) {
		super(errorKey, logMessage);
	}

	public ChargeValidationException(String errorKey, Object[] parameters, String logMessage) {
		super(errorKey, parameters, logMessage);
	}

}

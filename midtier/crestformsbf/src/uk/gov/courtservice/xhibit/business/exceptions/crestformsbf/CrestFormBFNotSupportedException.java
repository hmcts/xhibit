package uk.gov.courtservice.xhibit.business.exceptions.crestformsbf;

public class CrestFormBFNotSupportedException extends CrestFormBFException {
    
	static final long serialVersionUID = -3740149352811735344L;
	
	/**
     * Standard no arguments constructor.
     */
    public CrestFormBFNotSupportedException() {
        super();
    }

    /**
     * 
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CrestFormBFNotSupportedException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * 
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param parameters
     *            the parameters for the error message
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CrestFormBFNotSupportedException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public CrestFormBFNotSupportedException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }
}

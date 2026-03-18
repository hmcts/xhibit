package uk.gov.courtservice.xhibit.business.services.results;

public class CrestUnknownRefreshResyncException extends ResultsControllerException {
	
	static final long serialVersionUID = 1866152248576005007L;
	
    /**
     * @roseuid 3E2BF1F20234
     */
    public CrestUnknownRefreshResyncException() {
        super();
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CrestUnknownRefreshResyncException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
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
    public CrestUnknownRefreshResyncException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public CrestUnknownRefreshResyncException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
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
    public CrestUnknownRefreshResyncException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }
}

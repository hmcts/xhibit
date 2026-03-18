//Source file: D:\\jbproject\\XHIBIT\\session\\src\\uk\\gov\\courtservice\\xhibit\\business\\services\\results\\ResultsControllerException.java

package uk.gov.courtservice.xhibit.business.services.results;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsDebugValue;

public class ResultsControllerException extends CSBusinessException {
	
	static final long serialVersionUID = 5399444921605595983L;
	
    /**
     * Object dumps
     */
    private ResultsDebugValue resultsDebugValue;

    /**
     * @roseuid 3E2BF1F20234
     */
    public ResultsControllerException() {
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
    public ResultsControllerException(String errorKey, String logMessage, Throwable cause) {
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
    public ResultsControllerException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public ResultsControllerException(String errorKey, String logMessage) {
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
    public ResultsControllerException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }

    /**
     * Set the ResultsDebugValue (contains object graph dumps)
     */
    public void setResultsDebugValue(ResultsDebugValue resultsDebugValue) {
        this.resultsDebugValue = resultsDebugValue;
    }

    /**
     * Get the message
     */
    public String getMessage() {
        if (resultsDebugValue == null) {
            return super.getMessage();
        } else {
            return super.getMessage() + resultsDebugValue;
        }
    }

}

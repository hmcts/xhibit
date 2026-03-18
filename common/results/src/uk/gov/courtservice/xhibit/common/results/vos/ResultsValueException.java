package uk.gov.courtservice.xhibit.common.results.vos;

// JDK
import java.io.Serializable;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: ResultsValueException
 * </p>
 * <p>
 * Description: Thrown when there is an unrecoverable problem in a data object
 * for example trying to access unloaded data.
 * </p>
 * 
 * @author Willam Fardell
 * @version 1.0
 */
public class ResultsValueException extends CSUnrecoverableException implements Serializable {
    
	static final long serialVersionUID = -5843477132195560015L;
	
	/**
     * Construct the ResultsValueException with the given message
     * 
     * @param message
     *            describes the cause of the exception
     */
    public ResultsValueException(String message) {
        super(message);
    }

    /**
     * Construct the ResultsValueException with the given cause
     * 
     * @param cause
     *            the cause of the exception
     */
    public ResultsValueException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct the ResultsValueException with the given message and cause
     * 
     * @param message
     *            describes the cause of the exception
     * @param cause
     *            the cause of the exception
     */
    public ResultsValueException(String message, Throwable cause) {
        super(message, cause);
    }
}

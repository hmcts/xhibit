package uk.gov.courtservice.xhibit.business.database.results;

// JDK
import java.io.Serializable;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: ResultsDatabaseException
 * </p>
 * <p>
 * Description: Thrown when there is an unrecoverable problem occures in the
 * results database framework.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003)
 * @version 1.0
 */
public class ResultsDatabaseException extends CSUnrecoverableException implements Serializable {
	
	static final long serialVersionUID = 5407729069955588774L;
	
    /**
     * Construct the ResultsDatabaseException with the given message
     * 
     * @param message
     *            describes the cause of the exception
     */
    public ResultsDatabaseException(String message) {
        super(message);
    }

    /**
     * Construct the ResultsDatabaseException with the given cause
     * 
     * @param cause
     *            the cause of the exception
     */
    public ResultsDatabaseException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct the ResultsDatabaseException with the given message and cause
     * 
     * @param message
     *            describes the cause of the exception
     * @param cause
     *            the cause of the exception
     */
    public ResultsDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

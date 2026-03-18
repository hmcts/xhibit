package uk.gov.courtservice.framework.business.exceptions;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: Court not found exception class.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p/> Thrown when the configuration classes cannot find the court referred to.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class CourtNotFoundException extends CSUnrecoverableException {

    /**
     * Basic Constructor.
     */
    public CourtNotFoundException() {
        super();
    }

    /**
     * Complex constructor.
     * 
     * @param message
     *            A message explaining the exception.
     * @param cause
     *            The root cause of the problem.
     */
    public CourtNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Complex constructor.
     * 
     * @param courtId
     *            Court id.
     * @param cause
     *            The root cause of the problem.
     */
    public CourtNotFoundException(Integer courtId, Throwable cause) {
        super("Court not found with id " + courtId, cause);
    }

}
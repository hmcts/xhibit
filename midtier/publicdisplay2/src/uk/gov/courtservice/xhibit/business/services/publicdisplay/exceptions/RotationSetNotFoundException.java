package uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: Display not found exception class.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Thrown when the configuration classes cannot find the rotation set referred
 * to.
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
public class RotationSetNotFoundException extends CSUnrecoverableException {
	
	static final long serialVersionUID = 2532290717561924646L;

    /**
     * Basic Constructor.
     */
    public RotationSetNotFoundException() {
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
    public RotationSetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
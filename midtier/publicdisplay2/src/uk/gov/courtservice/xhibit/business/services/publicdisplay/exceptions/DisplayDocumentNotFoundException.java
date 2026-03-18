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
 * Thrown when the configuration classes cannot find the display referred to.
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
public class DisplayDocumentNotFoundException extends CSUnrecoverableException {
	
	static final long serialVersionUID = 6139059837975951200L;

    /**
     * Basic Constructor.
     */
    public DisplayDocumentNotFoundException() {
        super();
    }

    /**
     * Complex constructor.
     * 
     * @param displayDocumentId
     *            A message explaining the exception.
     * @param cause
     *            The root cause of the problem.
     */
    public DisplayDocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Complex constructor.
     * 
     * @param message
     *            Display document id.
     * @param cause
     *            The root cause of the problem.
     */
    public DisplayDocumentNotFoundException(Integer displayDocumentId, Throwable cause) {
        super("Display document not found with id:" + displayDocumentId, cause);
    }

}

package uk.gov.courtservice.xhibit.client.actions;

import uk.gov.courtservice.framework.exception.CSConfigurationException;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class ActionNotFoundException extends CSConfigurationException {

    public ActionNotFoundException() {
        super();
    }

    /**
     * Extended constructor
     * 
     * @param errorMessage
     *            error message (for logging)
     */
    public ActionNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Constructor for exception chaining
     * 
     * @param cause
     *            original exception caught
     */
    public ActionNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Extended constructor supporting exception chaining
     * 
     * @param errorMsg
     *            error message (for logging)
     * @param cause
     *            original caught exception
     */
    public ActionNotFoundException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

}
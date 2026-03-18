package uk.gov.courtservice.xhibit.business.exceptions.email;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: E-Mail Exception
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * The exception thrown when there is a problem in the e-mail functionality.
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

public class EmailException extends CSRecoverableException {

    /**
     * Creates an instance of an email exception
     * 
     * @param ex
     *            the originating exception.
     */
    public EmailException(Throwable ex) {
        super("EMAIL_001", "There was a problem with preparing the email for sending", ex);
    }
}
package uk.gov.courtservice.xhibit.client.print.exception;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: Xhibit2 CreateDocFailedException
 * </p>
 * <p>
 * Description: Exception thrown when the FOPInterface.createDocument fails.
 * Extends CSRecoverableException
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class FOPCreateDocumentException extends CSRecoverableException {

    /**
     * Contructor with a string argument
     * 
     * @param s
     *            String containing appropriate message
     */
    public FOPCreateDocumentException(String s) {
        super("Exception thrown creating FOP Document", s);
    }

    /**
     * Constructor with a throwable argument
     * 
     * @param t
     *            Throwable
     */
    public FOPCreateDocumentException(Throwable t) {
        super("Exception thrown creating FOP Document", "Exception thrown creating FOP Document", t);
    }
}
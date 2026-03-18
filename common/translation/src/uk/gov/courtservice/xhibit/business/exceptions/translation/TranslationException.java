package uk.gov.courtservice.xhibit.business.exceptions.translation;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: TranslationException
 * </p>
 * <p>
 * Description: Thrown by the Translation when an error occurs.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: TranslationException.java,v 1.3 2006/06/05 12:28:29 bzjrnl Exp $
 */
public class TranslationException extends CSUnrecoverableException {
    private static final long serialVersionUID = 1L;

    /**
     * Construct a new TranslationException with the specified cause.
     */
    public TranslationException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct a new TranslationException with the specified message and
     * cause.
     */
    public TranslationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Construct a new TranslationException with the specified message and
     * cause.
     */
    public TranslationException(String msg) {
        super(msg);
    }
}
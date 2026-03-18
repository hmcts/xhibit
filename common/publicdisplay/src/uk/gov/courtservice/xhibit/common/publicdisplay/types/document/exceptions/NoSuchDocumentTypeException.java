package uk.gov.courtservice.xhibit.common.publicdisplay.types.document.exceptions;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.Fatal;
import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.4 $
 */
public class NoSuchDocumentTypeException extends PublicDisplayRuntimeException implements Fatal {
    
	static final long serialVersionUID = 6356115104060449596L;
	
	/**
     * Creates a new NoSuchDocumentTypeException object.
     * 
     * @param documentId
     *            the string representation of the document type for which the
     *            is no associated DisplayDocumentType.
     * 
     * @see uk.gov.courtservice.xhibit.common.publicdisplay.types.document.DisplayDocumentType
     */
    public NoSuchDocumentTypeException(final String documentId) {
        super("There is no such document '" + documentId + "'.");
    }

    /**
     * Creates a new NoSuchDocumentTypeException object.
     * 
     * @param message
     *            the message.
     * @param cause
     *            the root cause.
     */
    public NoSuchDocumentTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new NoSuchDocumentTypeException object.
     * 
     * @param cause
     *            the root cause.
     */
    public NoSuchDocumentTypeException(final Throwable cause) {
        super(cause);
    }
}

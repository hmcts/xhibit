package uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv;

import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.RetrievalException;

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
 * @version $Revision: 1.3 $
 */
public class FileRetrievalException extends RetrievalException {
    /**
     * Failed to retrieve a file.
     * 
     * @param message
     *            the message.
     * @param cause
     *            the nested Throwable.
     */
    public FileRetrievalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

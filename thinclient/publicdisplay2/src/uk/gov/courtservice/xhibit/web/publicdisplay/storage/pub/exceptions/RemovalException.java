package uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions;

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
public class RemovalException extends PublicDisplayRuntimeException {
    /**
     * Creates a new RemovalException object.
     * 
     * @param s
     *            the message.
     */
    public RemovalException(String s) {
        super(s);
    }

    /**
     * Creates a new RemovalException object.
     * 
     * @param throwable
     *            the root cause.
     */
    public RemovalException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Creates a new RemovalException object.
     * 
     * @param s
     *            the message.
     * @param throwable
     *            the root cause.
     */
    public RemovalException(String s, Throwable throwable) {
        super(s, throwable);
    }
}

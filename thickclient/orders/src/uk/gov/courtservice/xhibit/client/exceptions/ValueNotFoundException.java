package uk.gov.courtservice.xhibit.client.exceptions;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * This exception is used to indicate that a value referenced within a data
 * model was not found.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class ValueNotFoundException extends CSRecoverableException {
    /**
     * @param s
     *            The message to use.
     * @see CSRecoverableException
     * 
     */
    public ValueNotFoundException(String s) {
        super("orders_***", s);
    }
}

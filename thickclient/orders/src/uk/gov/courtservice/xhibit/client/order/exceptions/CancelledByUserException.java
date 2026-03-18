package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: CancelledByUserException
 * </p>
 * <p>
 * Description: Exception thrown when an Orders dilaog is cancelled by the user
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class CancelledByUserException extends CSRecoverableException {
    private static final String ERROR_KEY = "ORDERS_XXX";

    public CancelledByUserException(String s) {
        super(ERROR_KEY, s);
    }

    public CancelledByUserException(Throwable t) {
        super(ERROR_KEY, ERROR_KEY, t);
    }

}
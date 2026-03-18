package uk.gov.courtservice.xhibit.business.services.exiss.itemtracking;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: ItemTrackingException
 * </p>
 * <p>
 * Description: Thrown when an error occures in item tracking
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 */
public class ItemTrackingException extends CSUnrecoverableException {
    private static final long serialVersionUID = 1L;

    public ItemTrackingException(String message) {
        super(message);
    }
    
    public ItemTrackingException(Throwable cause) {
        super(cause);
    }
    
    public ItemTrackingException(String message, Throwable cause) {
        super(message, cause);
    }
}

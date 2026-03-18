package uk.gov.courtservice.xhibit.business.services.contactdetail;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title:
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
 * @author Chris Kudzin
 * @version 1.0
 */

public class ContactDetailControllerException extends CSBusinessException {
    public ContactDetailControllerException(String errorKey, String logMessage, Throwable e) {
        super(errorKey, logMessage, e);
    }
}
package uk.gov.courtservice.xhibit.business.services.defendantreference;

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
 */

public class DefendantReferenceControllerException extends CSBusinessException {
    public DefendantReferenceControllerException(String errorKey, String logMessage, Throwable e) {
        super(errorKey, logMessage, e);
    }
}
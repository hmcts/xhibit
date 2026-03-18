package uk.gov.courtservice.xhibit.business.services.caseprosecutoragency;

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
 * Company: CGI
 * </p>
 * 
 * @author Chris Kudzin
 * @version 1.0
 */

public class CaseProsecutorAgencyControllerException extends CSBusinessException {
    public CaseProsecutorAgencyControllerException(String errorKey, String logMessage, Throwable e) {
        super(errorKey, logMessage, e);
    }
}
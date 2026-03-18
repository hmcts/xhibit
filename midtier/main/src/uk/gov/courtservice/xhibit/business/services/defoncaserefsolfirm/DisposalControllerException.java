package uk.gov.courtservice.xhibit.business.services.defoncaserefsolfirm;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */

public class DisposalControllerException extends CSBusinessException {
    public DisposalControllerException(String errorKey, String logMessage, Throwable e) {
        super(errorKey, logMessage, e);
    }
}
package uk.gov.courtservice.xhibit.business.services.prosecutorrefsolfirm;

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
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class ProsecutorRefSolFirmControllerException extends CSBusinessException {
    public ProsecutorRefSolFirmControllerException(String errorKey, String logMessage, Throwable e) {
        super(errorKey, logMessage, e);
    }
}
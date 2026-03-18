package uk.gov.courtservice.xhibit.business.services.defendant;

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

public class DefendantControllerException extends CSBusinessException {
	
	static final long serialVersionUID = -1336588505225463018L;
	
    public DefendantControllerException(String errorKey, String logMessage, Throwable e) {
        super(errorKey, logMessage, e);
    }
}
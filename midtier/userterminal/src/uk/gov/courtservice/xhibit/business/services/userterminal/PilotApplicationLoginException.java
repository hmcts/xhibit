package uk.gov.courtservice.xhibit.business.services.userterminal;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.xhibit.business.vos.services.version.ComponentValue;

/**
 * <p>
 * Title: Version not found exception
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Deepak R
 * @version $Id: VersionValueNotFoundException.java,v 1.1 2004/09/08 15:41:12
 *          sz0t7n Exp $
 */

public class PilotApplicationLoginException extends UserLoggedInElsewhereException {

	private static final long serialVersionUID = 1L;
    
    private static final String ERROR_KEY = "gui.user.loginFailed";
    
    private static final String PILOT_COURT_ERROR_MSG = "Application login is not possible at this time. CREST version of the application can  only be connected using existing thick client";

    /**
     * Complex constructor.
     * 
     * @param message
     *            A message explaining the exception.
     * @param cause
     *            The root cause of the problem.
     */
    public PilotApplicationLoginException() {
        super(ERROR_KEY, PILOT_COURT_ERROR_MSG);
    }

    
}
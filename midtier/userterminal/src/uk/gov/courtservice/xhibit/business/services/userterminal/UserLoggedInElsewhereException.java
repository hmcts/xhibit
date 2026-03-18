package uk.gov.courtservice.xhibit.business.services.userterminal;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title: UserLoggedInElsewhereException
 * </p>
 * <p>
 * Description: Exception which is thrown when a login is attempted by a user 
 * who is already logged in at another machine
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class UserLoggedInElsewhereException extends CSBusinessException{
    private static final long serialVersionUID = 1L;
    
    private static final String USER_LOGGED_IN_ELSEWHERE = "gui.user.loginFailedUserTerminal";

    public UserLoggedInElsewhereException(String userId,String terminalName, String terminalLocation, String loginDate, String lastUpdateDate) {
        super(USER_LOGGED_IN_ELSEWHERE, new Object[] {terminalName, terminalLocation, loginDate, lastUpdateDate}, "User is logged in at another terminal: " + userId);
    }
    
    public UserLoggedInElsewhereException(String errorKey,String errorMessage) {
    	super(errorKey, errorMessage);
    }
}

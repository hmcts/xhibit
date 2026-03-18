package uk.gov.courtservice.xhibit.web.action;

import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: UpdateTerminalCookieAction
 * </p>
 * <p>
 * Description: This action sets the terminal name in the terminal cookie
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell, Xdevelopment LLP (2004)
 * @version $Revision: 1.4 $
 */
public class UpdateTerminalCookieAction extends AbstractAction {
    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) {
        Object parameter = actionEnvironment.getRequestParameter("terminalName");
        if (parameter != null && parameter instanceof String) {
            TerminalCookieAction.setTerminalCookie(actionEnvironment, (String) parameter);
        }
        actionEnvironment.setResponseName("updateterminalcomplete");
    }
}

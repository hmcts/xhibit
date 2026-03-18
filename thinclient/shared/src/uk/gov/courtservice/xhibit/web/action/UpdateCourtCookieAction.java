package uk.gov.courtservice.xhibit.web.action;

import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: UpdateCourtCookieAction
 * </p>
 * <p>
 * Description: This action sets the court id in the court cookie
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell, Xdevelopment LLP (2004)
 * @version $Revision: 1.6 $
 */
public class UpdateCourtCookieAction extends AbstractAction {
    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) {
        Object parameter = actionEnvironment.getRequestParameter("courtid");
        if (parameter != null && parameter instanceof String) {
            actionEnvironment.setSessionParameter(UserTerminalProperties.COURT_ID.toString(), Integer
                    .valueOf((String) parameter));
        }
        actionEnvironment.setResponseName("updatecourtcomplete");
    }
}

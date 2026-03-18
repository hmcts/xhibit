package uk.gov.courtservice.xhibit.web.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: Logout Action
 * </p>
 * <p>
 * Description: The logout action for the application.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.4 $ $Log:
 *         LogoutAction.java,v $ Revision 1.2 2003/08/01 12:05:58 bzw8gp Jon
 *         Powell
 * 
 * organise imports
 * 
 * Revision 1.1 2003/05/16 08:59:40 fz0n8j Added logout button.
 * 
 * 
 */
public class LogoutAction extends AbstractAction {

    /**
     * The log4j logger
     */

    private static final Logger log = CSServices.getLogger(LogoutAction.class);

    /**
     * Empty default constructor
     */
    public LogoutAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) {
        // log.debug("******************* Remote host : " +
        // actionEnvironment.getRemoteHost());
        actionEnvironment.logout();
        actionEnvironment.setResponseName("loggedout");
    }
}

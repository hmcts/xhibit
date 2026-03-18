package uk.gov.courtservice.xhibit.web.wf.action;

import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: Default Action
 * </p>
 * <p>
 * Description: The default action for the application.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003)
 * @version $Id: DefaultAction.java,v 1.5 2006/06/05 12:32:38 bzjrnl Exp $
 */
public class DefaultAction extends TerminalCookieAction {

    /**
     * Empty default constructor
     */
    public DefaultAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void terminalPerformAction(ActionEnvironment actionEnvironment) {
        actionEnvironment.setResponseName("home");
    }
}

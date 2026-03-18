package uk.gov.courtservice.xhibit.web.ps.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
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
 * @author William Fardell, Xdevelopment LLP (2003)
 * @version $Id: DefaultAction.java,v 1.16 2006/06/05 12:32:25 bzjrnl Exp $
 */
public class DefaultAction extends TerminalCookieAction {

    /**
     * The log4j logger
     */

    private static final Logger log = CSServices.getLogger(DefaultAction.class);

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
        // log.debug("******************* Remote host : " +
        // actionEnvironment.getRemoteHost());
        actionEnvironment.setResponseName("home");
    }
}

package uk.gov.courtservice.xhibit.web.dr.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: DefaultAction
 * </p>
 * <p>
 * Description: The default action for the data reset application
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version $Revision: 1.1 $
 */
public class DefaultAction extends TerminalCookieAction // CourtSiteCookieAction
{
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
        actionEnvironment.setResponseName("home");
    }
}
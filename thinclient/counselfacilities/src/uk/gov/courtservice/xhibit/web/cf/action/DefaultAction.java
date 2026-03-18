package uk.gov.courtservice.xhibit.web.cf.action;

//import uk.gov.courtservice.xhibit.web.action.CourtSiteCookieAction;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: DefaultAction
 * </p>
 * <p>
 * Description: The default action for the application shows the legal
 * representative screen
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell, Xdevelopment (2004)
 * @version $Revision: 1.15 $
 */
public class DefaultAction extends TerminalCookieAction // CourtSiteCookieAction
{
    /**
     * Set up details for identify legal representative screen
     * 
     * @param actionEnvironment
     */
    public void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        actionEnvironment.setResponseName("identifylegrep");
        actionEnvironment.setSessionParameter("sortColStg", "");
        actionEnvironment.setSessionParameter("onlyRoom", "");
    }
}
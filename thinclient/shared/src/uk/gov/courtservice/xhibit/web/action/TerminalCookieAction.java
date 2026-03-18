package uk.gov.courtservice.xhibit.web.action;

import java.security.Principal;

import javax.security.auth.Subject;

import uk.gov.courtservice.framework.client.SessionPropertiesMap;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.xhibit.business.services.userterminal.TerminalNotFoundException;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.terminal.CourtSummaryFactory;
import uk.gov.courtservice.xhibit.business.terminal.TerminalSummaryFactory;
import uk.gov.courtservice.xhibit.business.terminal.interfaces.CourtSummary;
import uk.gov.courtservice.xhibit.business.terminal.interfaces.TerminalSummary;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNameConflictException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;
import weblogic.security.spi.WLSGroup;

/**
 * <p>
 * Title: CourtCookieAction
 * </p>
 * <p>
 * Description: This abstract action is extended by actions which need the
 * terminal name and or court id, it sets the court id and terminal name in a
 * two stage process
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell, Xdevelopment LLP (2004)
 * @version $Revision: 1.12 $
 */
public abstract class TerminalCookieAction extends CourtCookieAction {
    /**
     * Name for query string parameter that holds the computer name
     */
    private static final String COMPUTER_NAME = "computername";

    /**
     * Implementation of Abstract action method to ensure the court id and
     * terminal name are set before terminalPerformAction is called.
     */
    protected final void internalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (isSessionInitialised(actionEnvironment)) {
            terminalPerformAction(actionEnvironment);
        } else {
            String computerName = null;

            try {
                computerName = ((String) actionEnvironment.getRequestParameter(COMPUTER_NAME)).toLowerCase();
                // Computer name received from query string.
                // Update the cookie with this terminal name
                setTerminalCookie(actionEnvironment, computerName);
            } catch (ParameterNotFoundException ex) {
                // Ignore this exception and continue to try and find the
                // computer name from another source
            }
            if (computerName == null && hasTerminalName(actionEnvironment)) {
                computerName = getTerminalName(actionEnvironment);
            }

            if (computerName != null && computerName.trim().length() > 0) {
                SessionPropertiesMap terminal = null;
                try {
                    terminal = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                            .getXHIBITTerminalLocation(computerName);

                    if (((String) terminal.get(UserTerminalProperties.ROAMING)).equalsIgnoreCase("Y")) {
                        // Terminal roaming is not allowed so clear out the
                        // cookie and
                        // throw an error.
                        actionEnvironment.setCookie(UserTerminalProperties.TERMINAL_NAME.toString(), "");
                        throw new CSUnrecoverableException(new Message("terminal.roaming.thinclient.notallowed",
                                new Object[] {}), "Access to thin client with a roaming terminal name:\""
                                + computerName + "\"");
                    }
                    // Create session using terminal info
                    initialiseSessionParameters(actionEnvironment, terminal);
                    terminalPerformAction(actionEnvironment);
                } catch (TerminalNotFoundException ex) {
                    // terminal not found in table so revert back to the
                    // user
                    // supplying info.
                    userSelectsTerminal(actionEnvironment);
                }
            } else {
                userSelectsTerminal(actionEnvironment);
            }
        }
    }

    /**
     * Using the terminal value initialise the session parameters: CourtId,
     * CourtSiteId, CourtRoomId (if available), CourtName (displayName), and
     * Location
     * 
     * @param actionEnvironment
     * @param terminal
     * @throws ParameterNameConflictException
     * @throws IllegalArgumentException
     */
    protected void initialiseSessionParameters(ActionEnvironment actionEnvironment, SessionPropertiesMap terminal)
            throws ParameterNameConflictException, IllegalArgumentException {
        super.initialiseSession(actionEnvironment, terminal);
        actionEnvironment.setSessionParameter(UserTerminalProperties.COURT_SITE_ID.toString(), (Integer) terminal.get(UserTerminalProperties.COURT_SITE_ID));
        if (terminal.get(UserTerminalProperties.COURT_ROOM_ID) != null) {
            actionEnvironment.setSessionParameter(UserTerminalProperties.COURT_ROOM_ID.toString(), (Integer) terminal.get(UserTerminalProperties.COURT_ROOM_ID));
        }
        actionEnvironment.setSessionParameter(UserTerminalProperties.TERMINAL_NAME.toString(), (String) terminal.get(UserTerminalProperties.TERMINAL_NAME));
        actionEnvironment.setSessionParameter(UserTerminalProperties.COURT_NAME.toString(), (String) terminal.get(UserTerminalProperties.COURT_NAME));
        actionEnvironment.setSessionParameter(UserTerminalProperties.TERMINAL_LOCATION.toString(), (String) terminal.get(UserTerminalProperties.TERMINAL_LOCATION));
        actionEnvironment.setSessionParameter(UserTerminalProperties.TERMINAL_ID.toString(), (Integer) terminal.get(UserTerminalProperties.TERMINAL_ID));
        if (terminal.get(UserTerminalProperties.DISPLAY_NAME) != null) {
            actionEnvironment.setSessionParameter(UserTerminalProperties.DISPLAY_NAME.toString(), terminal.get(UserTerminalProperties.DISPLAY_NAME).toString());
        } else {
            actionEnvironment.setSessionParameter(UserTerminalProperties.DISPLAY_NAME.toString(), "ThinClient");
        }
        
        // Is user an admin or cps user
        actionEnvironment.setSessionParameter("isCPS", "false");
        actionEnvironment.setSessionParameter("isAdmin", "false");
        
        Subject s = weblogic.security.Security.getCurrentSubject();
        if (s != null) {
            for (Principal p: s.getPrincipals()) {
                if (p instanceof WLSGroup) {
                    System.out.println("TerminalCookieAction, found group: " + p.getName());
                    if (p.getName().toLowerCase().indexOf("cps") != -1) { // not found
                        actionEnvironment.setSessionParameter("isCPS", "true");
                    } else if (p.getName().toLowerCase().indexOf("admin") != -1) { // not found
                        actionEnvironment.setSessionParameter("isAdmin", "true");
                    }
                }
            }
        }
    }

    private boolean isSessionInitialised(ActionEnvironment actionEnvironment) {
        try {
            return (actionEnvironment.getSessionParameter(UserTerminalProperties.TERMINAL_NAME.toString()) != null);
        } catch (IllegalArgumentException ex) {
            return false;
        } catch (ParameterNotFoundException ex) {
            return false;
        }
    }

    private void userSelectsTerminal(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (!hasCourtId(actionEnvironment)) {
            selectCourtAction(actionEnvironment);
        } else {
            if (!hasTerminalName(actionEnvironment)) {
                selectTerminalCookieAction(actionEnvironment);
            } else {
                terminalPerformAction(actionEnvironment);
            }
        }
    }

    // Select the court from the list
    private void selectCourtAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        CourtSummary[] courts = CourtSummaryFactory.getInstance().getCourtSummaries();
        if (courts.length > 0) {
            actionEnvironment.setRequestParameter("courts", courts);
        }
        actionEnvironment.setResponseName("selectcourt");
    }

    // Select a terminal from the list for the court, assumes the court
    // cookie has been set!
    private void selectTerminalCookieAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        TerminalSummary[] terminals = TerminalSummaryFactory.getInstance().getTerminalSummaries(
                getCourtId(actionEnvironment));
        if (terminals.length > 0) {
            actionEnvironment.setRequestParameter("terminals", terminals);
        }
        actionEnvironment.setResponseName("selectterminal");
    }

    /**
     * @return true if the terminal name has been stored in the terminal cookie.
     */
    public boolean hasTerminalName(ActionEnvironment actionEnvironment) {
        return getTerminalName(actionEnvironment) != null;
    }

    /**
     * Get the terminal name from the terminal cookie
     * 
     * @return the terminal name from the cookie or null if it has not been set
     */
    public String getTerminalName(ActionEnvironment actionEnvironment) {
        return actionEnvironment.getCookie(UserTerminalProperties.TERMINAL_NAME.toString());
    }

    /**
     * @return true if the terminal name has been stored in the terminal cookie.
     */
    public boolean hasCourtSiteId(ActionEnvironment actionEnvironment) {
        return getCourtSiteId(actionEnvironment) != null;
    }

    /**
     * Get the terminal name from the terminal cookie
     * 
     * @return the terminal name from the cookie or null if it has not been set
     */
    public Integer getCourtSiteId(ActionEnvironment actionEnvironment) {
        try {
            return (Integer) actionEnvironment.getSessionParameter(UserTerminalProperties.COURT_SITE_ID.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        } catch (ParameterNotFoundException ex) {
            return null;
        }
    }

    /**
     * @return true if the terminal name has been stored in the terminal cookie.
     */
    public boolean hasCourtRoomId(ActionEnvironment actionEnvironment) {
        return getCourtRoomId(actionEnvironment) != null;
    }

    /**
     * Get the terminal name from the terminal cookie
     * 
     * @return the terminal name from the cookie or null if it has not been set
     */
    public Integer getCourtRoomId(ActionEnvironment actionEnvironment) {
        try {
            return (Integer) actionEnvironment.getSessionParameter(UserTerminalProperties.COURT_ROOM_ID.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        } catch (ParameterNotFoundException ex) {
            return null;
        }
    }

    /**
     * @return true if the terminal name has been stored in the terminal cookie.
     */
    public boolean hasCourtName(ActionEnvironment actionEnvironment) {
        return getCourtName(actionEnvironment) != null;
    }

    /**
     * Get the terminal name from the terminal cookie
     * 
     * @return the terminal name from the cookie or null if it has not been set
     */
    public String getCourtName(ActionEnvironment actionEnvironment) {
        try {
            return (String) actionEnvironment.getSessionParameter(UserTerminalProperties.COURT_NAME.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        } catch (ParameterNotFoundException ex) {
            return null;
        }
    }

    /**
     * @return true if the terminal name has been stored in the terminal cookie.
     */
    public boolean hasLocation(ActionEnvironment actionEnvironment) {
        return getLocation(actionEnvironment) != null;
    }

    /**
     * Get the location from the session
     * 
     * @return the terminal location from the session or null if it has not been
     *         set
     */
    public String getLocation(ActionEnvironment actionEnvironment) {
        try {
            return (String) actionEnvironment.getSessionParameter(UserTerminalProperties.TERMINAL_LOCATION.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        } catch (ParameterNotFoundException ex) {
            return null;
        }
    }

    public static final void setTerminalCookie(ActionEnvironment actionEnvironment, String terminalName) {
        actionEnvironment.setCookie(UserTerminalProperties.TERMINAL_NAME.toString(), terminalName.toLowerCase());
    }

    /**
     * This method replaces internalPerformAction for derived classes which want
     * to ensure the cookies have been set prior to exection.
     */
    public abstract void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException;
}

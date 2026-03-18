package uk.gov.courtservice.xhibit.web.messaging.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;
import uk.gov.courtservice.xhibit.web.messaging.IMSessionInfo;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class DefaultAction extends TerminalCookieAction {
    private Logger log = CSServices.getLogger(DefaultAction.class);

    private static final String XHB_JMS_ROOT = "jms/im/";

    public void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        try {
            actionEnvironment.getSessionParameter(IMSessionInfo.INSTANT_MESSAGE_SESSION);
        } catch (ParameterNotFoundException ex) {
            String location = getLocation(actionEnvironment);

            Integer courtId = getCourtId(actionEnvironment);
            log.debug("***courtId: " + courtId);
            String courtName = getCourtName(actionEnvironment);

            // Collection courts = srcbd.findCourts();
            String destinationRootNode = formatNodeName(courtName);

            log.debug("***destinationRootNode: " + destinationRootNode);
            log.debug("***location: " + location);

            String formatedLocation = formatNodeName(location);
            log.debug("***formated location: " + formatedLocation);

            // Will need to dynamically populate this lot...

            log.debug("Initialising new IM Session Info.");

            IMSessionInfo imsi = new IMSessionInfo(getTerminalName(actionEnvironment), destinationRootNode, location,
                    formatedLocation);
            log.debug("Done initialising new IM Session Info.");
            actionEnvironment.setSessionParameter(IMSessionInfo.INSTANT_MESSAGE_SESSION, imsi);
        }
        actionEnvironment.setResponseName("home");
    }

    /**
     * Replaces spaces with underscores for display as a tree node and applies
     * appropriate prefix.
     * 
     * @param s
     *            String to format
     * @return formated string
     */
    public static String formatNodeName(String s) {
        String returnString = s.toLowerCase().replace(' ', '_');
        if (s.startsWith("/"))
            returnString = XHB_JMS_ROOT + returnString.substring(1);
        else
            returnString = XHB_JMS_ROOT + returnString;

        return returnString;
    }

    /**
     * strip out the court name (jms/im/xxxxxxx) from full location
     * 
     * @param name
     *            the location
     * @return the jms court name
     */
    private String formatNodeNameForCourt(String name) {
        log.debug("<<<>>> DefaultAction.formatNodeNameForCourt name " + name);
        final String SLASH_DELIM = "/";
        int start = 0;

        start = name.indexOf(XHB_JMS_ROOT);
        log.debug("<<<>>> DefaultAction.formatNodeNameForCourt start " + start);

        int end = name.indexOf(SLASH_DELIM, XHB_JMS_ROOT.length());
        log.debug("<<<>>> DefaultAction.formatNodeNameForCourt end " + end);
        return name.substring(start, end);
    }
}

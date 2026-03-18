package uk.gov.courtservice.xhibit.client.im.helper;

import java.util.StringTokenizer;

import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.im.actions.IMSenderAction;
import uk.gov.courtservice.xhibit.client.im.util.IMResourceHelper;
import uk.gov.courtservice.xhibit.client.im.util.IMStringFormatter;
import uk.gov.courtservice.xhibit.client.im.util.JMSContextNode;
import uk.gov.courtservice.xhibit.client.im.util.NodeFormat;
import uk.gov.courtservice.xhibit.client.im.util.TopicNode;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: IMLocationHelper
 * </p>
 * <p>
 * Description: Utility class to retrieve information on the current session
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class IMLocationHelper {
    private static final String STR_UNDERSCORE = "_";

    private static final String STR_SLASH = "_";

    private static final String IM_JNDI_PREFIX = "jms/im/";

    private static final String AT_DELIM = "@";

    private static final char CHAR_SPACE = ' ';

    private static final char CHAR_UNDERSCORE = STR_UNDERSCORE.charAt(0);

    private static final char CHAR_SLASH = STR_SLASH.charAt(0);

    private static XhibitSingleton xSingleton = XhibitSingleton.getInstance();

    private static Logger log = CSServices.getLogger(IMLocationHelper.class);

    /**
     * Retrieves the fully qualified topic id
     * 
     * @return String The topic ID
     */
    public static String getTopicID() throws CSRecoverableException {
        String locationID = getLocationID();
        return getTopicIDForLocation(locationID);
    }

    /**
     * Retrieves the fully qualified topic id
     * 
     * @return String The topic ID
     */
    public static String getTopicIDForCourt() throws CSRecoverableException {
        String locationID = getLocationID();
        return formatForCourt(getTopicIDForLocation(locationID));
    }

    /**
     * Retrieves the fully qualified topic id for a given location
     * 
     * @param String
     *            The location
     * @return String The topic ID
     */
    public static String getTopicIDForLocation(String locationID) throws CSRecoverableException {
        StringBuffer buf = new StringBuffer();
        buf.append(getRootContext());
        if (locationID.startsWith("/") == false) {
            buf.append("/");
        }
        buf.append(locationID);
        String topicID = IMStringFormatter.formatExtName(buf.toString());
        log.debug("IMLocationHelper: getTopicID: Retrieving topic ID: " + topicID);
        return topicID;
    }

    /**
     * Retrieves the fully qualified topic id for a given location
     * 
     * @param String
     *            The location
     * @return String The topic ID
     */
    public static String getTopicIDForCourt(String locationID) throws CSRecoverableException {
        StringBuffer buf = new StringBuffer();
        buf.append(getRootContext());
        if (locationID.startsWith("/") == false) {
            buf.append("/");
        }
        buf.append(locationID);
        String topicID = IMStringFormatter.formatExtName(buf.toString());
        log.debug("IMLocationHelper: getTopicID: Retrieving topic ID: " + topicID);
        return topicID;
    }

    /**
     * Retrieves the fully qualified topic id
     * 
     * @return String The topic ID
     */
    public static String getExtTopicID() throws CSRecoverableException {
        StringBuffer buf = new StringBuffer();
        buf.append(IMStringFormatter.formatExtName(getTopicID()));
        log.debug("IMLocationHelper: getExtTopicID: Retrieving topic ID: " + buf.toString());
        return buf.toString();
    }

    /**
     * Retrieves the current terminal ID (id + name) for the session
     * 
     * @return String The terminal ID
     */
    public static String getTerminalID() throws CSRecoverableException {
        String terminalID = xSingleton.getUserSession().getSessionProperty(UserTerminalProperties.TERMINAL_ID);
        log.debug("IMLocationHelper: getTerminalID: Retrieving terminal ID: " + terminalID);
        return terminalID + getTerminalName();
    }

    /**
     * Retrieves the current terminal Name for the session
     * 
     * @return String The terminal ID
     */
    public static String getTerminalName() throws CSRecoverableException {
        String terminalName = xSingleton.getUserSession().getSessionProperty(UserTerminalProperties.TERMINAL_NAME);
        log.debug("IMLocationHelper: getTerminalName: Retrieving terminal name: " + terminalName);
        return terminalName;
    }

    /**
     * Retrieves the current location for the session
     * 
     * @return String The location
     */
    public static String getLocationID() throws CSRecoverableException {
        String locationID = xSingleton.getUserSession().getSessionProperty(UserTerminalProperties.TERMINAL_LOCATION);
        log.debug("IMLocationHelper: getLocationID: Retrieving location: " + locationID);
        return locationID;
    }

    /**
     * Retrieves the current location for the session
     * 
     * @return String The location
     */
    public static String getCourtID() throws CSRecoverableException {
        String courtID = xSingleton.getCourtBasicValue().getDisplayName();
        log.debug("IMLocationHelper: getCourtID: Retrieving Court Name: " + courtID);
        return courtID;
    }

    /**
     * DUMMY method Retrieves the current location for the session
     * 
     * @return String The location
     */
    public static String getLocationID(String s) throws CSRecoverableException {
        log.debug("IMLocationHelper: getLocationID: Retrieving location");
        return s;
    }

    /**
     * Returns the starting contact for the JMS lookup
     * 
     * @return String the root context
     */
    public static String getRootContext() {
        String rootContext = IMResourceHelper.getResourceString(IMSenderAction.IM_CONTEXT_ROOT);
        log.debug("IMLocationHelper: getRootContext: Retrieving root context: " + rootContext);
        return rootContext;
    }

    /**
     * method to return the location of the current court
     * 
     * @return String
     */
    public static String getCourtLocation() throws CSRecoverableException {
        String courtLocation = IMStringFormatter.formatWithSlashes(NodeFormat
                .displayNodeName(formatString(getLocationID())))
                + ": ";
        log.debug("IMLocationHelper: getCourtLocation: retrieving Court Location: " + courtLocation);
        return courtLocation;
    }

    /**
     * Format the location
     * 
     * @param s
     *            the location
     * @return the formatted location
     */
    private static String formatString(String s) {
        return replaceSpacesWithUnderscores(s.toLowerCase());
    }

    /**
     * Format the
     * 
     * @param name
     * @return
     */
    private static String formatForCourt(String name) {
        final String SLASH_DELIM = "/";
        int count = 0;
        StringTokenizer nameToken = new StringTokenizer(name, SLASH_DELIM);
        StringBuffer buf = new StringBuffer();
        String tokResult = null;
        while (nameToken.hasMoreTokens() && count < 3) {
            tokResult = nameToken.nextToken();
            buf.append(SLASH_DELIM);
            buf.append(tokResult);
            count++;
        }
        return buf.toString();
    }

    /**
     * Get the selector to monito rthe JMS topic with
     * 
     * @param node
     *            the selected node
     * @return the selector in the format e.g.
     *         isleworth_crown_court_court_site_i_court_room_3
     */
    public static String getMessageSelector(TreeNode node) {
        StringBuffer res = new StringBuffer();
        res.insert(0, node != null ? STR_UNDERSCORE + getNodeName(node) : null);
        TreeNode parent = (TreeNode) node.getParent();
        res.insert(0, (parent != null) ? getMessageSelector(parent) : new String());
        return formatForJMS(res.toString());

    }

    /**
     * Get the destination of the message and format correctly
     * 
     * @param node
     *            the selected node
     * @return the destination of the message in the format e.g.
     *         /jms/im/isleworth_crown_court
     */
    public static String getMessageDestination(TreeNode node) {
        TreeNode parent = (TreeNode) node.getParent();
        return (parent != null) ? getMessageDestination(parent) : IM_JNDI_PREFIX
                + formatForJMS(((JMSContextNode) node).getJndiName().toString());

    }

    /**
     * Format the strings to match the expected JMS selector and destination
     * 
     * @param str
     *            the JMS name
     * @return the formatted name
     */
    private static String formatForJMS(String str) {
        return removePrecedingUnderscore(replaceSpacesWithUnderscores(str).toLowerCase());
    }

    /**
     * Format the location to match the selector format e.g.
     * TopicName='isleworth_crown_court_court_site_i_court_room_3'
     * 
     * @param str
     * @return
     */
    private static String formatForSelector(String str) {
        return removePrecedingUnderscore(replaceSlashesWithUnderscores(str).toLowerCase());
    }

    /**
     * Change any spaces to underscores
     * 
     * @param str
     *            the terminal location
     * @return the location with underscores
     */
    private static String replaceSpacesWithUnderscores(String str) {
        return str.trim().replace(CHAR_SPACE, CHAR_UNDERSCORE);
    }

    /**
     * Change any slashes to underscores
     * 
     * @param str
     *            the terminal location
     * @return the location with underscores
     */
    private static String replaceSlashesWithUnderscores(String str) {
        return str.trim().replace(CHAR_SLASH, CHAR_UNDERSCORE);
    }

    /**
     * Remove the preceding underscore (if present) from the terminal location
     * 
     * @param str
     *            the original string
     * @return the string without a preceding underscore
     */
    private static String removePrecedingUnderscore(String str) {
        return str.startsWith(STR_UNDERSCORE) ? str.substring(str.indexOf(STR_UNDERSCORE) + 1) : str;
    }

    /**
     * Overloaded method to return the name of the node
     * 
     * @param node
     *            a TreeNode
     * @return the node name depending on the TreeNode type
     */
    private static String getNodeName(TreeNode node) {
        if (node instanceof TopicNode) {
            return getNodeName((TopicNode) node);
        } else if (node instanceof JMSContextNode) {
            return getNodeName((JMSContextNode) node);
        } else {
            return null;
        }
    }

    /**
     * Overloaded method to return the name of the node
     * 
     * @param node
     *            A TopicNode
     * @return the Node name
     */
    private static String getNodeName(TopicNode node) {
        return node.getName();
    }

    /**
     * Overloaded method to return the name of the node
     * 
     * @param node
     *            A JMSContextNode
     * @return the Node name
     */
    private static String getNodeName(JMSContextNode node) {
        return node.getJndiName();
    }
}

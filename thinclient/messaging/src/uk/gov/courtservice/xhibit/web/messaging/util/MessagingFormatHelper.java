package uk.gov.courtservice.xhibit.web.messaging.util;

import java.util.StringTokenizer;

import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.util.JMSContextNode;
import uk.gov.courtservice.xhibit.client.im.util.TopicNode;

/**
 * <p>
 * Title: MessagingFormatHelper
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

public class MessagingFormatHelper {

    /**
     * Underscore String literal
     */
    private static final String STR_UNDERSCORE = "_";

    /**
     * Slash String literal
     */
    private static final String STR_SLASH = "/";

    /**
     * The prefix for the JMS topics
     */
    private static final String IM_JNDI_PREFIX = "jms/im/";

    /**
     * Space String literal
     */
    private static final String STR_SPACE = " ";

    /**
     * Space character literal
     */
    private static final char CHAR_SPACE = STR_SPACE.charAt(0);

    /**
     * Underscaore character literal
     */
    private static final char CHAR_UNDERSCORE = STR_UNDERSCORE.charAt(0);

    /**
     * Slash character literal
     */
    private static final char CHAR_SLASH = STR_SLASH.charAt(0);

    /**
     * Space String literal
     */
    private static final String IM_SP_DELIM = " ";

    /**
     * Literal to split the destination and selector
     */
    public static final String DEST_SEL_DELIM = "$";

    /**
     * Logger
     */
    private static Logger log = CSServices.getLogger(MessagingFormatHelper.class);

    /**
     * Determine the appropriate message selector from the selected node
     * 
     * @param node
     *            the selected node
     * @return the jms selector for the node
     */
    public static String getMessageSelector(TreeNode node) {
        StringBuffer res = new StringBuffer();
        res.insert(0, node != null ? STR_UNDERSCORE + getNodeName(node) : null);
        TreeNode parent = (TreeNode) node.getParent();
        res.insert(0, (parent != null) ? getMessageSelector(parent) : new String());
        return formatForJMS(res.toString());

    }

    /**
     * Determine the destination of the massesge given the selected node
     * 
     * @param node
     *            the selected node
     * @return the destination of the message
     */
    public static String getMessageDestination(TreeNode node) {
        TreeNode parent = (TreeNode) node.getParent();
        return (parent != null) ? getMessageDestination(parent) : IM_JNDI_PREFIX
                + formatForJMS(((JMSContextNode) node).getJndiName().toString());

    }

    /**
     * Foramt a location to match the jms name convention i.e
     * im_isleworth_crown_court_ etc
     * 
     * @param str
     *            the location to convert
     * @return the formatted location
     */
    private static String formatForJMS(String str) {
        return removePrecedingUnderscore(replaceSpacesWithUnderscores(str).toLowerCase());
    }

    /**
     * Replace spaces with underscores for a given String
     * 
     * @param str
     *            the string to convert
     * @return the converted string
     */
    private static String replaceSpacesWithUnderscores(String str) {
        return str.trim().replace(CHAR_SPACE, CHAR_UNDERSCORE);
    }

    /**
     * Remove any preceding unbderscores in a string
     * 
     * @param str
     *            the string to convert
     * @return the converted string
     */
    private static String removePrecedingUnderscore(String str) {
        return str.startsWith(STR_UNDERSCORE) ? str.substring(str.indexOf(STR_UNDERSCORE) + 1) : str;
    }

    /*
     * Takes a JNDI node name and changes it to more readable format for
     * display. The first letter of each word is capitalised and underscores
     * replaced with spaces. It used to put a extra space on the end but this
     * wasn't required (checked with NE).
     * 
     * e.g. "court_site_a" -> "Court Site A"
     */
    public static String splitJMSParameter(String name, int tokNum) {
        log.debug("splitJMSParameter name = " + name + " tokNum = " + tokNum);
        StringTokenizer nameToken = new StringTokenizer(name, DEST_SEL_DELIM);
        int limit = nameToken.countTokens();
        String tokResult = null;
        int count = 0;
        while (nameToken.hasMoreTokens() && count < Math.min(tokNum, limit)) {
            tokResult = nameToken.nextToken();
            count++;
        }
        log.debug("tokResult = " + tokResult);
        return tokResult;
    }

    /**
     * Overloaded method to return the name of a TreeNode
     * 
     * @param node
     *            the TreeNode
     * @return the name
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
     * Overloaded method to return the name of a TopicNode
     * 
     * @param node
     *            the TreeNode
     * @return the name
     */
    private static String getNodeName(TopicNode node) {
        return node.getName();
    }

    /**
     * Return the JNDI name from the JMSContextNode
     * 
     * @param node
     *            the node
     * @return the JNDI name of the node
     */
    private static String getNodeName(JMSContextNode node) {
        return node.getJndiName();
    }
}
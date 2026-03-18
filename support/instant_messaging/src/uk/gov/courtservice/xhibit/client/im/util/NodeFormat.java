package uk.gov.courtservice.xhibit.client.im.util;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: NodeFormat
 * </p>
 * <p>
 * Description: Reformats IMS Node Names
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
 * 
 * this class has a unit test - please update it
 */
public class NodeFormat {
    private static Logger log = Logger.getLogger(NodeFormat.class.getName());

    private static final String F_STOP_DELIM = ".";

    private static final String U_SCR_DELIM = "_";

    // stop instantiation
    private NodeFormat() {
    }

    /*
     * Takes a JNDI node name and changes it to more readable format for
     * display. The first letter of each word is capitalised and underscores
     * replaced with spaces. It used to put a extra space on the end but this
     * wasn't required (checked with NE).
     * 
     * e.g. "court_site_a" -> "Court Site A"
     */
    private static String formatNodeName(String name) {
        log.debug("formatNodeName name='" + name + "'");
        name = name.toLowerCase();
        StringTokenizer nameToken = new StringTokenizer(name, U_SCR_DELIM);
        StringBuffer buf = new StringBuffer();
        String tokResult = null;

        while (nameToken.hasMoreTokens()) {
            tokResult = nameToken.nextToken();
            buf.append(tokResult.substring(0, 1).toUpperCase());
            buf.append(tokResult.substring(1));
            buf.append(" ");
        }
        // remove extra space on the end before returning
        buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }

    /**
     * Takes a JNDI node path and strips off name of last node. Added check for
     * null or zero-length node names to avoid NullPointerEx.
     * 
     * e.g. "jms.im.snaresbrook_crown_court.court_site_a" -> "court_site_a"
     * "jms" -> "jms"
     */
    public static String displayNodeName(String name) {
        log.debug("displayNodeName name='" + name + "'");
        if (name == null || name.length() == 0)
            return "";
        StringTokenizer token = new StringTokenizer(name, F_STOP_DELIM);
        String result = null;
        while (token.hasMoreTokens()) {
            result = token.nextToken();
        }
        return formatNodeName(result);
    }

}
package uk.gov.courtservice.xhibit.client.im.util;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: IMStringFormatter
 * </p>
 * <p>
 * Description: Utility class to format strings for display
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

public class IMStringFormatter {
    private static final Logger log = CSServices.getLogger(IMStringFormatter.class);

    private static final String IM_SP_DELIM = " ";

    /**
     * Replaces spaces with underscores for display as a tree node
     * 
     * @param s
     *            String to format
     * @return formated string
     */
    public static String formatTreeNode(String s) {
        return s.toLowerCase().replace(' ', '_');
    }

    /**
     * Replaces spaces with underscores for display as a tree node
     * 
     * @param s
     *            String to format
     * @return formated string
     */
    public static String formatJNDIToName(String s) {
        return s.indexOf("/") > 0 ? s.replace('/', '_') : s.substring(1).replace('/', '_');
    }

    /**
     * Foramts name for display
     * 
     * @param name
     * @return
     */
    public static String formatName(String name) {
        log.debug("$$$ formatName: " + name + " $$$");
        name = name.toLowerCase();
        StringTokenizer nameToken = new StringTokenizer(name, IM_SP_DELIM);
        StringBuffer buf = new StringBuffer();
        String tokResult = null;

        while (nameToken.hasMoreTokens()) {
            tokResult = nameToken.nextToken();
            buf.append(tokResult.substring(0, 1).toUpperCase());
            buf.append(tokResult.substring(1));
            buf.append(" ");
        }
        return buf.toString();
    }

    private static String displayName(String name) {
        StringTokenizer token = new StringTokenizer(name, IM_SP_DELIM);
        String result = null;
        while (token.hasMoreTokens()) {
            result = token.nextToken();
        }

        return formatName(result);
    }

    /**
     * Removes all spaces from a supplied string
     * 
     * @param source
     *            String to be stripped
     * @return String with spaces removed
     */
    public static String stripSpaces(String source) {
        String trimmed = source.trim();
        StringBuffer buf = new StringBuffer(trimmed.length());

        int start = 0;
        int nextSpace = 0;

        while (nextSpace >= 0) {
            nextSpace = trimmed.indexOf(" ", start);

            // if no more spaces nextSpace = -1
            if (nextSpace != -1) {
                // append the substring to the next space
                buf.append(trimmed.substring(start, nextSpace));
            } else {
                // append the rest of the string
                buf.append(trimmed.substring(start));
            }
            start = nextSpace + 1;
        }
        return buf.toString();
    }

    /**
     * Foramts name for display
     * 
     * @param name
     * @return
     */
    public static String formatExtName(String name) {
        name = name.toLowerCase();
        return name.replace(' ', '_');
    }

    /*
     * Takes a JNDI node name and changes it to more readable format for
     * display. The first letter of each word is capitalised and underscores
     * replaced with spaces. It used to put a extra space on the end but this
     * wasn't required (checked with NE).
     * 
     * e.g. "court_site_a" -> "Court Site A"
     */
    public static String formatWithSlashes(String name) {
        final String SLASH_DELIM = "/";
        StringTokenizer nameToken = new StringTokenizer(name, SLASH_DELIM);
        StringBuffer buf = new StringBuffer();
        String tokResult = null;
        while (nameToken.hasMoreTokens()) {
            tokResult = nameToken.nextToken();
            buf.append(SLASH_DELIM);
            buf.append(tokResult.substring(0, 1).toUpperCase());
            buf.append(tokResult.substring(1));
        }
        return buf.toString();
    }
}

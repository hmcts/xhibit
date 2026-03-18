package uk.gov.courtservice.xhibit.web.framework.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title: Primitive Util
 * </p>
 * <p>
 * Description: A bunch of utilities for manipulating primitives and Strings
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.6 $ $Log:
 *         PrimitiveUtil.java,v $ Revision 1.4 2003/12/02 16:15:56 cz4lvy
 *         Updated to handle special characters (&, >, <) when generating XML
 *         strings from normal strings.
 * 
 * Revision 1.3 2003/03/21 11:48:30 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:08 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:44 fz0n8j Added CVS log comments - ecawley
 * 
 */
public class PrimitiveUtil {
    /**
     * A string containing the newline character(s) suitable for use when
     * building up multi line strings
     */
    public static final String NL = System.getProperty("line.separator", "\n");

    /**
     * A string containing the tab character(s) suitable for use when building
     * up complex strings
     */
    public static final String TAB = "    ";

    /**
     * Special characters that need to be converted in order to allow successful
     * transforms
     */
    private static final String specialChars[] = { "&", "<", ">" };

    private static final String xmlReplacement[] = { "&amp;", "&lt;", "&gt;" };

    /**
     * Parses the source using the seperator
     * 
     * @param source
     *            the source to parse
     * @param seperator
     *            the seperator used to parse the source
     * 
     * @return the list of elements
     */
    public static List parseList(String source, String seperator) {
        List list = new ArrayList();

        int startIndex = 0;
        int endIndex = source.indexOf(seperator, startIndex);
        while (endIndex != -1) {
            list.add(source.substring(startIndex, endIndex).trim());
            startIndex = endIndex + seperator.length();
            endIndex = source.indexOf(seperator, startIndex);
        }
        list.add(source.substring(startIndex).trim());

        return list;
    }

    /**
     * Append a new line then the indent the the line to the supplied buffer.
     * 
     * @param buffer
     *            the buffer to append the line to
     * @param indent
     *            the indent to use
     * @param line
     *            the line to append
     */
    public static void appendNewLine(StringBuffer buffer, String indent, String line) {
        buffer.append(PrimitiveUtil.NL);
        buffer.append(indent);
        buffer.append(line);
    }

    /**
     * Converts Special character's codes back to the proper characters.
     */
    public static String convertSpecialChars(String inputString) {
        String tempStr = inputString;
        for (int i = 0; i < specialChars.length; i++) {
            tempStr = (replaceSubstring(tempStr, specialChars[i], xmlReplacement[i]));
        }

        return tempStr;
    }

    /**
     * Replace all occurrences of a pattern in a string
     * 
     * @param str
     *            String to process
     * @param pattern
     *            Pattern to replace
     * @param replace
     *            Replacement string.
     */
    private static String replaceSubstring(String str, String pattern, String replace) {
        int slen = str.length();
        int plen = pattern.length();
        int s = 0, e = 0;

        StringBuffer result = new StringBuffer(slen * 2);
        char[] chars = new char[slen];

        while ((e = str.indexOf(pattern, s)) >= 0) {
            str.getChars(s, e, chars, 0);
            result.append(chars, 0, e - s).append(replace);
            s = e + plen;
        }

        str.getChars(s, slen, chars, 0);
        result.append(chars, 0, slen - s);
        return result.toString();
    }

    /**
     * Stops this class being constructed unnecessarily
     */
    private PrimitiveUtil() {
    }

}

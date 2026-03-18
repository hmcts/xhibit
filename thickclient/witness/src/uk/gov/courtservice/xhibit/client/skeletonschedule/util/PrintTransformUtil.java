/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: May 14, 2003
 * Time: 10:27:50 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.skeletonschedule.util;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.print.FOPInterface;
import uk.gov.courtservice.xhibit.client.print.factory.FOPFactory;
import uk.gov.courtservice.xhibit.client.print.helper.PrintPreviewHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: PrintTransformUtil
 * </p>
 * <p>
 * Description: A bunch of utilities for transforming documents
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle (2003)
 */
public class PrintTransformUtil {

    /**
     * The log logger
     */

    private static final org.apache.log4j.Logger log = CSServices.getLogger(PrintTransformUtil.class);

    /**
     * The date format used in the xml
     */

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    /**
     * The date format for time used in the xml
     */

    private static final SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");

    private static final BasicTranslator tran = new BasicTranslator();

    /**
     * Stops this class being constructed unnecessarily
     */
    private PrintTransformUtil() {
    }

    public static void printTransform(String xslResource, HashMap data, XhibitApplicationController xac)
            throws CSRecoverableException {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<root>");

        sb.append(resolveObject("data", data));
        sb.append("</root>");
        printTransform(xslResource, sb.toString(), xac);
    }

    public static void printTransform(String xslResource, String xml, XhibitApplicationController xac)
            throws CSRecoverableException {
        log.debug("XML String to be passed to print framework : \n");
        log.debug("-----------------------------------------------------------------\n" + xml);
        log.debug("-----------------------------------------------------------------\n");
        printTransform(ResourceUtil.getResourceAsStream(xslResource), CSServices.getXSLServices().transform(xml,
                xslResource, Locale.getDefault(), null), xac);
    }

    public static void printTransform(InputStream xsl, InputStream xml, XhibitApplicationController xac)
            throws CSRecoverableException {
        try {
            Logger log = new ConsoleLogger(ConsoleLogger.LEVEL_DEBUG);

            FOPInterface fop = FOPFactory.getFOPRenderer(true);
            fop.printDocument(xml, xsl);
            if (fop instanceof PrintPreviewHelper) {
                ((PrintPreviewHelper) fop).getFrame().setIconImage(xac.getIconImage());
            }

        } catch (Exception e) {
            log.error("Error transforming document: " + e.getMessage());
            throw new CSRecoverableException(e);
        }
    }

    public static void printTransform(InputStream xsl, String xml, XhibitApplicationController xac)
            throws CSRecoverableException {
        try {
            Logger log = new ConsoleLogger(ConsoleLogger.LEVEL_DEBUG);

            FOPInterface fop = FOPFactory.getFOPRenderer(true);
            fop.printDocument(xml, xsl);
            if (fop instanceof PrintPreviewHelper) {
                ((PrintPreviewHelper) fop).getFrame().setIconImage(xac.getIconImage());
            }

        } catch (Exception e) {
            log.error("Error transforming document: " + e.getMessage());
            throw new CSRecoverableException(e);
        }
    }

    private static String resolveObject(String name, Object o) {
        StringBuffer sb = new StringBuffer();
        if (o instanceof HashMap) {
            sb.append("<" + tran.translate(name) + ">");
            HashMap map = (HashMap) o;
            Iterator i = map.keySet().iterator();
            while (i.hasNext()) {
                String mapName = (String) i.next();
                sb.append(resolveObject(mapName, map.get(mapName)));
            }
            sb.append("</");
            sb.append(tran.translate(name));
            sb.append(">");
        } else if (o instanceof List) {
            List list = (List) o;
            Iterator i = list.iterator();
            while (i.hasNext()) {
                sb.append(resolveObject(name, i.next()));
            }
        } else if (o instanceof Date) {
            sb.append("<");
            sb.append(tran.translate(name));
            sb.append(">");
            sb.append(sdf.format(o));
            sb.append("");
            sb.append("</");
            sb.append(tran.translate(name));
            sb.append(">");
            sb.append("<");
            sb.append(tran.translate(name));
            sb.append("time>");
            sb.append(sdftime.format(o));
            sb.append("");
            sb.append("</");
            sb.append(tran.translate(name));
            sb.append("time>");
        } else if (o instanceof String) {
            sb.append("<");
            sb.append(tran.translate(name));
            sb.append(">");
            sb.append(tran.translate(o.toString()));
            sb.append("");
            sb.append("</");
            sb.append(tran.translate(name));
            sb.append(">");
        } else if (o == null) // put a blank entry in for the moment . . .
        {
            sb.append("<");
            sb.append(tran.translate(name));
            sb.append(">");
            sb.append("");
            sb.append("</");
            sb.append(tran.translate(name));
            sb.append(">");
        } else {
            log.debug("Unknown object type for object resolve : " + name + " object " + o.getClass().getName());
        }
        return sb.toString();
    }
}

/**
 * @author pznwc5
 * 
 * Basic translator
 */
class BasicTranslator {

    private static final String[] specialChars = { "&amp;", "&lt;", "&gt;", "&apos;", "&quot;" };

    // , "&pound;", "&nbsp;",
    // "&euro;", "&#8364;"};

    private static final String[] toBeReplaced = { "&", "<", ">", "'", "\"" }; // ,

    // "£",
    // " ",
    // "\u20ac",
    // "\u20ac"};

    private String xsl;

    /**
     * Applies an xsl template to xml and send result to requested stream.
     * 
     * @throws EDSException
     *             if anything fails.
     */
    public static String translate(final String input) {
        return convertSpecialChars(input);
    }

    /**
     * Converts Special character's codes back to the proper characters.
     */
    private static String convertSpecialChars(final String inputString) {
        String tempStr = inputString;
        for (int i = 0; i < specialChars.length; i++) {
            tempStr = replaceSubstring(tempStr, toBeReplaced[i], specialChars[i]);
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
        final int slen = str.length();
        final int plen = pattern.length();
        final StringBuffer result = new StringBuffer(slen * 2);
        int s = 0, e = 0;
        final char[] chars = new char[slen];
        while ((e = str.indexOf(pattern, s)) >= 0) {
            str.getChars(s, e, chars, 0);
            result.append(chars, 0, e - s).append(replace);
            s = e + plen;
        }
        str.getChars(s, slen, chars, 0);
        result.append(chars, 0, slen - s);
        return result.toString();
    }

}

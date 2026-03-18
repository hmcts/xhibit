package uk.gov.courtservice.xhibit.web.messaging;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import uk.gov.courtservice.framework.util.Contract;

/**
 * <p/> Title: Messaging utility class.
 * </p>
 * <p/> Description:
 * </p>
 * <p/> This class holds useful utility methods for thin client messaging
 * functionality.
 * </p>
 * <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 * @history updated by Kevin Buckthorpe 30 Sept 2003. Added method: public
 *          static String getHTMLFormattedString(String toBeFormatted, int
 *          maxCols )
 */
public class Utility {

    /**
     * Utility method that parses an incoming message into a format suitable for
     * HTML. At the moment, it only replaces CR/LF with <br/> tags.
     * 
     * @param toBeFormatted
     *            The string to be formatted.
     * @return The formatted string.
     */
    public static String getHTMLFormattedString(String toBeFormatted) {
        StreamTokenizer streamTokenizer = new StreamTokenizer(new StringReader(toBeFormatted));
        streamTokenizer.resetSyntax();
        streamTokenizer.wordChars(0, 9);
        streamTokenizer.wordChars(11, 12);
        streamTokenizer.wordChars(14, 65535);
        streamTokenizer.eolIsSignificant(true);
        StringBuffer returnBuffer = new StringBuffer();
        try {
            while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                if (streamTokenizer.ttype == StreamTokenizer.TT_EOL)
                    returnBuffer.append("<br/>");
                else if (streamTokenizer.ttype == StreamTokenizer.TT_WORD)
                    returnBuffer.append(streamTokenizer.sval);
            }
        } catch (IOException ex) {
            // Will never happen coming from a string.
            Contract.fail(ex);
        }
        return returnBuffer.toString();
    }

    /**
     * Utility method that parses an incoming message into a format suitable for
     * HTML. It replaces CR/LF with <br/> tags and will start a new line if the
     * number of characters in the next word exceeds the maxCols specified
     * 
     * @param toBeFormatted
     *            The string to be formatted
     * @param maxCols
     *            number of characters permitted on a line
     * @return the formatted string
     */
    public static String getHTMLFormattedString(String toBeFormatted, int maxCols) {
        StreamTokenizer streamTokenizer = new StreamTokenizer(new StringReader(toBeFormatted));
        streamTokenizer.resetSyntax();
        streamTokenizer.wordChars(0, 9);
        streamTokenizer.wordChars(11, 12);
        streamTokenizer.wordChars(14, 65535);
        streamTokenizer.whitespaceChars(32, 32);
        streamTokenizer.eolIsSignificant(true);
        StringBuffer returnBuffer = new StringBuffer();
        StringBuffer lineBuffer = new StringBuffer();
        int colCount = 0;

        try {
            while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                if (streamTokenizer.ttype == StreamTokenizer.TT_EOL) {
                    returnBuffer.append("<br/>");
                    colCount = 0;
                    lineBuffer = new StringBuffer();
                } else if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
                    lineBuffer.append(streamTokenizer.sval);
                    colCount = lineBuffer.length();
                    String test = streamTokenizer.sval;
                    int count = 1;

                    if (test.length() > maxCols) {
                        do {
                            String subString = test.substring(0, maxCols);
                            returnBuffer.append(subString);
                            returnBuffer.append("<br/>");
                            test = streamTokenizer.sval.substring(maxCols * count);
                            count++;
                        } while (test.length() > maxCols);
                        returnBuffer.append(test);
                    } else if (colCount >= maxCols) {
                        colCount = 0;
                        returnBuffer.append("<br/>");
                        lineBuffer = new StringBuffer();
                        returnBuffer.append(streamTokenizer.sval);
                    } else {
                        returnBuffer.append(streamTokenizer.sval);
                    }
                    returnBuffer.append(" ");

                }
            }
        } catch (IOException ex) {
            // Will never happen coming from a string.
            Contract.fail(ex);
        }
        return returnBuffer.toString();
    }

}
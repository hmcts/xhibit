package uk.gov.courtservice.framework.log4j;

import java.util.Calendar;

import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * <P>
 * Name: XhibitLayout
 * </P>
 * <P>
 * Layout the events in the standard Xhibit way (see format method for example)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author: William Fardell, Xdevelopment (2004)
 * @version: 1.0
 */
public class XhibitLayout extends Layout {
    /**
     * The line seperator to append to the end of the message
     */
    private static final String NEW_LINE = System.getProperty("line.separator", "\n");

    /**
     * The end index of the line seperator cached here
     */
    private static final int NEW_LINE_END_INDEX = NEW_LINE.length();

    /**
     * The digits are used for formating the date
     */
    private static final char[] DIGIT = "0123456789".toCharArray();

    /**
     * The amount of space to insert the (right aligned) level into
     */
    private static final int LEVEL_SPACE = 5;

    /**
     * The layout to use if an error occures using the fast layout
     */
    private static final PatternLayout ERROR_LAYOUT = new PatternLayout(
            "%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %c{1} (PL) - %m%n");

    /**
     * <p>
     * Format the logging event if an error occures default to the slower
     * pattern layout
     * </p>
     * <p>
     * Pattern: %d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %c{1} - %m%n
     * </p>
     * <p>
     * Sample: 2006-05-31 14:30:17,225 DEBUG Test - Test message 7
     * </p>
     * 
     * @param event
     *            The event to format
     * @return The formated event for outputing into the log
     */
    public String format(LoggingEvent event) {
        try {
            return _format(event);
        } catch (Throwable t) {
            return ERROR_LAYOUT.format(event);
        }
    }

    private String _format(LoggingEvent event) {
        String message = event.getRenderedMessage();
        int messageEndIndex = message.length();

        String category = event.getLoggerName();
        // Changed to use complete logger name for non courtservice loggers, for
        // courtservice
        // loggers strip common prefix.
        // Previous: int categoryStartIndex = category.lastIndexOf('.') + 1; //
        // If not found -1 which gives 0 which is ok index
        int categoryStartIndex;
        if (category.startsWith("uk.gov.courtservice.")) {
            categoryStartIndex = "uk.gov.courtservice.".length();
        } else {
            categoryStartIndex = 0;
        }
        int categoryEndIndex = category.length();

        // See below sections to see how the buffer length calculation is
        // deduced.
        char[] buffer = new char[23 + 1 + LEVEL_SPACE + 1 + (categoryEndIndex - categoryStartIndex) + 3
                + messageEndIndex + NEW_LINE_END_INDEX];

        // Append Date/Time (23 chars)
        Calendar calendar = Calendar.getInstance(); // Current Date and Time
        int year = calendar.get(Calendar.YEAR);
        buffer[0] = DIGIT[year / 1000];
        buffer[1] = DIGIT[(year % 1000) / 100];
        buffer[2] = DIGIT[((year % 1000) % 100) / 10];
        buffer[3] = DIGIT[((year % 1000) % 100) % 10];
        buffer[4] = '-';
        int month = calendar.get(Calendar.MONTH) + 1;
        buffer[5] = DIGIT[month / 10];
        buffer[6] = DIGIT[month % 10];
        buffer[7] = '-';
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        buffer[8] = DIGIT[day / 10];
        buffer[9] = DIGIT[day % 10];
        buffer[10] = ' ';
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        buffer[11] = DIGIT[hour / 10];
        buffer[12] = DIGIT[hour % 10];
        buffer[13] = ':';
        int minute = calendar.get(Calendar.MINUTE);
        buffer[14] = DIGIT[minute / 10];
        buffer[15] = DIGIT[minute % 10];
        buffer[16] = ':';
        int second = calendar.get(Calendar.SECOND);
        buffer[17] = DIGIT[second / 10];
        buffer[18] = DIGIT[second % 10];
        buffer[19] = ',';
        int millisecond = calendar.get(Calendar.MILLISECOND);
        buffer[20] = DIGIT[millisecond / 100];
        buffer[21] = DIGIT[(millisecond % 100) / 10];
        buffer[22] = DIGIT[(millisecond % 100) % 10];

        // Append Space (1 char)
        buffer[23] = ' ';

        int index = 24; // The next available index

        // Append Level (LEVEL_SPACE chars)
        String level = event.getLevel().toString();
        int levelEndIndex = level.length();
        if (levelEndIndex < LEVEL_SPACE) {
            int padEndIndex = 24 + (LEVEL_SPACE - levelEndIndex);
            while (index < padEndIndex) {
                buffer[index++] = ' ';
            }
            level.getChars(0, levelEndIndex, buffer, index);
            index += levelEndIndex;
        } else {
            level.getChars(0, LEVEL_SPACE, buffer, index);
            index += LEVEL_SPACE;
        }

        // Append Space (1 char)
        buffer[index++] = ' ';

        // Append Category (categoryEndIndex - categoryStartIndex chars)
        category.getChars(categoryStartIndex, categoryEndIndex, buffer, index);
        index += categoryEndIndex - categoryStartIndex;

        // Append Dash (3 chars)
        buffer[index++] = ' ';
        buffer[index++] = '-';
        buffer[index++] = ' ';

        // Append Message (messageEndIndex chars)
        message.getChars(0, messageEndIndex, buffer, index);
        index += messageEndIndex;

        // Append NL (NEW_LINE_END_INDEX chars)
        NEW_LINE.getChars(0, NEW_LINE_END_INDEX, buffer, index);

        // Return
        return new String(buffer);
    }

    /**
     * Return true as we do not layout the exception
     */
    public boolean ignoresThrowable() {
        return true;
    }

    /**
     * OptionHandler Implemenation: Activate any changes from config, this
     * layout has no optional configuration as it is highly optimised for speed
     */
    public void activateOptions() {
        // Implement Interface
    }
}

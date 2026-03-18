package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import uk.gov.courtservice.xhibit.client.courtlog.util.EventConstants;
import uk.gov.courtservice.xhibit.client.util.DocumentAttributeSet;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.util.table.style.StyleTableCellValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: Table Model for Court Log Events
 * </p>
 * <p>
 * Description: Contains the backing data for the court log events
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @editor Rakesh Lakhani v1.1 - Modified to extend XHIBITDefaultTableModel
 *         instead of Abstract
 * @version 1.1
 */

public class CourtLogEventsTableModel extends XHIBITDefaultTableModel {
    /**
     * The date column of the log event table.
     */
    public static final int DATE_COLUMN = 0;

    /**
     * The time column of the log event table.
     */
    public static final int TIME_COLUMN = 1;

    /**
     * The event details column of the log event table.
     */
    public static final int EVENT_COLUMN = 2;

    /**
     * The formatted court log events.
     */
    private HashMap courtLogEventsHashMap;

    /**
     * Constructs a CourtLogEventsTableModel
     * 
     * @param cleArrayIn
     *            Array of CourtLogViewValue
     */
    public CourtLogEventsTableModel(CourtLogViewValue[] cleArrayIn) {
        super();
        setData(cleArrayIn);

        String[] columnNames = new String[3];
        columnNames[0] = ResourceBundleHelper.getResource(XhibitBundles.CourtLogResources, "Date");
        columnNames[1] = ResourceBundleHelper.getResource(XhibitBundles.CourtLogResources, "Time");
        columnNames[2] = ResourceBundleHelper.getResource(XhibitBundles.CourtLogResources, "Event");
        setColumnNames(columnNames);
    }

    /**
     * Returns the value for the cell at row and column
     * 
     * @param row
     *            the row whose value is to be queried
     * @param column
     *            the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int row, int column) {
        if ((_data == null) || row >= _data.length) {
            if (column == EVENT_COLUMN) {
                return new ArrayList();
            } else {
                return "";
            }
        } else {
            CourtLogViewValue myVO = (CourtLogViewValue) _data[row];
            switch (column) {
            case DATE_COLUMN:
                return XDateFormat.format(myVO.getEntryDate(), XDateFormat.DATEFORMAT);
            case TIME_COLUMN:
                return XDateFormat.format(myVO.getEntryDate(), XDateFormat.TIMEFORMAT);
            case EVENT_COLUMN:
                // Get the details from CourtLogEvents HashMap keyed on
                // myVO.getLogEntryId( )
                return courtLogEventsHashMap.get(myVO.getLogEntryId());
            default:
                return "";
            }
        }
    }

    /**
     * Sets the data for the model.
     * 
     * @param cleArrayIn
     *            Array of CourtLogViewValue
     */
    public void setData(Object[] cleArrayIn) {
        super.setData(cleArrayIn);

        if (courtLogEventsHashMap == null) {
            courtLogEventsHashMap = new HashMap();
        } else {
            courtLogEventsHashMap.clear();
        }

        for (int x = 0; x < cleArrayIn.length; x++) {
            CourtLogViewValue myVO = (CourtLogViewValue) cleArrayIn[x];
            courtLogEventsHashMap.put(myVO.getLogEntryId(), parseLogEntry(myVO));
        }
    }

    /**
     * Parse the given CourtLogViewValue and format the event text for display.
     * 
     * @param clvv
     *            CourtLogViewValue to parse
     * @return List of StyleTableCellValue
     */
    private ArrayList parseLogEntry(final CourtLogViewValue clvv) {
        final String logEntry = clvv.getLogEntry();
        Calendar updateDate = Calendar.getInstance();
        updateDate.setTime(clvv.getLastUpdateDate());
        Calendar logDate = Calendar.getInstance();
        logDate.setTime(clvv.getEntryDate());

        final boolean onSameDay = (updateDate.get(Calendar.YEAR) == logDate.get(Calendar.YEAR) && updateDate
                .get(Calendar.DAY_OF_YEAR) == logDate.get(Calendar.DAY_OF_YEAR));
        final int mainFormat = onSameDay ? DocumentAttributeSet.BOLD : DocumentAttributeSet.BOLD_ITALICS;
        final int freeFormat = onSameDay ? DocumentAttributeSet.NOFORMATTING : DocumentAttributeSet.ITALICS;

        final ArrayList cellValues = new ArrayList();
        int offset;

        // get the event header text which will be displayed using the
        // mainFormat
        offset = logEntry.indexOf(EventConstants.EVENT_HEADER_BEGIN);
        if (offset >= 0) {
            String headerText = logEntry.substring(offset + EventConstants.EVENT_HEADER_BEGIN.length(),
                    logEntry.indexOf(EventConstants.EVENT_HEADER_END)).trim();
            cellValues.add(new StyleTableCellValue(convertSpecialChars(headerText), mainFormat));
        }

        // Get the event text.
        offset = logEntry.indexOf(EventConstants.EVENT_TEXT_BEGIN);
        if (offset >= 0) {
            String freeText = logEntry.substring(offset + EventConstants.EVENT_TEXT_BEGIN.length(),
                    logEntry.indexOf(EventConstants.EVENT_TEXT_END)).trim();
            if (freeText.length() > 0) {
                if (cellValues.size() > 0) {
                    // If there is already at least one value then this one
                    // needs to
                    // go on a new line.
                    freeText = "\n" + freeText;
                }
                cellValues.add(new StyleTableCellValue(convertSpecialChars(freeText), freeFormat));
            }
        }

        if (cellValues.size() <= 0) {
            // Event not in usual format i.e. no header text and no free
            // text,
            // so just display an error.
            if (logEntry.indexOf(EventConstants.EVENT_HEADER_EMPTY) >= 0
                    && logEntry.indexOf(EventConstants.EVENT_TEXT_EMPTY) >= 0) {
                String errorText = ResourceBundleHelper
                        .getResource(XhibitBundles.CourtLogResources, "translationError")
                        + " " + clvv.getEventType().toString();
                cellValues.add(new StyleTableCellValue(errorText, DocumentAttributeSet.BOLD));
            }
        }
        return cellValues;
    }

    //
    // Moved from CourtLogXslHelper, we should not convert these characters
    // before deiplay
    //    

    private static final String[] specialChars = { "&amp;", "&lt;", "&gt;", "&#13;" };

    private static final String[] replacement = { "&", "<", ">", "" };

    /**
     * Converts Special character's codes back to the proper characters.
     * 
     * @param inputString
     *            String to convert.
     * 
     * @return The converted string.
     */
    private static String convertSpecialChars(final String inputString) {
        String tempStr = inputString;
        for (int i = 0; i < specialChars.length; i++) {
            tempStr = replaceSubstring(tempStr, specialChars[i], replacement[i]);
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
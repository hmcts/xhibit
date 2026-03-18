/*
 *   Copyright (c) 2001 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
 * 
 *   The author makes no representations or warranties about the suitability of the
 *   software, either express or implied, including but not limited to the
 *   implied warranties of merchantability, fitness for a particular
 *   purpose, or non-infringement. The author shall not be liable for any damages
 *   suffered by licensee as a result of using, modifying or distributing
 *   this software or its derivatives.
 *
 *   The author requests that he be notified of any application, applet, or other binary that 
 *   makes use of this code and that some acknowedgement is given. Comments, questions and 
 *   requests for change will be welcomed.
 */
package mseries.ui;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import javax.swing.JTextField;
import javax.swing.plaf.TextUI;
import javax.swing.text.Caret;

/**
 * A SpinnerEditor that manages input and display of date values
 * (java.util.Date). The required display format is fully configurable and
 * follows the format rules defined in java.text.SimpleDateFormat.
 * <P>
 * The position of the curser defines the steps size to use when the field is
 * incremented
 * 
 * @see mseries.ui.MDateSpinnerModel
 */
public class DateEditor extends DefaultSpinnerEditor {

    MDateField display;

    private Caret caret;

    private Calendar m_calendar = Calendar.getInstance();

    private ArrayList fieldPositions = new ArrayList();

    private Date m_lastDate = new Date();

    protected String format = "dd/MM/yy";

    private MDateFormat df = new MSimpleDateFormat(format, Locale.getDefault());

    private int fieldId;

    private int[] fieldTypes = { DateFormat.ERA_FIELD, DateFormat.YEAR_FIELD, DateFormat.MONTH_FIELD,
            DateFormat.DATE_FIELD, DateFormat.HOUR_OF_DAY1_FIELD, DateFormat.HOUR_OF_DAY0_FIELD,
            DateFormat.MINUTE_FIELD, DateFormat.SECOND_FIELD, DateFormat.MILLISECOND_FIELD,
            DateFormat.DAY_OF_WEEK_FIELD, DateFormat.DAY_OF_YEAR_FIELD, DateFormat.DAY_OF_WEEK_IN_MONTH_FIELD,
            DateFormat.WEEK_OF_YEAR_FIELD, DateFormat.WEEK_OF_MONTH_FIELD, DateFormat.AM_PM_FIELD,
            DateFormat.HOUR1_FIELD, DateFormat.HOUR0_FIELD };

    /**
     * Constructs a DateEditor with the format passed.
     * 
     * @param format
     *            the required date format according to the rules in
     *            java.text.DateFormat
     * @see java.text.SimpleDateFormat
     */
    public DateEditor(String format) {
        display = new MDateField() {
            public void setUI(TextUI ui) {
                super.setUI(ui);
                setBorder(null);
            }
        };
        setFormat(format);
        init();
    }

    /**
     * Default constructor using the default date format which is "dd/MM/yy"
     */
    public DateEditor() {
        display = new MDateField();
        init();
    }

    private void init() {
        display.setPopup(false);
        display.setDocument(getCustomDocument());
        caret = display.getCaret();
        setFormatter(df);
        setValue(m_lastDate);
    }

    protected void reinit() {
        caret = display.getCaret();
        setValue(m_lastDate);
    }

    /**
     * @return the value of the field
     * @exception when
     *                the value can not be parsed using the default or passed
     *                date formatter
     */
    public Date getDate() throws ParseException {
        return display.getValue();
    }

    /**
     * @return the value of the field
     * @param defaultValue
     *            the value to be returned if a parseException occurs, useful if
     *            there is no value in the field as "" is not parsable
     */
    public Date getDate(Date defaultValue) {
        Date d;
        try {
            d = getDate();
        } catch (ParseException e) {
            d = defaultValue;
        }
        return d;
    }

    /**
     * Returns the current valid in the display, it is far more reliable to use
     * the value from the model as the user may have typed garbage into the
     * display if the field is editable.
     * 
     * @rturn the text in the display
     */
    public Object getValue() {
        return getDate(m_lastDate);
    }

    /**
     * Sets the value of the field
     * 
     * @param date
     *            the value
     */
    public void setValue(Object date) {
        if (date instanceof java.util.Date) {
            m_lastDate = (Date) date;
            m_calendar.setTime((Date) date);

            setting = true;
            display.setValue((Date) date);
            setting = false;
        }

        getFieldPositions();
        if (fieldId > 0) {
            caret = display.getCaret();
            FieldPosition fieldPosition = getFieldPosition(fieldId);
            caret.setDot(fieldPosition.getBeginIndex());
        }
    }

    private FieldPosition getFieldPosition(int fieldNum) {
        FieldPosition result = null;

        for (Iterator iter = fieldPositions.iterator(); iter.hasNext();) {
            FieldPosition fieldPosition = (FieldPosition) iter.next();

            if (fieldPosition.getField() == fieldNum) {
                result = fieldPosition;

                break;
            }
        }
        return (result);
    }

    private void getFieldPositions() {
        fieldPositions.clear();

        for (int ctr = 0; ctr < fieldTypes.length; ++ctr) {
            int fieldId = fieldTypes[ctr];
            FieldPosition fieldPosition = new FieldPosition(fieldId);
            StringBuffer formattedField = new StringBuffer();

            df.format(m_lastDate, formattedField, fieldPosition);

            if (fieldPosition.getEndIndex() > 0) {
                fieldPositions.add(fieldPosition);
            }
        }

        fieldPositions.trimToSize();
        Collections.sort(fieldPositions, new Comparator() {
            public int compare(Object o1, Object o2) {
                return (((FieldPosition) o1).getBeginIndex() - ((FieldPosition) o2).getBeginIndex());
            }
        });
    }

    private FieldPosition getField(int caretLoc) {
        FieldPosition fieldPosition = null;

        for (Iterator iter = fieldPositions.iterator(); iter.hasNext();) {
            FieldPosition chkFieldPosition = (FieldPosition) iter.next();

            if ((chkFieldPosition.getBeginIndex() <= caretLoc) && (chkFieldPosition.getEndIndex() > caretLoc)) {
                fieldPosition = chkFieldPosition;

                break;
            }
        }

        return (fieldPosition);
    }

    private FieldPosition getPrevField(int caretLoc) {
        FieldPosition fieldPosition = null;

        for (int ctr = fieldPositions.size() - 1; ctr > -1; --ctr) {
            FieldPosition chkFieldPosition = (FieldPosition) fieldPositions.get(ctr);

            if (chkFieldPosition.getEndIndex() <= caretLoc) {
                fieldPosition = chkFieldPosition;

                break;
            }
        }
        return (fieldPosition);
    }

    private FieldPosition getFirstField() {
        FieldPosition result = null;

        try {
            result = ((FieldPosition) fieldPositions.get(0));
        } catch (NoSuchElementException ex) {
        }

        return (result);
    }

    /**
     * Gets the part of the field that the curser is in expressed as a
     * java.util.DateFormat constant.
     * 
     * @return the step or field
     */
    public int getStep() {
        int curField;

        FieldPosition fieldPosition = getField(caret.getDot());

        if (fieldPosition != null) {
            if (caret.getDot() != fieldPosition.getBeginIndex()) {
                caret.setDot(fieldPosition.getBeginIndex());
            }
        } else {
            fieldPosition = getPrevField(caret.getDot());

            if (fieldPosition != null) {
                caret.setDot(fieldPosition.getBeginIndex());
            } else {
                fieldPosition = getFirstField();

                if (fieldPosition != null) {
                    caret.setDot(fieldPosition.getBeginIndex());
                }
            }
        }

        if (fieldPosition != null) {
            curField = fieldPosition.getField();
        } else {
            curField = -1;
        }
        fieldId = curField;
        return curField;
    }

    /**
     * Sets the format for display using the notation in
     * java.text.SimpleDateFormat. <br>
     * <ul>
     * For Example
     * </ul>
     * <br>
     * 
     * <pre>
     *     HH:mm - hours &amp; minutes (e.g. 12:36)
     *     MMMMM, yyyy - Month and year (e.g. April, 2001)
     *     dd MMM yy - Day, Month, year (e.g. 14 Apr 01)
     * </pre>
     * 
     * @param format
     *            the display format, default is "HH:mm"
     */
    public void setFormat(String format) {
        this.format = format;
        setFormatter(new MSimpleDateFormat(format, Locale.getDefault()));
    }

    /**
     * Sets the format for display using the notation in
     * java.text.SimpleDateFormat
     * 
     * @param format
     *            the display format, default is "HH:mm"
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * Sets a custom date formatter
     * 
     * @param the
     *            date formatter, java.util.Date format can be easily wrapped to
     *            implement the necessary interface or an instance of
     *            MSimpleDateFormat may be used.
     */
    public void setFormatter(MDateFormat formatter) {
        display.setDateFormatter(formatter);
        df = formatter;
        reinit();
    }

    /**
     * @return the date formatter
     */
    public MDateFormat getFormatter() {
        return df;
    }

    public JTextField getTextField() {
        return display;
    }
}

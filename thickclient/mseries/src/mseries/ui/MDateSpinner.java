/*
 *   Copyright (c) 2001 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
 *   
 *   Thanks go to David M Karr who, with permission, kindly supplied the code in the form
 *   of his DateTimeEditor, on which this component is based.
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
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
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import mseries.Calendar.MFieldListener;
import mseries.Calendar.MMonthEvent;
import mseries.Calendar.MMonthListener;
import mseries.utils.MSpinnerLayout;

/**
 * Date entry field with spinner buttons to increment and decrement portions of
 * the date/time. Many date formats can be set, the buttons operate on the
 * portion of the field that is nearest the caret, when the buttons are pressed
 * MMonthEvents are fired to the registered listeners. MFieldEvents are fired
 * when the component gets and loses focus.
 * <P>
 * <B>NOTE:</B> When no time is passed, the component uses the 'default
 * date/time' (new Date()) which disregards Daylight Saving Time for (at least)
 * GMT timezone (known as BST) on Windows. I consider this a bug in
 * java.util.Date, using the setValue method will cause the passed time to be
 * used.
 * 
 * @deprecated in favour of MSpinner with MDateSpinnerModel and DateEditor
 * @see mseries.ui.MSpinner
 * @see mseries.ui.MDateSpinnerModel
 * @see mseries.ui.DateEditor
 */
public class MDateSpinner extends JPanel {
    private Calendar m_calendar = Calendar.getInstance();

    private ArrayList fieldPositions = new ArrayList();

    private Date m_lastDate = new Date();

    private Caret m_caret;

    private int curField = -1;

    public MDateField display;

    protected Vector listeners = new Vector();

    protected Vector fieldListeners = new Vector();

    private AbstractAction upAction = new UpDownAction(1, "up");

    private AbstractAction downAction = new UpDownAction(-1, "down");

    private JButton up, down;

    private boolean setting = false;

    private boolean editable = true;

    protected String format = "HH:mm";

    private MDateFormat df = new MSimpleDateFormat(format, Locale.getDefault());

    private int fieldId;

    private int[] fieldTypes = { DateFormat.ERA_FIELD, DateFormat.YEAR_FIELD, DateFormat.MONTH_FIELD,
            DateFormat.DATE_FIELD, DateFormat.HOUR_OF_DAY1_FIELD, DateFormat.HOUR_OF_DAY0_FIELD,
            DateFormat.MINUTE_FIELD, DateFormat.SECOND_FIELD, DateFormat.MILLISECOND_FIELD,
            DateFormat.DAY_OF_WEEK_FIELD, DateFormat.DAY_OF_YEAR_FIELD, DateFormat.DAY_OF_WEEK_IN_MONTH_FIELD,
            DateFormat.WEEK_OF_YEAR_FIELD, DateFormat.WEEK_OF_MONTH_FIELD, DateFormat.AM_PM_FIELD,
            DateFormat.HOUR1_FIELD, DateFormat.HOUR0_FIELD };

    /**
     * Default constructor
     */
    public MDateSpinner() {
        display = new MDateField();
        init();
    }

    /*
     * Constructor @param size the size of the display field, not the number of
     * characters
     */
    public MDateSpinner(int size) {
        display = new MDateField(size);
        init();
    }

    private void init() {
        display.setPopup(false);
        display.addFocusListener(new FocusManager(this));

        up = new BasicArrowButton(BasicArrowButton.NORTH);
        up.addActionListener(upAction);
        up.setBackground(UIManager.getColor("control"));

        down = new BasicArrowButton(BasicArrowButton.SOUTH);
        down.addActionListener(downAction);
        down.setBackground(UIManager.getColor("control"));

        setLayout(new MSpinnerLayout());
        // display.setBorder(null);
        // setBorder(UIManager.getBorder("ComboBox.border"));
        display.setDocument(getCustomDocument());
        add(display);
        add(up);
        add(down);

        m_caret = display.getCaret();

        setupKeymap();
        setFormatter(df);
        setValue(m_lastDate);
        setMinimumSize(new Dimension(64, 21));
    }

    /**
     * Oerride this method to supply the custom document to the textfield. The
     * document is used to restrict the characters that can be typed into the
     * field.
     * 
     * @return the custom document
     */
    public Document getCustomDocument() {
        return new DateTimeDocument();
    }

    /**
     * The custom document that prevents editing of he field yet allows the
     * curser to be landed in the field.
     */
    protected class DateTimeDocument extends PlainDocument {
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            if (isEditable() || setting) {
                super.insertString(offset, str, a);
            }
        }

        public void remove(int offset, int len) throws BadLocationException {
            if (isEditable() || setting) {
                super.remove(offset, len);
            }
        }
    }

    /**
     * Sets the value of the field
     * 
     * @param date
     *            the value
     */
    public void setValue(Date date) {
        m_lastDate = date;

        m_calendar.setTime(date);

        setting = true;
        display.setValue(date);
        setting = false;

        getFieldPositions();
        if (fieldId != 0) {
            FieldPosition fieldPosition = getFieldPosition(fieldId);
            m_caret.setDot(fieldPosition.getBeginIndex());
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

    private void reinit() {
        setValue(m_lastDate);
        m_caret.setDot(0);
        setCurField();
        repaint();
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

    /**
     * Class to do the increment/decrement when the buttons are pressed
     */
    protected class UpDownAction extends AbstractAction {
        int m_direction; // +1 = up; -1 = down

        public UpDownAction(int direction, String name) {
            super(name);

            m_direction = direction;
        }

        public void actionPerformed(ActionEvent evt) {
            if (!this.isEnabled()) {
                return;
            }

            setCurField();
            boolean dateSet = true;

            switch (curField) {

            case DateFormat.AM_PM_FIELD:
                // If the time is AM, add 12 hours, if it is PM subtract 12
                // hours
                int ampm = m_calendar.get(Calendar.AM_PM);
                int mult = (ampm == Calendar.AM) ? 12 : -12;

                m_calendar.set(Calendar.HOUR, m_calendar.get(Calendar.HOUR) + mult);

                break;

            case DateFormat.DATE_FIELD:

            case DateFormat.DAY_OF_WEEK_FIELD:

            case DateFormat.DAY_OF_WEEK_IN_MONTH_FIELD:

            case DateFormat.DAY_OF_YEAR_FIELD:
                m_calendar.set(Calendar.DAY_OF_YEAR, m_calendar.get(Calendar.DAY_OF_YEAR) + m_direction);
                break;

            case DateFormat.ERA_FIELD:
                dateSet = false;

                break;

            case DateFormat.HOUR0_FIELD:

            case DateFormat.HOUR1_FIELD:

            case DateFormat.HOUR_OF_DAY0_FIELD:

            case DateFormat.HOUR_OF_DAY1_FIELD:

                m_calendar.set(Calendar.HOUR, m_calendar.get(Calendar.HOUR) + m_direction);
                break;

            case DateFormat.MILLISECOND_FIELD:
                m_calendar.set(Calendar.MILLISECOND, m_calendar.get(Calendar.MILLISECOND) + m_direction);
                break;

            case DateFormat.MINUTE_FIELD:
                m_calendar.set(Calendar.MINUTE, m_calendar.get(Calendar.MINUTE) + m_direction);

                break;

            case DateFormat.MONTH_FIELD:
                m_calendar.set(Calendar.MONTH, m_calendar.get(Calendar.MONTH) + m_direction);

                m_lastDate = m_calendar.getTime();

                break;

            case DateFormat.SECOND_FIELD:
                m_calendar.set(Calendar.SECOND, m_calendar.get(Calendar.SECOND) + m_direction);

                break;

            case DateFormat.WEEK_OF_MONTH_FIELD:
                m_calendar.set(Calendar.WEEK_OF_MONTH, m_calendar.get(Calendar.WEEK_OF_MONTH) + m_direction);

                break;

            case DateFormat.WEEK_OF_YEAR_FIELD:
                m_calendar.set(Calendar.WEEK_OF_MONTH, m_calendar.get(Calendar.WEEK_OF_MONTH) + m_direction);

                break;

            case DateFormat.YEAR_FIELD:
                m_calendar.set(Calendar.YEAR, m_calendar.get(Calendar.YEAR) + m_direction);

                break;

            default:
                dateSet = false;
            }

            if (dateSet) {
                m_lastDate = m_calendar.getTime();
                fieldId = curField;

                setValue(m_lastDate);

                display.requestFocus();
                repaint();
                notifyListeners(new MMonthEvent(this, MMonthEvent.NEW_DATE, m_calendar));
            }
        }
    }

    /**
     * Assigns the actions to the up and down arrow keys
     */
    protected void setupKeymap() {
        display.registerKeyboardAction(upAction, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_FOCUSED);
        display
                .registerKeyboardAction(downAction, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                        JComponent.WHEN_FOCUSED);

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

    private void setCurField() {
        FieldPosition fieldPosition = getField(m_caret.getDot());

        if (fieldPosition != null) {
            if (m_caret.getDot() != fieldPosition.getBeginIndex()) {
                m_caret.setDot(fieldPosition.getBeginIndex());
            }
        } else {
            fieldPosition = getPrevField(m_caret.getDot());

            if (fieldPosition != null) {
                m_caret.setDot(fieldPosition.getBeginIndex());
            } else {
                fieldPosition = getFirstField();

                if (fieldPosition != null) {
                    m_caret.setDot(fieldPosition.getBeginIndex());
                }
            }
        }

        if (fieldPosition != null) {
            curField = fieldPosition.getField();
        } else {
            curField = -1;
        }
    }

    /**
     * Sets the field enabled or not. The disabled field can not be editednor
     * landed in
     * 
     * @param enable
     *            true - enabled, false not enabled
     */
    public void setEnabled(boolean enable) {
        display.setEnabled(enable);
        up.setEnabled(enable);
        down.setEnabled(enable);
    }

    /**
     * Is the field enabled ?
     * 
     * @return true if the field is enabled
     */
    public boolean isEnabled() {
        return (display.isEnabled() && up.isEnabled() && down.isEnabled());
    }

    /**
     * @return the value of the field
     * @exception when
     *                the value can not be parsed using the default or passed
     *                date formatter
     */
    public Date getValue() throws ParseException {
        return display.getValue();
    }

    /**
     * @return the value of the field
     * @param defaultValue
     *            the value to be returned if a parseException occurs, useful if
     *            there is no value in the field as "" is not parsable
     */
    public Date getValue(Date defaultValue) {
        try {
            return getValue();
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    /**
     * Disable changing the date using the keyboard, use setEnabled(false) if
     * the date is not to be changed at all.
     * 
     * @param editable
     *            true if the field can be changed by typing.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Can the date be changed using the keyboard ?
     * 
     * @return is the date changeble using the keyboard ?
     */
    public boolean isEditable() {
        return this.editable;
    }

    /**
     * Registers the listeners of the field changes. Fired when the spinner
     * buttons are pushed.
     * 
     * @param listener -
     *            MMonthListener
     */
    public void addMMonthListener(MMonthListener listener) {
        listeners.addElement(listener);
    }

    /**
     * Removes the listener from the registered list of listeners
     * 
     * @param listener -
     *            MMonthListener
     */
    public void removeMMonthListener(MMonthListener listener) {
        listeners.removeElement(listener);

    }

    protected void notifyListeners(MMonthEvent event) {
        // Pass these events on to the registered listener

        Vector list = (Vector) listeners.clone();
        for (int i = 0; i < list.size(); i++) {
            MMonthListener l = (MMonthListener) listeners.elementAt(i);
            l.dataChanged(event);
        }
    }

    /**
     * Simply delgates to super.removeFocusListener(),
     * 
     * @deprecated use removeMFieldListener instead, it is much more reliable
     */
    public void removeFocusListener(FocusListener listener) {
        super.removeFocusListener(listener);
    }

    /**
     * Simply delgates to super.addFocusListener(),
     * 
     * @deprecated use addMFieldListener instead, it is much more reliable
     */
    public void addFocusListener(FocusListener listener) {
        super.addFocusListener(listener);
    }

    /**
     * Registers the listeners field getting and losing focus
     * 
     * @param listener -
     *            MMonthListener
     */
    public void addMFieldListener(MFieldListener listener) {
        fieldListeners.addElement(listener);
    }

    /**
     * Removes the listener from the registered list of listeners
     * 
     * @param listener -
     *            MMonthListener
     */
    public void removeMFieldListener(MFieldListener listener) {
        fieldListeners.removeElement(listener);
    }

    private void notifyListeners(FocusEvent e) {
        int type = e.getID();
        Vector list = (Vector) fieldListeners.clone();
        for (int i = 0; i < list.size(); i++) {
            MFieldListener l = (MFieldListener) list.elementAt(i);
            if (type == FocusEvent.FOCUS_GAINED) {
                l.fieldEntered(e);
            } else {
                l.fieldExited(e);
            }
        }
    }

    public void setToolTipText(String text) {
        display.setToolTipText(text);
    }

    public String getToolTipText() {
        return display.getToolTipText();
    }

    /**
     * Simple class to be a focus listener for the MDateSpinner
     */
    class FocusManager implements FocusListener {
        MDateSpinner parent;

        public FocusManager(MDateSpinner parent) {
            this.parent = parent;
        }

        public void focusLost(FocusEvent e) {
            parent.notifyListeners(e);
        }

        public void focusGained(FocusEvent e) {
            parent.notifyListeners(e);
        }
    }
}

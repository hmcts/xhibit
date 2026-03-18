package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mseries.ui.MDateField;
import mseries.ui.MSimpleDateFormat;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 31-03-2003 AW Daley Modified to read Resource Bundle name from Constant in
 * XhibitBundles class rather than String. Ensures correct resource bundle name
 * is used.
 */
public class XTimePanel extends MDateField // XPanel
{
    // log directly from the class
    private static Logger log = Logger.getLogger(XTimePanel.class.getName());

    private boolean required = true;

    private ResourceBundle timePanelResources;

    private JPanel timeContainer;

    public final String timeError = "validation.time";

    public final String errorMessage;

    private boolean isValidTime = true;

    private SimpleDateFormat timeFormat;

    // Ensures that the correct error message is displayed
    private static final String ERROR_RESOURCE = "XHIBITUtilResources";

    private String localTimeFormat;

    private boolean timeLostFocus = false;

    /**
     * Create a time panel using the time passed in.
     * 
     * @param containingPanel
     * @param defaultTime
     */
    public XTimePanel(JPanel containingPanel, Calendar defaultTime) {
        super(6);
        errorMessage = XHIBITConstant.getResource(ERROR_RESOURCE, "time.error.message");
        timeFormat = new SimpleDateFormat(XDateFormat.simpleTimeFormat);
        localTimeFormat = timeFormat.toLocalizedPattern().toUpperCase();
        timeContainer = containingPanel;
        init(defaultTime);
    }

    /**
     * Create a time panel using the time and the format passed in.
     * 
     * @param containingPanel
     * @param defaultTime
     * @param format
     *            The date format to display the time
     */
    public XTimePanel(JPanel containingPanel, Calendar defaultTime, SimpleDateFormat format) {
        super(format.toLocalizedPattern().length());
        timeFormat = format;
        errorMessage = (XHIBITConstant.getResource(ERROR_RESOURCE, "time.log.error.message") + " " + timeFormat
                .toLocalizedPattern().toUpperCase());
        timeContainer = containingPanel;
        init(defaultTime);
        // setDateFormatter(new
        // MSimpleDateFormat(timeFormat.toLocalizedPattern()));
        localTimeFormat = timeFormat.toLocalizedPattern().toUpperCase();
    }

    /**
     * Create a time panel using the current system time.
     * 
     * @param containingPanel
     */
    public XTimePanel(JPanel containingPanel) {
        this(containingPanel, Calendar.getInstance());
    }

    /**
     * Create a time panel using the current system time.
     * 
     * @param containingPanel
     */
    public XTimePanel(JPanel containingPanel, SimpleDateFormat format) {
        this(containingPanel, Calendar.getInstance(), format);
    }

    private void init(Calendar calendar) {
        timePanelResources = XHIBITConstant.getResourceBundle(XhibitBundles.UtilResources);

        timeFormat.setLenient(false);
        setDateFormatter(new MSimpleDateFormat(timeFormat.toLocalizedPattern()));
        setEditable(true);
        setPopup(false);
        Dimension dM = new Dimension(60, XHIBITConstant.getLineHeight());
        Dimension dP = new Dimension(100, XHIBITConstant.getLineHeight());
        setPreferredSize(dP);
        setMinimumSize(dM);
        setTime(calendar);

        setInputVerifier(new TimeVerifier(this, errorMessage));

        addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent fe) {
                // pre-emptive bug to only call update methods if
                // the component is visible
                if (isShowing() && !fe.isTemporary()) {
                    try {
                        stepUpdateViewState();
                        // At this point try insert colon.
                        insertColon();
                    } catch (Exception e) {
                        XHIBITConstant.handleError(e);
                    }
                }
            }
        });

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                entryField_keyReleased(e);
            }
        });

        setToolTipText(XHIBITConstant.getResource(timePanelResources, "TimeComponent"));
    }

    private boolean validTime() {
        isValidTime = true;
        if (!isEditable() || !isEnabled())
            return true;

        if (getText().length() == 0 && !required) {
            // do nothing
        } else {
            String timeText = getText().trim();
            String userEnteredTime = new String(timeText);

            try {
                // System.err.println("userEnteredTime = " + userEnteredTime);
                // System.err.println("userEnteredTime length = " +
                // userEnteredTime.length() +
                // " localTimeFormat.length = " + localTimeFormat.length() +
                // " - " + localTimeFormat);

                if (!checkTimeFormat(userEnteredTime)) {
                    // throws exception if a number has not been entered.
                    Integer.parseInt(timeText);
                    // System.err.println("============= Integer.parseInt
                    // was successful! ========");

                    int timeLength = timeText.length();
                    if (timeLength == 3 && timeLostFocus) {
                        timeText = timeText.substring(0, 1) + ":" + timeText.substring(1, 3);
                    } else if (timeLength == 4 && timeLostFocus) {
                        timeText = timeText.substring(0, 2) + ":" + timeText.substring(2, 4);
                    } else {
                        setText(userEnteredTime);
                        requestFocus();

                        throw new CSValidationException(timeError, new String[] { localTimeFormat }, "time not integer");
                    }

                    if ((timeLength == 3 || timeLength == 4) && localTimeFormat.length() == 5) {
                        setText(timeText);
                        getValue();
                    } else {
                        isValidTime = false;
                    }
                } else {
                    isValidTime = true;
                }
            } catch (Exception ex2) {
                // setText(userEnteredTime);
                requestFocus();

                isValidTime = false;
            }
            timeLostFocus = false;
        }

        log.debug("validTime() returning " + isValidTime);
        return isValidTime;
    }

    private boolean checkTimeFormat(String time) {
        boolean timeOk = true;

        Date parsedDate = null;
        if (timeFormat != null) {
            ParsePosition pos = new ParsePosition(0);
            parsedDate = timeFormat.parse(time, pos);
        }
        // log.debug("parsedDate = " + parsedDate);

        if (parsedDate == null) {
            timeOk = false;
        } else {
            int iBegin = time.length() - 1;
            int iEnd = time.length();
            String lastChar = time.substring(iBegin, iEnd);

            try {
                Integer.parseInt(lastChar);

                if (time.length() == localTimeFormat.length()) {
                    timeOk = true;
                } else {
                    timeOk = false;
                }
            } catch (NumberFormatException nfe) {
                timeOk = false;
            }
        }

        log.debug("checkTimeFormat(" + time + ") returning " + timeOk);
        return timeOk;
    }

    /**
     * Returns a new string which is the given string without colons. Used to
     * get a numeric time string from the user entered time with colon(s).
     * 
     * @param timeText
     *            a time string you want to remove colons from.
     * @return a string without colons.
     */
    private String removeColons(String timeText) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < timeText.length(); i++) {
            char c = timeText.charAt(i);
            if (c != ':') {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public void insertColon() {
        log.debug("[insertColon]");
        String timeText = getText().trim();
        String userEnteredTime = new String(timeText);

        try {
            // throws exception if a number has not been entered.
            Integer.parseInt(timeText);

            int timeLength = timeText.length();
            if (timeLength == 3) {
                timeText = timeText.substring(0, 1) + ":" + timeText.substring(1, 3);
            } else if (timeLength == 4) {
                timeText = timeText.substring(0, 2) + ":" + timeText.substring(2, 4);
            } else {
                setText(userEnteredTime);
            }
            setText(timeText);
            getValue();
        } catch (Exception ex2) {
            setText(userEnteredTime);
        }
    }

    public void stepValidate() throws CSValidationException {
        log.debug("[stepValidate]");
        boolean valid = validTime();
        if (!valid) {
            log.debug("[stepValidate] ***FAILING TIME VALIDATION***");
            throw new CSValidationException(timeError, new String[] { localTimeFormat }, "time not integer");
        }
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        log.debug("[XTimePanel] stepUpdateViewState");
        if (timeContainer instanceof XPanel) {
            ((XPanel) timeContainer).stepUpdateViewState();
        }
        else if (timeContainer instanceof ChildOfXPanel) {
            ((ChildOfXPanel) timeContainer).stepUpdateViewState();
        }
    }

    /**
     * Returns the date in the field as a Calendar. Only a valid date will be
     * returned
     * 
     * @return
     * @throws CSValidationException
     */
    public Calendar getDate() throws CSValidationException {
        Calendar c = null;

        // Check to see if field is not required
        // If not required and no date then return null
        if (getText().length() <= 0)
            return c;

        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, getHour());
        c.set(Calendar.MINUTE, getMinute());
        c.set(Calendar.SECOND, getSecond());
        return c;
    }

    /**
     * Set a new time for the field
     * 
     * @param newDate
     */
    public void setTime(Calendar newDate) {
        if (newDate == null) {
            Date d = null;
            setTime(d);
        } else {
            setTime(newDate.getTime());
        }
    }

    /**
     * Set a new date for the field
     * 
     * @param newDate
     */
    public void setTime(Date newDate) {
        setValue(newDate);
    }

    /**
     * Sets the time field using hours and minutes passed in
     * 
     * @param hour
     * @param minute
     * @throws CSValidationException
     */
    public void setTime(int hour, int minute) throws CSValidationException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.HOUR, minute);
        setTime(c);
        stepValidate();
    }

    /**
     * Overrides getValue() in MDateField as the getValue() in MDateField will
     * quite happily accept any input after the seconds.
     * 
     * @return the date.
     * @throws ParseException
     */
    public Date getValue() throws ParseException {
        int correctNumericTimeLength;
        if (localTimeFormat.length() == 8) {
            correctNumericTimeLength = 6;
        } else {
            correctNumericTimeLength = 4;
        }

        String numericTime = removeColons(getText());
        if (numericTime.length() > correctNumericTimeLength) {
            throw new ParseException("Too many characters", 6);
        }
        try {
            Integer.parseInt(numericTime);
        } catch (NumberFormatException nfe) {
            // re-throw as a parse exception so as not to break classes that
            // use
            // XTimePanel
            log.debug(nfe);
            throw new ParseException("letters in time", 0);
        }

        return super.getValue();
    }

    /**
     * Return the hours from the user entry
     * 
     * @return
     * @throws CSValidationException
     */
    public int getHour() throws CSValidationException {
        Calendar d = Calendar.getInstance();
        try {
            d.setTime(getValue());
        } catch (ParseException ex) {
            throw new CSValidationException(timeError, new String[] { localTimeFormat }, "Date format invalid");
        }
        return d.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Return the minutes from the user entry
     * 
     * @return
     * @throws CSValidationException
     */
    public int getMinute() throws CSValidationException {
        Calendar d = Calendar.getInstance();
        try {
            d.setTime(getValue());
        } catch (ParseException ex) {
            throw new CSValidationException(timeError, new String[] { localTimeFormat }, "Date format invalid");
        }
        return d.get(Calendar.MINUTE);
    }

    /**
     * Return the seconds from the user entry This method was added as Court Log
     * events have to logged to the nearest second to order the log correctly.
     * 
     * @return
     * @throws CSValidationException
     */
    public int getSecond() throws CSValidationException {
        Calendar d = Calendar.getInstance();
        try {
            d.setTime(getValue());
        } catch (ParseException ex) {
            throw new CSValidationException(timeError, new String[] { localTimeFormat }, "Date format invalid");
        }
        return d.get(Calendar.SECOND);
    }

    /**
     * For an existing date, this methods appends the time and returns the full
     * date
     * 
     * @param datePart
     * @return
     * @throws CSValidationException
     */
    public Calendar getTimeForDate(Calendar datePart) throws CSValidationException {
        stepValidate();
        try {
            datePart.set(Calendar.HOUR_OF_DAY, getHour());
            datePart.set(Calendar.MINUTE, getMinute());
            datePart.set(Calendar.SECOND, getSecond());
            return datePart;
        } catch (CSValidationException ex) {
            log.debug(ex);
            return null;
        }
    }

    /**
     * For an existing date, this methods appends the time and returns the full
     * date
     * 
     * @param datePart
     * @return
     * @throws CSValidationException
     */
    public Calendar getTimeForDate(Date datePart) throws CSValidationException {
        Calendar c = Calendar.getInstance();
        c.setTime(datePart);
        return getTimeForDate(c);
    }

    /**
     * Set if the user is required to complete the field Defaults to true
     * 
     * @param newValue
     */
    public void setRequired(boolean newValue) {
        required = newValue;
    }

    /**
     * Set whether the display of the field is enabled. Defaults to true
     * 
     * @param newValue
     */
    public void setDateEnabled(boolean newValue) {
        setEnabled(newValue);
    }

    /**
     * Set whether the field can be editted. Defaults to true.
     * 
     * @param newValue
     */
    public void setDateEditable(boolean newValue) {
        setEditable(newValue);
    }

    /**
     * Allows access to the component for more advanced programming
     * 
     * @return
     */
    public MDateField getTimeComponent() {
        return this;
    }

    /**
     * Check if the user has populated text in the field. This does not check
     * the validity of the date.
     * 
     * @return true if there is data. If the field is not required, also returns
     *         true.
     */
    public boolean isMandatoryFieldsCompleted() {
        if (required) {
            if (getText().length() <= 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    void entryField_keyReleased(KeyEvent e) {
        if (!(e.getKeyCode() == KeyEvent.VK_LEFT) && !(e.getKeyCode() == KeyEvent.VK_RIGHT)) {
            try {
                isValidTime = false;
                if (validTime()) {
                    stepUpdateViewState();
                }
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
            }
        }
    }

    public boolean isValidTime() {
        return isValidTime;
    }

    class TimeVerifier extends InputVerifier {
        private Component component;

        private String errorTitle = XHIBITConstant.getResource(timePanelResources, "time.error.title");

        private String errorMessage = XHIBITConstant.getResource(timePanelResources, "time.error.message");

        public TimeVerifier(Component component) {
            this.component = component;
        }

        public TimeVerifier(Component component, String errorMsg) {
            this(component);
            this.errorMessage = errorMsg;
        }

        public boolean verify(JComponent input) {
            log.debug("[TimeVerifier] verify");
            timeLostFocus = true;
            boolean inRange = validTime();
            return inRange;
        }

        public boolean shouldYieldFocus(JComponent input) {
            boolean valid = super.shouldYieldFocus(input);

            // Work around for fixing Bug number X54325
            // Basically the MSeries datepickers mess around with the
            // focus when the dialog is closed by the window 'X' button
            // so a spurious call to this method is made.
            // When this button is pressed, the component isShowing() method
            // is false (when at other times it is true), so can be used
            // to resolve this problem.
            if (!valid && input.isShowing()) {
                log.debug("* * *   VERIFIER - FAILING TIME VALIDATION   * * *");
                input.requestFocus();
                JOptionPane.showMessageDialog(component, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
            }
            return valid;
        }
    }

}
package uk.gov.courtservice.xhibit.client.util;

/**
 * <p>Title: Xhibit2</p>
 * <p>Description: Court Services Application</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author David Crossland
 * @version 1.0
 */

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;

/**
 * @author unascribed
 * @version 1.0
 * @deprecated replaced by <code>XDatePanel</code>
 *             <p>
 *             Title:
 *             </p>
 *             <p>
 *             Description:
 *             </p>
 *             <p>
 *             Copyright: Copyright (c) 2003
 *             </p>
 *             <p>
 *             Company: Electronic Data Systems
 *             </p>
 */
public class DatePanel extends XPanel {

    private JPanel dateContainer;

    private JTextField outDate;

    private Image image;

    private ResourceBundle datePanelResources;

    private Dimension dayDateTextDim = new Dimension(20, 21);

    private Dimension monthDateCbDim = new Dimension(60, 21);

    private Dimension yearDateTextDim = new Dimension(40, 21);

    private Dimension panelDim = new Dimension(152, 24);

    private Dimension iconDim = new Dimension(24, 24);

    private JTextField dayDateText;

    private JComboBox monthDateCb;

    private JTextField monthDateText;

    private JTextField yearDateText;

    private JLabel calendarLabel;

    // public static final int SUCCESS = 0;
    // public static final int NO_DATA = 1;
    // public static final int DAY_IN_ERROR = 2;
    // public static final int MONTH_IN_ERROR = 4;
    // public static final int YEAR_IN_ERROR = 8;
    // public static final int DATE_IN_ERROR = 16;

    public static final String dateError = "validation.date";

    public static final boolean LENIENT = false; // true;

    private int selectedIndex;

    private String selectedMonth;

    private String[] listOfMonths = { "   ", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT",
            "NOV", "DEC" };

    private static final Logger log = CSServices.getLogger(DatePanel.class);

    public DatePanel(JPanel containingPanel) {

        try {
            this.dateContainer = containingPanel;
            URL u = getClass().getClassLoader().getResource(XHIBITConstant.imageRoot + "calendar.gif");
            image = Toolkit.getDefaultToolkit().getImage(u);
            stepInitialise();
            jbInit();
            stepActivate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stepInitialise() {
        try {
            datePanelResources = XHIBITConstant.getResourceBundle(XhibitBundles.UtilResources);
        } catch (java.util.MissingResourceException ex) {
            XHIBITConstant.error("ResourceBundle could not be found for " + Locale.getDefault());
            XHIBITConstant.error(ex);
        }
    }

    public void stepActivate() {
        // moveModelToScreen();
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        if (dateContainer instanceof XPanel) {
            ((XPanel) dateContainer).stepUpdateViewState();
        }
    }

    public void stepValidate() throws CSValidationException {
        try {
            validateDate(true);
        } catch (CSValidationException ex) {
            getDayDateText().requestFocus();
            throw ex;
        }
    }

    public void stepDeactivate() {
    }

    public void stepDeinitialise(boolean save) {
    }

    private void jbInit() {
        addDateFields();
    }

    private void addDateFields() {

        this.setLayout(new GridBagLayout());
        this.setPreferredSize(panelDim);
        this.setMinimumSize(panelDim);
        this.setMaximumSize(panelDim);
        this.add(getDayDateText(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));
        this.add(getYearDateText(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));
        this.add(getCalendarLabel(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getMonthDateCb(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));

        /*
         * this.dateText.addFocusListener(new FocusListener() { public void
         * focusLost(FocusEvent e) { validateDate(); } public void
         * focusGained(FocusEvent e) {} } );
         */
    }

    /**
     * validate the text for a valid date
     */
    /*
     * private void validateDate() {
     * 
     * if ( !getDayDateText().getText().equals("") &&
     * !getYearDateText().getText().equals("") ) { String fullDate =
     * getDayDateText().getText() + " " + selectedMonth + " " +
     * getYearDateText().getText(); JTextField tf = new JTextField(fullDate);
     * uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug(" fullDate : " +
     * fullDate); uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("
     * tf.getText() : " + tf.getText());
     * 
     * if (!fullDate.equals("")) { ParsePosition pos = new ParsePosition(0);
     * SimpleDateFormat sd = new SimpleDateFormat("dd MMM yyyy"); Date d =
     * sd.parse(fullDate, pos);
     * uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug(" d : " + d);
     * if (d==null) { CSValidationException csv = new
     * CSValidationException("Date is invalid: " + fullDate, new
     * Message("xxx")); XHIBITConstant.handleError(csv); } else {
     * tf.setText(XDateFormat.format(d, XDateFormat.DATEFORMAT)); } } outDate =
     * tf; uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug(" outDate : " +
     * fullDate); }
     */

    /**
     * Checks for mandatory fields, valid integers and a valid date.
     * 
     * @param lenient
     * @throws CSValidationException
     */
    private void validateDate(boolean lenient) throws CSValidationException {
        if (!isMandatoryFieldsCompleted())
            throw new CSValidationException(dateError, "All Time vos not entered");
        validateDay();
        validateMonth();
        validateYear();
        validateCompleteDate(lenient);
    }

    private void validateDay() throws CSValidationException {
        try {
            Integer.parseInt(getDayDateText().getText());
        } catch (NumberFormatException e) {
            throw new CSValidationException(dateError, "day not integer");
        }
    }

    private void validateMonth() throws CSValidationException {
        if (getMonthDateCb().getSelectedIndex() == 0) {
            throw new CSValidationException(dateError, "Month not selected");
        }
    }

    private void validateYear() throws CSValidationException {
        try {
            if (getYearDateText().getText().length() != 4) {
                throw new CSValidationException(dateError, "year not correctly entered");
            } else {
                Integer.parseInt(getYearDateText().getText());
            }
        } catch (NumberFormatException re) {
            throw new CSValidationException(dateError, "year not integer");
        }
    }

    /**
     * Used to just validate the date without any additional checking of
     * individual fields. All mandatory fields must be complete. An exception
     * will not be thrown if not. Use validateDate if mandatory checking is
     * required.
     * 
     * @param lenient
     * @throws CSValidationException
     */
    private void validateCompleteDate(boolean lenient) throws CSValidationException {
        if (isMandatoryFieldsCompleted()) {
            Calendar date = Calendar.getInstance();

            try {
                date.setLenient(lenient);
                date.set(Integer.parseInt(getYearDateText().getText()), getMonthDateCb().getSelectedIndex() - 1,
                        Integer.parseInt(getDayDateText().getText()));
                date.getTime();
            } catch (IllegalArgumentException e) {
                log.debug(e);
                throw new CSValidationException(dateError, "Date entered not valid");
            }
        }
    }

    public JTextField getDayDateText() {
        if (dayDateText == null) {
            dayDateText = new JTextField();
            dayDateText.setPreferredSize(dayDateTextDim);
            dayDateText.setHorizontalAlignment(SwingConstants.CENTER);
            dayDateText.setMinimumSize(dayDateTextDim);
            dayDateText.setMaximumSize(dayDateTextDim);
            uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug(" dayDateText is: " + dayDateText.getText());
            dayDateText.setToolTipText(XHIBITConstant.getResource(datePanelResources, "LogDateDays"));
            dayDateText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    dayDateText_keyReleased(e);
                }
            });

        }
        return dayDateText;
    }

    public JComboBox getMonthDateCb() {
        if (monthDateCb == null) {
            monthDateCb = new JComboBox();
            monthDateCb.setMaximumSize(monthDateCbDim);
            monthDateCb.setMinimumSize(monthDateCbDim);
            monthDateCb.setPreferredSize(monthDateCbDim);
            monthDateCb.setToolTipText(XHIBITConstant.getResource(datePanelResources, "LogDateMonths"));
            monthDateCb.addActionListener(new XAction() {
                // java.awt.event.ActionListener() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    monthDateCb_ActionPerformed(e);
                }
            });
            populateComboBox(getMonthDateCb(), listOfMonths);
        }
        return monthDateCb;
    }

    private void populateComboBox(JComboBox comboBox, String[] comboBoxData) {
        comboBox.removeAllItems();

        for (int x = 0; x < comboBoxData.length; x++) {
            comboBox.addItem(comboBoxData[x]);
        }
    }

    private void monthDateCb_ActionPerformed(ActionEvent e) throws CSRecoverableException {
        JComboBox cb = (JComboBox) e.getSource();
        selectedMonth = (String) cb.getSelectedItem();
        selectedIndex = cb.getSelectedIndex();
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug(" selectedIndex is: " + selectedIndex);
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug(" selectedMonth is: " + selectedMonth);
        stepUpdateViewState();
    }

    public JTextField getYearDateText() {
        if (yearDateText == null) {
            yearDateText = new JTextField();
            yearDateText.setPreferredSize(yearDateTextDim);
            yearDateText.setHorizontalAlignment(SwingConstants.CENTER);
            yearDateText.setMinimumSize(yearDateTextDim);
            yearDateText.setMaximumSize(yearDateTextDim);
            yearDateText.setToolTipText(XHIBITConstant.getResource(datePanelResources, "LogDateYears"));
            uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug(" yearDateText is: " + yearDateText.getText());
            yearDateText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    yearDateText_keyReleased(e);
                }
            });

            // RL: Removing focus lost listener as causing two pop-ups to
            // appear.
            // also unable to cancel.
            // yearDateText.addFocusListener(new FocusListener() {
            // public void focusLost(FocusEvent e) {
            // try
            // {
            // //stepValidate();
            // //Using individual field validation to stop error appearing
            // twice
            // validateYear();
            // validateCompleteDate(LENIENT);
            // }
            // catch (CSValidationException ex)
            // {
            // XHIBITConstant.handleError(ex);
            // getYearDateText().requestFocus();
            // }
            // }
            // public void focusGained(FocusEvent e) {}
            // }
            // );
        }
        return yearDateText;
    }

    private JLabel getCalendarLabel() {
        if (calendarLabel == null) {
            calendarLabel = new JLabel();
            calendarLabel.setIcon(new ImageIcon(image));
            calendarLabel.setPreferredSize(iconDim);
            calendarLabel.setMinimumSize(iconDim);
            calendarLabel.setMaximumSize(iconDim);
            calendarLabel.setToolTipText(XHIBITConstant.getResource(datePanelResources, "CalendarComponent"));
        }
        return calendarLabel;
    }

    // private void moveModelToScreen( ) {
    // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug( "In
    // moveModelToScreen" );

    // Calendar modelDate = Calendar.getInstance( );
    // getDayDateText().setText( modelDate.get( Calendar.DAY_OF_MONTH) +
    // "");
    // getMonthDateCb().setSelectedIndex( modelDate.get( Calendar.MONTH ) );
    // getYearDateText().setText( modelDate.get( Calendar.YEAR ) + "" );
    // }

    public JTextField getOutDate() {
        return outDate;
    }

    // stully
    public void setYearDateText(int param) {
        yearDateText.setText(Integer.toString(param));
    }

    // stully
    public void setDayDateText(int param) {
        dayDateText.setText((param < 10 ? "0" + Integer.toString(param) : Integer.toString(param)));
    }

    // stully
    public void setMonth(int param) {
        monthDateCb.setSelectedIndex(param + 1);
    }

    // stully
    public int getDay() {
        return Integer.parseInt(getDayDateText().getText());
    }

    // stully
    public int getMonth() {
        return getMonthDateCb().getSelectedIndex() - 1;
    }

    // stully
    public int getYear() {
        return Integer.parseInt(getYearDateText().getText());
    }

    void dayDateText_keyReleased(KeyEvent e) {
        try {
            if (dateContainer instanceof XPanel) {
                ((XPanel) dateContainer).stepUpdateViewState();
            }
        } catch (Exception ex) {
            log.fatal(ex, ex);
            throw new CSUnrecoverableException(ex);

        }
    }

    void yearDateText_keyReleased(KeyEvent e) {
        try {
            if (dateContainer instanceof XPanel) {
                ((XPanel) dateContainer).stepUpdateViewState();
            }
        } catch (Exception ex) {
            log.fatal(ex, ex);
            throw new CSUnrecoverableException(ex);

        }
    }

    public boolean isMandatoryFieldsCompleted() {
        boolean returnCode = true;

        if (getDayDateText().getText().trim().length() == 0 || getMonthDateCb().getSelectedIndex() == 0
                || getYearDateText().getText().trim().length() == 0) {
            returnCode = false;
        }

        return returnCode;
    }

    public Calendar getDateAsCalendar() {
        Calendar theDate = Calendar.getInstance();
        theDate.set(getYear(), getMonth(), getDay());

        return theDate;
    }
}

package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Date/Time/Freetext log panel
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Create this class passing in one of the four components required<br>
 * <ul>
 * <li>DATEPICKER</li>
 * <li>DATEPOPUP</li>
 * <li>TIME</li>
 * <li>FREETEXT</li>
 * <br>
 * For example:<br>
 * <code>new LogAuditPanel(LogAuditPanel.FREETEXT+LogAuditPanel.DATEPOPUP+LogAuditPanel.TIME, array, defaultDate)</code>
 * <p>
 * Date picker requires the array of date objects to be passed. <br>
 * For all other options, the date array can be null. <br>
 * The default date is used to set the default dates and time.
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
public class LogAuditPanel extends XPanel {
    private static final Logger LOG = CSServices.getLogger(LogAuditPanel.class);

    // Components
    public static final int FREETEXT = 1;

    public static final int DATEPOPUP = 2;

    public static final int DATEPICKER = 4;

    public static final int TIME = 8;

    // JComponents
    protected TitledBorder tb = new TitledBorder("");

    protected FreeTextPanel ftp = null;

    protected XDatePickerPanel xdp = null;

    protected XDatePanel xd = null;

    protected XTimePanel xt = null;

    protected JLabel freetextLabel = null;

    protected JLabel dateLabel = null;

    protected JLabel timeLabel = null;

    // Local Variables
    protected int requiredComponents = 0;

    protected Date defaultDate;

    protected List dateList = null;

    private final Dimension labelDim = new Dimension(75, XHIBITConstant.getLineHeight());

    protected String resources = XhibitBundles.SimpleEvent;

    /**
     * Creates a panel with a date popup field, time field and free text.
     * 
     * @throws CSRecoverableException
     */
    public LogAuditPanel() throws CSRecoverableException {
        this(FREETEXT + DATEPOPUP + TIME, null, Calendar.getInstance().getTime());
    }

    /**
     * @param components
     *            see notes above
     * @param listOfDates
     *            only required if DATEPICKER is one of the components
     * @param defaultDate
     *            the default date and time to appear in the components
     * @throws CSRecoverableException
     */
    public LogAuditPanel(int components, List listOfDates, Date defaultDate) throws CSRecoverableException {
        this(components, listOfDates, defaultDate, true);
    }

    public LogAuditPanel(int components, List listOfDates, Date defaultDate, boolean executeStepInitialise)
            throws CSRecoverableException {
        if ((components & DATEPICKER) == DATEPICKER) {
            if ((listOfDates == null) || (listOfDates.size() <= 0)) {
                throw new CSRecoverableException("xhibit.error.unexpected", "No values passed in for date list");
            } else {
                setDateList(listOfDates);
            }
        }
        requiredComponents = components;
        this.defaultDate = defaultDate;

        // only initialise if required to do so...
        if (executeStepInitialise) {
            stepInitialise();
        }
    }

    /**
     * Set a new list for the DATEPICKER drop down
     * 
     * @param list
     */
    public void setDateList(List list) {
        dateList = list;
    }

    public List getDateList() {
        return dateList;
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        CompoundBorder border2 = BorderFactory.createCompoundBorder(tb, BorderFactory.createEmptyBorder(0, 0, 0, 0));

        this.setBorder(border2);
        // this.setMinimumSize(defaultDim);
        // this.setPreferredSize(defaultDim);
        if ((requiredComponents & FREETEXT) == FREETEXT) {
            this.add(getFreetextLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
            this.add(getFreeText(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        }
        if ((requiredComponents & DATEPOPUP) == DATEPOPUP) {
            this.add(getDateLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
            this.add(getDatePopup(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        }
        if ((requiredComponents & DATEPICKER) == DATEPICKER) {
            this.add(getDateLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
            this.add(getDatePicker(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        }
        if ((requiredComponents & TIME) == TIME) {
            this.add(getTimeLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
            this.add(getTimePanel(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        }
    }

    /**
     * Get a handle on the FreeTextPanel
     * 
     * @return
     */
    public FreeTextPanel getFreeText() {
        if (ftp == null) {
            ftp = new FreeTextPanel(this);
        }
        return ftp;
    }

    /**
     * Get a handle on the DatePicker
     * 
     * @return
     */
    public XDatePickerPanel getDatePicker() {
        if (xdp == null) {
            xdp = new XDatePickerPanel(this, getDateList(), defaultDate);
        }
        return xdp;
    }

    /**
     * Get a handle on the DatePopup
     * 
     * @return
     */
    public XDatePanel getDatePopup() {
        if (xd == null) {
            xd = new XDatePanel(this);
        }
        return xd;
    }

    /**
     * Get a handle on the TimePanel
     * 
     * @return
     */
    public XTimePanel getTimePanel() {
        if (xt == null) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            xt = new XTimePanel(this, format);
        }
        return xt;
    }

    private JLabel getFreetextLabel() {
        if (freetextLabel == null) {
            freetextLabel = new JLabel();
            freetextLabel.setText(XHIBITConstant.getResource(resources, "lblFreeText"));
            freetextLabel.setPreferredSize(labelDim);
        }
        return freetextLabel;
    }

    protected JLabel getDateLabel() {
        if (dateLabel == null) {
            dateLabel = new JLabel();
            dateLabel.setText(XHIBITConstant.getResource(resources, "lblLogDate"));
            dateLabel.setPreferredSize(labelDim);
        }
        return dateLabel;
    }

    private JLabel getTimeLabel() {
        if (timeLabel == null) {
            timeLabel = new JLabel();
            timeLabel.setText(XHIBITConstant.getResource(resources, "lblLogTime"));
            timeLabel.setPreferredSize(labelDim);
        }
        return timeLabel;
    }

    // Getters
    /**
     * The text entered in the free text panel
     * 
     * @return
     */
    public String getFreeTextString() {
        if (ftp != null) {
            return ftp.getFreeText();
        }
        return "";
    }

    /**
     * The date selected in either the DATEPOPUP or DATEPICKER If both
     * components have been added then the date from DATEPOPUP is returned
     * 
     * @return
     * @throws CSValidationException
     */
    public Calendar getDate() throws CSValidationException {
        if ((requiredComponents & DATEPOPUP) == DATEPOPUP) {
            if (xd != null)
                return xd.getDate();
        }
        if ((requiredComponents & DATEPICKER) == DATEPICKER) {
            if (xdp != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(xdp.getDate());
                return c;
            }
        }
        return null;
    }

    /**
     * Returns the time from the time panel as a Calendar object. The date
     * portion of this object shows the current date.
     * 
     * @return
     * @throws CSValidationException
     */
    public Calendar getTime() throws CSValidationException {
        if (xt != null) {
            return getTimeForDate(Calendar.getInstance());
        }
        return null;
    }

    /**
     * If both the date and times components are included, a calendar object is
     * returned combining the data from the date and time panels.
     * 
     * @return
     * @throws CSValidationException
     */
    public Calendar getDateTime() throws CSValidationException {
        Calendar d;
        if ((requiredComponents & DATEPOPUP) == DATEPOPUP) {
            if (xd != null) {
                d = xd.getDate();
                d = getTimeForDate(d);
                return d;
            }
        }
        if ((requiredComponents & DATEPICKER) == DATEPICKER) {
            if (xdp != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(xdp.getDate());
                return getTimeForDate(c);
            }
        }
        return null;
    }

    /**
     * Enable or disable one of the components
     * 
     * @param component
     * @param enabled
     */
    public void setComponentEnabled(int component, boolean enabled) {
        switch (component) {
        case FREETEXT:
            if (getFreeText() != null)
                getFreeText().getFreeTextArea().setEnabled(enabled);
            break;
        case DATEPICKER:
            if (getDatePicker() != null)
                getDatePicker().setDateEnabled(enabled);
            break;
        case DATEPOPUP:
            if (getDatePopup() != null)
                getDatePopup().setDateEnabled(enabled);
            break;
        case TIME:
            if (getTimePanel() != null)
                getTimePanel().setEnabled(enabled);
        }
    }

    public void setComponentEditable(int component, boolean editable) {
        switch (component) {
        case FREETEXT:
            if (getFreeText() != null)
                getFreeText().getFreeTextArea().setEditable(editable);
            break;
        case DATEPICKER:
            if (getDatePicker() != null)
                getDatePicker().setDateEditable(editable);
            break;
        case DATEPOPUP:
            if (getDatePopup() != null)
                getDatePopup().setDateEditable(editable);
            break;
        case TIME:
            if (getTimePanel() != null)
                getTimePanel().setEditable(editable);
        }
    }

    public void setComponentRequireed(int component, boolean required) {
        switch (component) {
        case FREETEXT:
            if (getFreeText() != null)
                getFreeText().setRequired(required);
            break;
        case DATEPICKER:
            if (getDatePicker() != null)
                getDatePicker().setRequired(required);
            break;
        case DATEPOPUP:
            if (getDatePopup() != null)
                getDatePopup().setRequired(required);
            break;
        case TIME:
            if (getTimePanel() != null)
                getTimePanel().setRequired(required);
        }
    }

    /**
     * This method is used to add the seconds onto the time so the court log can
     * be sorted correctly.
     * 
     * @param date
     * @return
     */
    private Calendar getTimeForDate(Calendar date) throws CSValidationException {
        return ((xt != null) ? xt.getTimeForDate(date) : null);
    }

    // Setters for defaults
    /**
     * Set new free text
     * 
     * @param newText
     */
    public void setFreeTextString(String newText) {
        if (ftp != null) {
            getFreeText().setFreeTextArea(newText);
        }
    }

    /**
     * Set a date in the date components
     * 
     * @param date
     */
    public void setDateDefault(Date date) {
        if ((xdp != null) && ((requiredComponents & DATEPICKER) == DATEPICKER)) {
            LOG.debug("xdp.setDate(" + date + ")");
            xdp.setDate(date);
        }

        if ((xd != null) && ((requiredComponents & DATEPOPUP) == DATEPOPUP)) {
            LOG.debug("xd.setDate(" + date + ")");
            xd.setDate(date);
        }
    }

    /**
     * Set a date in the date components
     * 
     * @param calendar
     */
    public void setDateDefault(Calendar calendar) {
        Date date = ((calendar != null) ? calendar.getTime() : null);
        setDateDefault(date);
    }

    /**
     * Set a time in the time components
     * 
     * @param date
     */
    public void setTimeDefault(Date time) {
        if (xt != null) {
            xt.setTime(time);
            Calendar c = Calendar.getInstance();
            c.setTime(time);
        }
    }

    /**
     * Set a time in the time components
     * 
     * @param date
     */
    public void setTimeDefault(Calendar time) {
    	if (time != null) {
    		LOG.debug("setTimeDefault(" + time.getTime() + ")");
            setTimeDefault(time.getTime());
    	}
    }

    /**
     * Change the text in the title border
     * 
     * @param date
     */
    public void setTitledBorderText(String title) {
        tb.setTitle(title);
    }

    /**
     * Gets an array of hearing dates for the currently active case in the
     * xhibit application window
     * 
     * @param controller
     * @return
     * @throws CSRecoverableException
     */
    public static ArrayList getShvDateList(XhibitApplicationController controller) throws CSRecoverableException {
        if (controller == null) {
            return new ArrayList();
        }

        return (ArrayList) controller.getApplicationCaseModel().getAllScheduledHearingDatesForCase(true);
    }

    public void stepInitialise() {
        init();
    }

    public void stepActivate() {
    }

    /**
     * Calls step validate on its sub components
     * 
     * @throws CSValidationException
     */
    public void stepValidate() throws CSValidationException {
        if (ftp != null)
            ftp.stepValidate();
        if (xdp != null)
            xdp.stepValidate();
        if (xd != null)
            xd.stepValidate();
        if (xt != null)
            xt.stepValidate();
    }

    /**
     * Call stepUpdateViewState on the owning panel, if that panel is an XPanel
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        if ((getParent() != null) && (getParent() instanceof XPanel)) {
            ((XPanel) getParent()).stepUpdateViewState();
        }
    }

    public void stepDeactivate() {
    }

    public void stepDeinitialise(boolean update) {
    }
}

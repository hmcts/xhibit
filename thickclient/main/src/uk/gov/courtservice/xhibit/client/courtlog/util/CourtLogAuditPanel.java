package uk.gov.courtservice.xhibit.client.courtlog.util;

import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.LogAuditPanel;
import uk.gov.courtservice.xhibit.client.util.XDatePickerPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * An implementation of the <code>LogAuditPanel</code> that is to be used
 * specifically by court log events, that can be used to process the dates as a
 * list of the scheduled hearing dates, with custom displayed data.
 * 
 * @author tz0d5m
 * @version $Revision: 1.7 $
 */
public class CourtLogAuditPanel extends LogAuditPanel {
    /**
     * Constant represents that only the date picker and time components should
     * be displayed.
     */
    public static final int DATEPICKER_AND_TIME = LogAuditPanel.DATEPICKER + LogAuditPanel.TIME;

    /** The default components that are displayed for court log events */
    private static final int DATEPICKER_AND_TIME_AND_FREETEXT = DATEPICKER_AND_TIME + LogAuditPanel.FREETEXT;

    /** The scheduled hearing id that should be used as the default */
    private final Integer defaultScheduledHearingId;

    /**
     * The default constructor for court log events used to instantiate this log
     * audit panel. The components included will be the free text field, the
     * scheduled hearing date picker and the time.
     * 
     * @see CourtLogAuditPanel(int, java.util.List, java.lang.Integer)
     */
    public CourtLogAuditPanel(List values, Integer defaultScheduledHearingId) throws CSRecoverableException {
        this(DATEPICKER_AND_TIME_AND_FREETEXT, values, defaultScheduledHearingId);
    }

    /**
     * Constructor for court log events used to instantiate this log audit
     * panel. The components included will be determined by the passed in
     * components parameter.
     * 
     * @param components
     *            An <code>int</code> value used to indicate which components
     *            should be displayed.
     * @param values
     *            A <code>List</code> containing
     *            <code>CourtLogScheduledHearingValue</code> objects that are
     *            to be displayed.
     * @param defaultScheduledHearingId
     *            The scheduled hearing id of the scheduled hearing that is to
     *            be selected from the list of values by default
     * @throws CSRecoverableException
     *             If an error is thrown from the parent classes constructor.
     * 
     * @see #getValues(java.lang.Integer)
     * @see uk.gov.courtservice.xhibit.client.util.LogAuditPanel (int,
     *      java.util.List, java.util.Date)
     */
    public CourtLogAuditPanel(int components, List values, Integer defaultScheduledHearingId)
            throws CSRecoverableException {
        super(components, values, null, false);

        this.defaultScheduledHearingId = defaultScheduledHearingId;
        stepInitialise();
    }

    /**
     * Method to acquire the <code>List</code> of
     * <code>CourtLogScheduledHearingValue</code>s that is to be used by the
     * <code>CourtLogAuditPanel</code> to display and select which scheduled
     * hearing date/type should be used.
     * 
     * @param caseId
     *            The case id that we want to acquire all of the scheduled
     *            hearings for.
     * @return A <code>List</code> of
     *         <code>CourtLogScheduledHearingValue</code>
     */
    public static List getValues(Integer caseId) {
        // the LogAuditPanel requires things to be in a List
        return Arrays.asList(XhibitDelegateHelper.getCourtLogDelegate2().getCourtLogScheduledHearingValuesForCase(
                caseId));
    }

    /**
     * Over-ridden method that forces the <code>CourtLogAuditPanel</code> to
     * use the court log specific date picker panel.
     * 
     * @see uk.gov.courtservice.xhibit.client.util.LogAuditPanel
     *      #getDatePicker()
     * @see uk.gov.courtservice.xhibit.client.courtlog.util
     *      .CourtLogXDatePickerPanel
     */
    public XDatePickerPanel getDatePicker() {
        if (xdp == null) {
            xdp = new CourtLogXDatePickerPanel(this, getDateList(), defaultScheduledHearingId);
        }

        return xdp;
    }

    /**
     * Accessor method for the selected scheduled hearings id. This method will
     * delegate to the getter on the <code>CourtLogXDatePickerPanel</code>
     * 
     * @return An <code>Integer</code> containing the scheduled hearing id.
     * @see uk.gov.courtservice.xhibit.client.courtlog.util
     *      .CourtLogXDatePickerPanel#getScheduledHearingId()
     */
    public Integer getScheduledHearingId() {
        return ((xdp != null) ? ((CourtLogXDatePickerPanel) xdp).getScheduledHearingId() : null);
    }

    /**
     * Setter method for the selected scheduled hearing id. This method will
     * delegate to the setter on the <code>CourtLogXDatePickerPanel</code>.
     * 
     * @return An <code>Integer</code> containing the scheduled hearing id.
     * @see uk.gov.courtservice.xhibit.client.courtlog.util
     *      .CourtLogXDatePickerPanel#setScheduledHearingId(java.lang.Integer)
     */
    public void setScheduledHearingId(Integer scheduledHearingId) {
        if (xdp != null) {
            ((CourtLogXDatePickerPanel) xdp).setScheduledHearingId(scheduledHearingId);
        }
    }

    /**
     * Overridden method that allows the setting of a more descriptive label
     * when used for court log events.
     * 
     * @return The more descriptive <code>JLabel</code>.
     * @see uk.gov.courtservice.xhibit.client.util.LogAuditPanel#getDateLabel()
     */
    protected JLabel getDateLabel() {
        // @todo - need to override to get a better label...
        return super.getDateLabel();
    }
}

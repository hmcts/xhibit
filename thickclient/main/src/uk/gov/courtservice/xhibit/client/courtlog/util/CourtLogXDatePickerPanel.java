package uk.gov.courtservice.xhibit.client.courtlog.util;

import java.awt.Dimension;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import uk.gov.courtservice.xhibit.client.util.XDatePickerPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogScheduledHearingValue;

/**
 * Custom implementation of the <code>XDatePickerPanel</code> that allows the
 * selection of dates to be based upon the scheduled hearing dates.
 * 
 * @author tz0d5m
 * @version $Revision: 1.9 $
 */
public class CourtLogXDatePickerPanel extends XDatePickerPanel {
    private final Integer defaultScheduledHearingId;

    public CourtLogXDatePickerPanel(JPanel containingPanel, List collectionOfDates, Integer defaultScheduledHearingId) {
        super(containingPanel, collectionOfDates);

        log.debug("Constructor - defaultScheduledHearingId = " + defaultScheduledHearingId);
        this.defaultScheduledHearingId = defaultScheduledHearingId;
        stepActivate();
    }

    /**
     * Set the list of scheduled hearings that can be selected.
     * 
     * @param values
     *            A <code>List</code> containing
     *            <code>CourtLogScheduledHearingValue</code>
     * @see uk.gov.courtservice.xhibit.client.util.XDatePickerPanel
     *      #setListOfValidDates(java.util.List)
     */
    protected void setListOfValidDates(List values) {
        Object[] array = values.toArray(new CourtLogScheduledHearingValue[values.size()]);
        log.debug("setListOfValidDates() - There are " + array.length + " possible dates");
        setListOfValidDates(array);
    }

    public void stepActivate() {
        setScheduledHearingId(defaultScheduledHearingId);
    }

    public void setScheduledHearingId(Integer scheduledHearingId) {
        log.debug("setScheduledHearingId() - trying to change to " + scheduledHearingId);

        if (scheduledHearingId != null) {
            final Iterator it = super.collectionOfDates.iterator();

            for (int i = 0; it.hasNext(); i++) {
                CourtLogScheduledHearingValue value = (CourtLogScheduledHearingValue) it.next();

                if (scheduledHearingId.equals(value.getScheduledHearingId())) {
                    log.debug("setScheduledHearingId() - Setting to " + scheduledHearingId + " at index " + i);
                    super.dropDownDate.setSelectedIndex(i);
                    return;
                }
            }
        }

        log.debug("setScheduledHearingId() - did not change to " + scheduledHearingId);
    }

    public void setDate(Date date) {
        // do nothing as we only want to allow setting of the scheduled hearing
        // id
    }

    public Date getDate() {
        final Date returnDate = getSelectedValue().getDate();
        log.debug("getDate() - returning - " + returnDate);
        return returnDate;
    }

    public Integer getScheduledHearingId() {
        final Integer scheduledHearingId = getSelectedValue().getScheduledHearingId();
        log.debug("getScheduledHearingId() - returning - " + scheduledHearingId);
        return scheduledHearingId;
    }

    public CourtLogScheduledHearingValue getSelectedValue() {
        return (CourtLogScheduledHearingValue) getDropDownDate().getSelectedItem();
    }

    /**
     * Overridden method that allows for the slightly larger size of the drop
     * down since the extra fields have been included.
     * 
     * @return A <code>Dimension</code> that is the preferred size of the date
     *         label for this component.
     * @see uk.gov.courtservice.xhibit.client.util.XDatePickerPanel
     *      #getDimension()
     */
    protected Dimension getDimension() {
        return new Dimension(150, XHIBITConstant.getLineHeight());
    }
}

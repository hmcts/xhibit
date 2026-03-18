package uk.gov.courtservice.xhibit.business.services.witness.schedule;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessValue;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.InvalidArrivedDateException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.InvalidNetworkException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.InvalidNoteException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummary;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @see uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail
 * @author Neil Ellis
 * 
 */
public class WitnessDetailImpl extends WitnessSummaryImpl implements WitnessDetail {
    private static final Logger log = CSServices.getLogger(WitnessDetailImpl.class);
    
    private static final long serialVersionUID = -9194065621688209539L;

    public void setWitnessValue(XhbWitnessValue witnessValue) {
        this.witnessValue = witnessValue;
    }

    public WitnessDetailImpl(final XhbWitnessValue witnessValue) {
        super(witnessValue);
    }

    public void setName(final String name) {
        witnessValue.setName(name);
        markAsModified();
    }

    public void setStatus(final String status) {
        witnessValue.setStatus(status);
        markAsModified();
    }

    public void setAge(final int age) {
        witnessValue.setAge((short) age);
        markAsModified();
    }

    public Time getExpected() {
        final Time expectedArrivalTime = witnessValue.getExpectedArrivalTime();
        if (expectedArrivalTime == null) {
            return null;
        }
        return expectedArrivalTime;
    }

    public WitnessDetail getAssociatedWitnessDetail() {
        return null;
    }

    public void setExpected(final Time time) {
        if (time == null) {
            witnessValue.setExpectedArrivalTime(null);
        }
        witnessValue.setExpectedArrivalTime(time);
        markAsModified();
    }

    public void setArrived(final Date date) throws InvalidArrivedDateException // Note:
    // date
    // part
    // should
    // be
    // today.
    {
        if (date == null) {
            witnessValue.setActualArrivalDateTime(null);
        } else {
            witnessValue.setActualArrivalDateTime(new Timestamp(date.getTime()));
        }
        markAsModified();
    }

    public void setPagerNetwork(final String network) throws InvalidNetworkException // Note:network
                                                                                        // should
                                                                                        // be
                                                                                        // one
                                                                                        // of
    // the ones available
    {
        witnessValue.setPagernet(network);
        markAsModified();
    }

    public String getMobileNumber() {
        return witnessValue.getMobilenumber();
    }

    public void setMobileNumber(final String number) {
        witnessValue.setMobilenumber(number);
        markAsModified();
    }

    public void setNotes(final String note) throws InvalidNoteException // Note:
    // must
    // be
    // less
    // than
    // 255
    // chars
    // long
    // (and
    // not
    // null)
    {
        if (note.length() > MAXIMUM_NOTE_LENGTH) {
            throw new InvalidNoteException("WITNESS_XXX", "Note supplied had length of:" + note.length()
                    + " maximum is" + MAXIMUM_NOTE_LENGTH);
        }
        witnessValue.setNotes(note);
        markAsModified();
    }

    public String getPagerNumber() {
        return witnessValue.getPagernumber();
    }

    public void setPagerNumber(final String number) {
        witnessValue.setPagernumber(number);
        markAsModified();
    }

    public String getPagerNetwork() {
        return witnessValue.getPagernet();
    }

    public String getNotes() {
        return witnessValue.getNotes();
    }

    public int getDayNumber() {
        return 0;
    }

    public String getType() {
        return witnessValue.getWitnessType();
    }

    public void setType(final String type) {
        witnessValue.setWitnessType(type);
        markAsModified();
    }

    /**
     * Converts a WitnessSession to a WitnessSummary.
     * 
     * @return
     */
    public WitnessSummary getAssociatedWitnessSummary() {
        return this;
    }

    public void update() throws ModificationException {
        try {
            if (isModified()) {
                WitnessDetail witnessDetail = WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                        .updateWitnessDetail(this);
                setWitnessValue(((WitnessDetailImpl) witnessDetail).getWitnessValue());
            }
        } catch (WitnessNotFoundException e) {
            log.error(e);
            throw new WitnessModificationException("WITNESS_XXX",
                    "Could not find the witness to update it: " + getId(), e);
        }
    }

    public void remove() throws ModificationException {
        try {
            if (isModified()) {
                throw new WitnessModificationException("WITNESS_XXX", "Cannot delete a modified witness:"
                        + this.getId());
            }
            WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance().removeWitnessDetail(this);
        } catch (WitnessNotFoundException e) {
            log.error(e);
            throw new WitnessModificationException("WITNESS_XXX", "Could nor find the witness:" + this.getId(), e);
        }
    }

    public String getWitnessType() {
        return witnessValue.getWitnessType();
    }

    public int compareTo(final Object o) {
        return compareTo((WitnessDetail) o);
    }
}

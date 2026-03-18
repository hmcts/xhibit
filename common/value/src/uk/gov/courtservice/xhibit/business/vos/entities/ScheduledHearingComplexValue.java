package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Collection;

/**
 * <p>
 * Title: ScheduledHearingComplexValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * Scheduled Hearing enitity CMR fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class ScheduledHearingComplexValue extends ScheduledHearingBasicValue {
	private static final long serialVersionUID = 5401197651541027315L;
	
    private HearingBasicValue hearing;

    private SittingBasicValue sitting;

    private Collection schedHearingAttendee;

    private Collection schedHearingDefendants;

    public ScheduledHearingComplexValue() {
        super();
    }

    public ScheduledHearingComplexValue(Integer version) {
        super(version);
    }

    public ScheduledHearingComplexValue(Integer scheduledHearingID, Integer version) {
        super(scheduledHearingID, version);
    }

    /*
     * (public ScheduledHearingComplexValue(Integer scheduledHearingID, Integer
     * sequenceNo, Integer hearingProgress, SittingBasicValue sitting,
     * HearingBasicValue hearing, Integer version, Integer refCourtReporterID,
     * Integer linkedSHID, Date notBeforeTime, Date originalTime, Date
     * lastUpdateDate, Date creationDate, Date endTime, Date startTime, Date
     * dateOfHearing, String listingNote, String currentStatus, String
     * movedFrom, String lastUpdatedBy, String createdBy) {
     * super(scheduledHearingID, sequenceNo, hearingProgress,
     * sitting.getSittingID(), hearing.getHearingID(), version,
     * refCourtReporterID, linkedSHID, notBeforeTime, originalTime,
     * lastUpdateDate, creationDate, endTime, startTime, dateOfHearing,
     * listingNote, currentStatus, movedFrom, lastUpdatedBy, createdBy);
     * 
     * this.hearing = hearing; this.sitting = sitting; }
     */

    public HearingBasicValue getHearing() {
        return hearing;
    }

    public void setHearing(HearingBasicValue hearing) {
        this.hearing = hearing;
    }

    public void setSitting(SittingBasicValue sitting) {
        this.sitting = sitting;
    }

    public SittingBasicValue getSitting() {
        return sitting;
    }

    public void setSchedHearingAttendee(Collection schedHearingAttendee) {
        this.schedHearingAttendee = schedHearingAttendee;
    }

    public Collection getSchedHearingAttendee() {
        return schedHearingAttendee;
    }

    public void setSchedHearingDefendants(Collection schedHearingDefendants) {
        this.schedHearingDefendants = schedHearingDefendants;
    }

    public Collection getSchedHearingDefendants() {
        return schedHearingDefendants;
    }
}
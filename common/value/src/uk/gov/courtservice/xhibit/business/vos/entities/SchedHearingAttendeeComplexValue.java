package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title: SchedHearingAttendeeComplexValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * SchedHearingAttendee enitity CMR fields.
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

public class SchedHearingAttendeeComplexValue extends SchedHearingAttendeeBasicValue {

	static final long serialVersionUID = 7322142594065831067L;
	
	private ScheduledHearingBasicValue scheduledHearing;

    private SHStaffBasicValue shStaff;

    private SHJusticeBasicValue shJustice;

    private SHLegRepBasicValue shLegRep;

    public SchedHearingAttendeeComplexValue() {
        super();
    }

    public SchedHearingAttendeeComplexValue(Integer version) {
        super(version);
    }

    public SchedHearingAttendeeComplexValue(Integer schedHearingAttendeeID, Integer version) {
        super(schedHearingAttendeeID, version);
    }

    public ScheduledHearingBasicValue getScheduledHearing() {
        return scheduledHearing;
    }

    public void setScheduledHearing(ScheduledHearingBasicValue scheduledHearing) {
        this.scheduledHearing = scheduledHearing;
    }

    public void setShStaff(SHStaffBasicValue shStaff) {
        this.shStaff = shStaff;
    }

    public SHStaffBasicValue getShStaff() {
        return shStaff;
    }

    public void setShJustice(SHJusticeBasicValue shJustice) {
        this.shJustice = shJustice;
    }

    public SHJusticeBasicValue getShJustice() {
        return shJustice;
    }

    public void setShLegRep(SHLegRepBasicValue shLegRep) {
        this.shLegRep = shLegRep;
    }

    public SHLegRepBasicValue getShLegRep() {
        return shLegRep;
    }
}
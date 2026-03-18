package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title: SHStaffComplexValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the SHStaff
 * enitity CMR fields.
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

public class SHStaffComplexValue extends SHStaffBasicValue {
	
	private static final long serialVersionUID = 4796954853714072214L;
    private SchedHearingAttendeeBasicValue schedHearingAttendee;

    public SHStaffComplexValue() {
        super();
    }

    public SHStaffComplexValue(Integer version) {
        super(version);
    }

    public SHStaffComplexValue(Integer shStaffID, Integer version) {
        super(shStaffID, version);
    }

    public SchedHearingAttendeeBasicValue getSchedHearingAttendee() {
        return schedHearingAttendee;
    }

    public void setSchedHearingAttendee(SchedHearingAttendeeBasicValue schedHearingAttendee) {
        this.schedHearingAttendee = schedHearingAttendee;
    }
}
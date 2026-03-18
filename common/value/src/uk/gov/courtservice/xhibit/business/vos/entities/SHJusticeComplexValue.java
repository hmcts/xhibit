package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title: SHJusticeComplexValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * SHJustice enitity CMR fields.
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

public class SHJusticeComplexValue extends SHJusticeBasicValue {

	private static final long serialVersionUID = 5310454917715513659L;
    private SchedHearingAttendeeBasicValue schedHearingAttendee;

    public SHJusticeComplexValue() {
        super();
    }

    public SHJusticeComplexValue(Integer version) {
        super(version);
    }

    public SHJusticeComplexValue(Integer shJusticeID, Integer version) {
        super(shJusticeID, version);
    }

    public SchedHearingAttendeeBasicValue getSchedHearingAttendee() {
        return schedHearingAttendee;
    }

    public void setSchedHearingAttendee(SchedHearingAttendeeBasicValue schedHearingAttendee) {
        this.schedHearingAttendee = schedHearingAttendee;
    }
}
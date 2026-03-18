package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: SchedHearingAttendeeBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * SchedHearingAttendee enitity CMP fields.
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

public class SchedHearingAttendeeBasicValue extends CSAbstractValue {
	
	static final long serialVersionUID = -5609681965028891491L;
	
    private Integer sheduledHearingID;

    private String attendeeType;

    private Integer shStaffID;

    private Integer shJusticeID;

    private Integer refJudgeID;

    private Integer refCourtReporterID;

    private Integer refJusticeID;

    public SchedHearingAttendeeBasicValue() {
        super();
    }

    public SchedHearingAttendeeBasicValue(Integer version) {
        super(version);
    }

    public SchedHearingAttendeeBasicValue(Integer shAttendeeID, Integer version) {
        super(shAttendeeID, version);
    }

    public void setSheduledHearingID(Integer sheduledHearingID) {
        this.sheduledHearingID = sheduledHearingID;
    }

    public Integer getSheduledHearingID() {
        return sheduledHearingID;
    }

    public void setAttendeeType(String attendeeType) {
        this.attendeeType = attendeeType;
    }

    public String getAttendeeType() {
        return attendeeType;
    }

    public void setShStaffID(Integer shStaffID) {
        this.shStaffID = shStaffID;
    }

    public Integer getShStaffID() {
        return shStaffID;
    }

    public void setShJusticeID(Integer shJusticeID) {
        this.shJusticeID = shJusticeID;
    }

    public Integer getShJusticeID() {
        return shJusticeID;
    }

    public void setRefJudgeID(Integer refJudgeID) {
        this.refJudgeID = refJudgeID;
    }

    public Integer getRefJudgeID() {
        return refJudgeID;
    }

    public void setRefCourtReporterID(Integer refCourtReporterID) {
        this.refCourtReporterID = refCourtReporterID;
    }

    public Integer getRefCourtReporterID() {
        return refCourtReporterID;
    }

    public void setRefJusticeID(Integer refJusticeID) {
        this.refJusticeID = refJusticeID;
    }

    public Integer getRefJusticeID() {
        return refJusticeID;
    }

}

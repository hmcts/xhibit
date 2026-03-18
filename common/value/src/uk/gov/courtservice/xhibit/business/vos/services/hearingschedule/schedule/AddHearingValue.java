package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: AddHearingValue
 * </p>
 * <p>
 * Description: Value Object for creation of a new hearings to be added to the
 * daily schedule.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * 
 * <Change History/>
 * 
 * <P>
 * 25/MAY/2004 - SET - First issue.
 * </P>
 */

public class AddHearingValue extends CSAbstractValue {
	private static final long serialVersionUID = -6250466598454465762L;
    private Integer caseNumber;

    private String caseType;

    private Integer courtId;

    private Integer courtRoomId;

    private java.util.Collection defendants;

    private java.util.Date notBeforeTime;

    private Integer refJudgeID;

    private Integer refHearingTypeId;

    public AddHearingValue() {
    }

    public Integer getRefJudgeID() {
        return refJudgeID;
    }

    public void setRefJudgeID(Integer refJudgeID) {
        this.refJudgeID = refJudgeID;
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public Integer getRefHearingTypeId() {
        return refHearingTypeId;
    }

    public void setCourtRoomId(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    public Integer getCourtRoomId() {
        return courtRoomId;
    }

    public void setDefendants(java.util.Collection defendants) {
        this.defendants = defendants;
    }

    public java.util.Collection getDefendants() {
        return defendants;
    }

    public void setNotBeforeTime(java.util.Date notBeforeTime) {
        this.notBeforeTime = notBeforeTime;
    }

    public java.util.Date getNotBeforeTime() {
        return notBeforeTime;
    }

    public void setRefHearingTypeId(Integer refHearingTypeId) {
        this.refHearingTypeId = refHearingTypeId;
    }
}
package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: MoveCaseValue
 * </p>
 * <p>
 * Description: Value Object for moving a Scheduled Hearing within the Todays
 * Schedule.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Paul Fitton
 * @version $Id: MoveCaseValue.java,v 1.3 2006/06/05 12:28:47 bzjrnl Exp $
 * 
 * <Change History/>
 * 
 * <P>
 * 11/02/03 - PDF - First issue.
 * </P>
 */

public class MoveCaseValue extends CSAbstractValue {
	private static final long serialVersionUID = 4907446978798122847L;
    private Integer scheduledHearingId;

    private Integer newCourtRoomId;

    private Integer courtId;

    private boolean useExistingJudge;

    private boolean useExistingCourtClerk;

    private boolean useExistingSHWriter;

    private boolean adjourned;

    private java.util.Date newHearingTime;

    private Integer existingJudgeId;

    private Integer oldCourtRoomId;

    private Integer existingCourtClerkId;

    private Integer exisitingSHWriterId;

    private Integer linkedSHId;

    public MoveCaseValue() {
    }

    public Integer getScheduledHearingId() {
        return scheduledHearingId;
    }

    public void setScheduledHearingId(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }

    public void setNewCourtRoomId(Integer newCourtRoomId) {
        this.newCourtRoomId = newCourtRoomId;
    }

    public Integer getNewCourtRoomId() {
        return newCourtRoomId;
    }

    public void setUseExistingJudge(boolean useExistingJudge) {
        this.useExistingJudge = useExistingJudge;
    }

    public boolean isUseExistingJudge() {
        return useExistingJudge;
    }

    public void setUseExistingCourtClerk(boolean useExistingCourtClerk) {
        this.useExistingCourtClerk = useExistingCourtClerk;
    }

    public boolean isUseExistingCourtClerk() {
        return useExistingCourtClerk;
    }

    public void setUseExistingSHWriter(boolean useExistingSHWriter) {
        this.useExistingSHWriter = useExistingSHWriter;
    }

    public boolean isUseExistingSHWriter() {
        return useExistingSHWriter;
    }

    public void setAdjourned(boolean adjourned) {
        this.adjourned = adjourned;
    }

    public boolean isAdjourned() {
        return adjourned;
    }

    public void setNewHearingTime(java.util.Date newHearingTime) {
        this.newHearingTime = newHearingTime;
    }

    public java.util.Date getNewHearingTime() {
        return newHearingTime;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public void setExistingJudgeId(Integer judgeId) {
        this.existingJudgeId = judgeId;
    }

    public Integer getExistingJudgeId() {
        return existingJudgeId;
    }

    public void setOldCourtRoomId(Integer oldCourtRoomId) {
        this.oldCourtRoomId = oldCourtRoomId;
    }

    public Integer getOldCourtRoomId() {
        return oldCourtRoomId;
    }

    public void setExistingCourtClerkId(Integer ccId) {
        this.existingCourtClerkId = ccId;
    }

    public Integer getExistingCourtClerkId() {
        return existingCourtClerkId;
    }

    public void setExisitingSHWriterId(Integer shId) {
        this.exisitingSHWriterId = shId;
    }

    public Integer getExisitingSHWriterId() {
        return exisitingSHWriterId;
    }

    public void setLinkedSHId(Integer linkedSHId) {
        this.linkedSHId = linkedSHId;
    }

    public Integer getLinkedSHId() {
        return linkedSHId;
    }

}
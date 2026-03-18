package uk.gov.courtservice.xhibit.business.vos.entities;

//JDK
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: ScheduledHearingBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * Scheduled Hearing enitity CMP fields.
 * </p>
 * <P>
 * <B>The following fields must be populated in order to create a scheduled
 * hearing: <BR>- sequenceNo <BR>- sittingId <BR>- hearingId</B>
 * </P>
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

public class ScheduledHearingBasicValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private Integer sequenceNo;

    private Integer hearingProgress;

    private Integer sittingID;

    private Integer hearingID;

    private Integer linkedSHID;

    private Date notBeforeTime;

    private Date originalTime;

    private Date endTime;

    private Date startTime;

    private Date dateOfHearing;

    private String listingNote;

    private String movedFrom;

    private Boolean isCaseActive;

    private Integer movedFromCourtRoomId;
    
    private String addHearingUsed;
    
    private Integer refCrackedEffectiveId;

    public ScheduledHearingBasicValue() {
        super();
    }

    public ScheduledHearingBasicValue(Integer version) {
        super(version);
    }

    public ScheduledHearingBasicValue(Integer scheduledHearingID, Integer version) {
        super(scheduledHearingID, version);
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setHearingProgress(Integer hearingProgress) {
        this.hearingProgress = hearingProgress;
    }

    public Integer getHearingProgress() {
        return hearingProgress;
    }

    public void setSittingID(Integer sittingID) {
        this.sittingID = sittingID;
    }

    public Integer getSittingID() {
        return sittingID;
    }

    public void setHearingID(Integer hearingID) {
        this.hearingID = hearingID;
    }

    public Integer getHearingID() {
        return hearingID;
    }

    public void setLinkedSHID(Integer linkedSHID) {
        this.linkedSHID = linkedSHID;
    }

    public Integer getLinkedSHID() {
        return linkedSHID;
    }

    public void setNotBeforeTime(Date notBeforeTime) {
        this.notBeforeTime = notBeforeTime;
    }

    public Date getNotBeforeTime() {
        return notBeforeTime;
    }

    public void setOriginalTime(Date originalTime) {
        this.originalTime = originalTime;
    }

    public Date getOriginalTime() {
        return originalTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setDateOfHearing(Date dateOfHearing) {
        this.dateOfHearing = dateOfHearing;
    }

    public Date getDateOfHearing() {
        return dateOfHearing;
    }

    public void setListingNote(String listingNote) {
        this.listingNote = listingNote;
    }

    public String getListingNote() {
        return listingNote;
    }

    public void setMovedFrom(String movedFrom) {
        this.movedFrom = movedFrom;
    }

    public String getMovedFrom() {
        return movedFrom;
    }

    public void setIsCaseActive(Boolean isCaseActive) {
        this.isCaseActive = isCaseActive;
    }

    public Boolean getIsCaseActive() {
        return isCaseActive;
    }

    public Integer getMovedFromCourtRoomId() {
        return movedFromCourtRoomId;
    }

    public void setMovedFromCourtRoomId(Integer movedFromCourtRoomId) {
        this.movedFromCourtRoomId = movedFromCourtRoomId;
    }
    
    public String getAddHearingUsed() {
        return addHearingUsed;
    }
    
    public void setAddHearingUsed(String addHearingUsed) {
        this.addHearingUsed = addHearingUsed;
    }

	/**
	 * @return the refCrackedEffectiveId
	 */
	public Integer getRefCrackedEffectiveId() {
		return refCrackedEffectiveId;
	}

	/**
	 * @param refCrackedEffectiveId the refCrackedEffectiveId to set
	 */
	public void setRefCrackedEffectiveId(Integer refCrackedEffectiveId) {
		this.refCrackedEffectiveId = refCrackedEffectiveId;
	}
}
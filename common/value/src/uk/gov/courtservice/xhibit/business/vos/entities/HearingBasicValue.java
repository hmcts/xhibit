package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: HearingBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the Hearing
 * enitity CMP fields.
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
public class HearingBasicValue extends CSAbstractValue {
    
	private static final long serialVersionUID = -6221149048474726599L;
	
	private Integer caseID;

    private Integer refHearingTypeID;

    private Integer courtID;

    private String mpHearingType;

    private Integer linkedHearingID;

    private Date hearingStartDate;

    private Date hearingEndDate;

    public HearingBasicValue() {
        super();
    }

    public HearingBasicValue(Integer version) {
        super(version);
    }

    public HearingBasicValue(Integer hearingID, Integer version) {
        super(hearingID, version);
    }

    /**
     * Default constructor to use in client - when no version number is
     * available.
     * 
     * @param hearingID
     * @param caseID
     * @param refHearingTypeID
     * @param courtID
     * @param mpHearingType
     */
    public HearingBasicValue(Integer caseID, Integer refHearingTypeID, Integer courtID, String mpHearingType) {
        this.caseID = caseID;
        this.refHearingTypeID = refHearingTypeID;
        this.courtID = courtID;
        this.mpHearingType = mpHearingType;
    }

    /*
     * Constructor to use when a version is available (e.g. on return from
     * creating/accessing an entity). @param hearingID @param caseID @param
     * refHearingTypeID @param courtID @param mpHearingType @param version
     * 
     * public HearingBasicValue(Integer hearingID, Integer caseID, Integer
     * refHearingTypeID, Integer courtID, String mpHearingType, Integer version) {
     * super( version ); this.hearingID = hearingID; this.caseID = caseID;
     * this.refHearingTypeID = refHearingTypeID; this.courtID = courtID;
     * this.mpHearingType = mpHearingType; }
     */

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setRefHearingTypeID(Integer refHearingTypeID) {
        this.refHearingTypeID = refHearingTypeID;
    }

    public Integer getRefHearingTypeID() {
        return refHearingTypeID;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    public Integer getCourtID() {
        return courtID;
    }

    public void setMpHearingType(String mpHearingType) {
        this.mpHearingType = mpHearingType;
    }

    public String getMpHearingType() {
        return mpHearingType;
    }

    public void setLinkedHearingID(Integer linkedHearingID) {
        this.linkedHearingID = linkedHearingID;
    }

    public Integer getLinkedHearingID() {
        return linkedHearingID;
    }

    public void setHearingStartDate(Date hearingStartDate) {
        this.hearingStartDate = hearingStartDate;
    }

    public Date getHearingStartDate() {
        return hearingStartDate;
    }

    public void setHearingEndDate(Date hearingEndDate) {
        this.hearingEndDate = hearingEndDate;
    }

    public Date getHearingEndDate() {
        return hearingEndDate;
    }
}

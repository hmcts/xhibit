package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: PartyOnCaseHeaderValue
 * </p>
 * <p>
 * Description: This is the super class of PartyOnCaseValue. Many of the
 * PartyOnCaseValues will contain the same data. This header will be used to set
 * the common header data ones for several PartyOnCaseValues.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford / Marie Holmberg
 * @version 1.0
 */
public class PartyOnCaseHeaderValue extends CSAbstractValue // implements
// Serializable
{

    // The unique scheduled hearing id
    private Integer scheduledHearingId;

    // The unique courtroom id
    private Integer courtRoomId;

    // The case number
    private Integer caseNumber;

    // The case type
    private String caseType;

    // The case sub type
    private String caseSubType;

    // The case title
    private String caseTitle;

    // The date/time the scheudled hearing is listed for
    private Date timeListed;

    // The description of the courtroom
    private String courtRoomDescription;

    // The courtroom name
    private String courtRoomName;

    // The name of the courtroom that should be displayed
    private String courtRoomDisplayName;

    // A flag to determine if the scheduled hearing is floating (unassigned
    // to
    // a ocurtroom). 0=assigned, 1=unassigned
    private String isFloating;

    // The hearing type description
    private String hearingTypeDesc;

    // The hearing type code
    private String hearingTypeCode;

    // The crest courtroom number
    private Integer crestCourtRoomNumber;

    private String courtSiteCode;

    private Integer sittingSequenceNo;

    private Integer shSequenceNo;

    private String courtSiteShortName;
    
    private static final long serialVersionUID = -2245970394441131708L;

    /**
     * The default constructor that will take all the class variables as
     * parameters.
     * 
     * @param courtRoomId
     * @param caseNumber
     * @param caseType
     * @param timeListed
     * @param courtRoomDescription
     * @param courtRoomName
     * @param courtRoomDisplayName
     * @param isFloating
     * @param hearingTypeDesc
     * @param hearingTypeCode
     * @param crestCourtRoomNumber
     */
    public PartyOnCaseHeaderValue(Integer scheduledHearingId, Integer courtRoomId, Integer caseNumber, String caseType,
            String caseSubType, String caseTitle, Date timeListed, String courtRoomDescription, String courtRoomName,
            String courtRoomDisplayName, String isFloating, String hearingTypeDesc, String hearingTypeCode,
            Integer crestCourtRoomNumber, String courtSiteCode, Integer sittingSequenceNo, Integer shSequenceNo,
            String courtSiteShortName) {
        this.scheduledHearingId = scheduledHearingId;
        this.courtRoomId = courtRoomId;
        this.caseNumber = caseNumber;
        this.caseType = caseType;
        this.caseSubType = caseSubType;
        this.caseTitle = caseTitle;
        this.timeListed = timeListed;
        this.courtRoomDescription = courtRoomDescription;
        this.courtRoomName = courtRoomName;
        this.courtRoomDisplayName = courtRoomDisplayName;
        this.isFloating = isFloating;
        this.hearingTypeDesc = hearingTypeDesc;
        this.hearingTypeCode = hearingTypeCode;
        this.crestCourtRoomNumber = crestCourtRoomNumber;
        // three new parameters for PRE00180
        this.courtSiteCode = courtSiteCode;
        this.sittingSequenceNo = sittingSequenceNo;
        this.shSequenceNo = shSequenceNo;
        this.courtSiteShortName = courtSiteShortName;
    }

    /**
     * Empty constructor
     */
    public PartyOnCaseHeaderValue() {
    }

    public Integer getScheduledHearingId() {
        return this.scheduledHearingId;
    }

    public Integer getCourtRoomId() {
        return this.courtRoomId;
    }

    public Integer getCaseNumber() {
        return this.caseNumber;
    }

    public String getCaseType() {
        return caseType;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public Date getTimeListed() {
        return this.timeListed;
    }

    public String getCourtRoomDisplayName() {
        return this.courtRoomDisplayName;
    }

    public String getCourtRoomName() {
        return this.courtRoomName;
    }

    public String getCourtRoomDescription() {
        return this.courtRoomDescription;
    }

    public String getIsFloating() {
        return this.isFloating;
    }

    public String getHearingTypeCode() {
        return hearingTypeCode;
    }

    public String getHearingTypeDesc() {
        return hearingTypeDesc;
    }

    public Integer getCrestCourtRoomNumber() {
        return crestCourtRoomNumber;
    }

    public String getCaseSubType() {
        return caseSubType;
    }

    // three new methods for PRE00180
    public String getCourtSiteCode() {
        return this.courtSiteCode;
    }

    public Integer getSittingSequenceNo() {
        return this.sittingSequenceNo;
    }

    public Integer getShSequenceNo() {
        return this.shSequenceNo;
    }

    public String getCourtSiteShortName() {
        return courtSiteShortName;
    }

    public void setScheduledHearingId(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }

    public void setCourtRoomId(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public void setCaseSubType(String caseSubType) {
        this.caseSubType = caseSubType;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public void setTimeListed(Date timeListed) {
        this.timeListed = timeListed;
    }

    public void setCourtRoomDisplayName(String name) {
        this.courtRoomDisplayName = name;
    }

    public void setCourtRoomDescription(String description) {
        this.courtRoomDescription = description;
    }

    public void setCourtRoomName(String name) {
        this.courtRoomName = name;
    }

    public void setIsFloating(String isFloating) {
        this.isFloating = isFloating;
    }

    public void setHearingTypeCode(String hearingTypeCode) {
        this.hearingTypeCode = hearingTypeCode;
    }

    public void setHearingTypeDesc(String hearingTypeDesc) {
        this.hearingTypeDesc = hearingTypeDesc;
    }

    public void setCrestCourtRoomNumber(Integer crestCourtRoomNumber) {
        this.crestCourtRoomNumber = crestCourtRoomNumber;
    }

    // three new methods for PRE00180
    public void setCourtSiteCode(String courtSiteCode) {
        this.courtSiteCode = courtSiteCode;
    }

    public void setSittingSequenceNo(Integer sittingSequenceNo) {
        this.sittingSequenceNo = sittingSequenceNo;
    }

    public void setShSequenceNo(Integer shSequenceNo) {
        this.shSequenceNo = shSequenceNo;
    }

    public void setCourtSiteShortName(String courtSiteShortName) {
        this.courtSiteShortName = courtSiteShortName;
    }
}

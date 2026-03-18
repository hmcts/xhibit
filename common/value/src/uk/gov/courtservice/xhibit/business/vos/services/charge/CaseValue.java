package uk.gov.courtservice.xhibit.business.vos.services.charge;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CaseValue
 * </p>
 * <p>
 * Description: CaseValue is intended to represent case entities as stored in
 * the case table.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Laurent Bossard
 * @version 1.0
 */

public class CaseValue extends CSAbstractValue {
    private Integer caseID;
    private Integer caseNumber;
    private String caseType;
    private String prosAgencyRef;
    private Date magConvictionDate;
    private Date committalDate;
    private String caseSubType;
    private String caseTitle;
    private String caseDescription;
    private Integer linkedCaseID;
    private Integer courtID;
    private Integer refCourtId;
    private String judgeReasonForAppeal;
    private String caseStatus;
    private Date firstHearingDate;
    private Date sentForTrialDate;
    
    private static final long serialVersionUID = 136306301587304638L;
    
    public CaseValue() {
    }

    /**
     * @param caseID
     * @param caseNumber
     * @param caseType
     */
    public CaseValue(Integer caseID, Integer caseNumber, String caseType) {
        this.caseID = caseID;
        this.caseNumber = caseNumber;
        this.caseType = caseType;
    }

    /**
     * @param caseNumber
     * @param caseType
     */
    public CaseValue(Integer caseNumber, String caseType) {
        this.caseNumber = caseNumber;
        this.caseType = caseType;
    }

    public CaseValue(Integer caseID, Integer caseNumber, String caseType, String caseTitle) {
        this.caseID = caseID;
        this.caseNumber = caseNumber;
        this.caseType = caseType;
        this.caseTitle = caseTitle;
    }

    public CaseValue(Integer caseID, Integer caseNumber, String caseType, String prosAgencyRef, Date magConvictionDate,
            String caseSubType, String caseTitle, String caseDescription, Integer linkedCaseID, Integer courtID) {
        this.caseID = caseID;
        this.caseNumber = caseNumber;
        this.caseType = caseType;
        this.prosAgencyRef = prosAgencyRef;
        this.magConvictionDate = magConvictionDate;
        this.caseSubType = caseSubType;
        this.caseTitle = caseTitle;
        this.caseDescription = caseDescription;
        this.linkedCaseID = linkedCaseID;
        this.courtID = courtID;
    }


    public CaseValue(Integer caseID, Integer caseNumber, String caseType, String prosAgencyRef, Date magConvictionDate, Date committalDate,
            String caseSubType, String caseTitle, String caseDescription, Integer linkedCaseID, Integer courtID, Integer refCourtId, String caseStatus,
            Date firstHearingDate) {
        this.caseID = caseID;
        this.caseNumber = caseNumber;
        this.caseType = caseType;
        this.prosAgencyRef = prosAgencyRef;
        this.magConvictionDate = magConvictionDate;
        this.committalDate = committalDate;
        this.caseSubType = caseSubType;
        this.caseTitle = caseTitle;
        this.caseDescription = caseDescription;
        this.linkedCaseID = linkedCaseID;
        this.courtID = courtID;
        this.refCourtId = refCourtId;
        this.caseStatus = caseStatus;
        this.firstHearingDate = firstHearingDate;
    }

    public CaseValue(Integer caseID, Integer caseNumber, String caseType, String prosAgencyRef, Date magConvictionDate, Date committalDate,
            String caseSubType, String caseTitle, String caseDescription, Integer linkedCaseID, Integer courtID, Integer refCourtId, String caseStatus,
            Date firstHearingDate, Date sentForTrialDate) {
        this.caseID = caseID;
        this.caseNumber = caseNumber;
        this.caseType = caseType;
        this.prosAgencyRef = prosAgencyRef;
        this.magConvictionDate = magConvictionDate;
        this.committalDate = committalDate;
        this.caseSubType = caseSubType;
        this.caseTitle = caseTitle;
        this.caseDescription = caseDescription;
        this.linkedCaseID = linkedCaseID;
        this.courtID = courtID;
        this.refCourtId = refCourtId;
        this.caseStatus = caseStatus;
        this.firstHearingDate = firstHearingDate;
        this.sentForTrialDate = sentForTrialDate;
    }

    /**
     * @return java.util.Date
     */
	public Date getFirstHearingDate() {
		return firstHearingDate;
	}

	/**
     * @return java.util.Date
     */
	public void setFirstHearingDate(Date firstHearingDate) {
		this.firstHearingDate = firstHearingDate;
	}

	/**
     * @return java.lang.Integer
     */
    public Integer getCaseID() {
        return caseID;
    }

    /**
     * @return java.lang.Integer
     */
    public Integer getCaseNumber() {
        return caseNumber;
    }

    /**
     * @return java.lang.String
     */
    public String getCaseType() {
        return caseType;
    }

    /**
     * @return java.lang.String
     */
    public String getProsAgencyRef() {
        return prosAgencyRef;
    }

    /**
     * @return java.lang.Date
     */
    public Date getMagConvictionDate() {
        return magConvictionDate;
    }

    /**
     * @return java.lang.Date
     */
    public Date getSentForTrialDate() {
        return sentForTrialDate;
    }

    /**
     * @return java.lang.Date
     */
    public Date getCommittalDate() {
        return committalDate;
    }

    /**
     * @return java.lang.String
     */
    public String getCaseSubType() {
        return caseSubType;
    }

    /**
     * @return java.lang.String
     */
    public String getCaseTitle() {
        return caseTitle;
    }

    /**
     * @return java.lang.String
     */
    public String getCaseDescription() {
        return caseDescription;
    }

    /**
     * @return java.lang.Integer
     */
    public Integer getLinkedCaseID() {
        return linkedCaseID;
    }

    /**
     * @return java.lang.Integer
     */
    public Integer getRefCourtId() {
        return refCourtId;
    }

    /**
     * @return java.lang.String
     */
    public String getCaseStatus() {
        return caseStatus;
    }

    /**
     * @param java.lang.String
     */
    public void setProsAgencyRef(String prosAgencyRef) {
        this.prosAgencyRef = prosAgencyRef;
    }

     /**
     * @param java.lang.Date
     */
    public void setMagConvictionDate(Date magConvictionDate) {
        this.magConvictionDate = magConvictionDate;
    }

    /**
     * @param java.lang.Date
     */
    public void setCommittalDate(Date committalDate) {
        this.committalDate = committalDate;
    }

    /**
     * @param java.lang.Date
     */
    public void setSentForTrialDate(Date sentForTrialDate) {
        this.sentForTrialDate = sentForTrialDate;
    }

    /**
     * @param java.lang.String
     */
    public void setCaseSubType(String caseSubType) {
        this.caseSubType = caseSubType;
    }

    /**
     * @param java.lang.String
     */
    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    /**
     * @param java.lang.String
     */
    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    /**
     * @param java.lang.Integer
     */
    public void setLinkedCaseID(Integer linkedCaseID) {
        this.linkedCaseID = linkedCaseID;
    }

    public String getJudgeReasonForAppeal() {
        return judgeReasonForAppeal;
    }

    public void setJudgeReasonForAppeal(String judgeReasonForAppeal) {
        this.judgeReasonForAppeal = judgeReasonForAppeal;
    }

    /**
     * @param java.lang.Integer
     */
    public void setRefCourtId(Integer refCourtId) {
        this.refCourtId = refCourtId;
    }

    /**
     * @param java.lang.String
     */
    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

}
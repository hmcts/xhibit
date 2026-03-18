package uk.gov.courtservice.xhibit.business.vos.services.caselinking;

import java.io.Serializable;
import java.util.Date;

/*
 * Author: n walters
 * Modifying to only include the fields we pull from the database.
 * We do not need to do a c.*, this not only impacts performance but brings back data
 * we never use
 */
public class CaseLinkingValue implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	private Integer caseId;
	
	private Integer caseNumber;
	
	private String caseType;
		
	private String caseTitle;
				
	private Integer refCourtId;
	
	private String receiptType;
	
	private Date appealLodgedDate;
	
	private Date committalDate;
	
	private Date sentForTrialDate;
		
	private Integer caseGroupNumber;
	
	private Date dateTransTo;	
	
	public CaseLinkingValue() {
        super();
    }
	
	public Integer getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}


	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	public Integer getRefCourtId() {
		return refCourtId;
	}

	public void setRefCourtId(Integer refCourtId) {
		this.refCourtId = refCourtId;
	}
	
	public String getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
	
	public Date getAppealLodgedDate() {
		return appealLodgedDate;
	}

	public void setAppealLodgedDate(Date appealLodgedDate) {
		this.appealLodgedDate = appealLodgedDate;
	}

	public Date getCommittalDate() {
		return committalDate;
	}

	public void setCommittalDate(Date committalDate) {
		this.committalDate = committalDate;
	}

	public Date getSentForTrialDate() {
		return sentForTrialDate;
	}

	public void setSentForTrialDate(Date sentForTrialDate) {
		this.sentForTrialDate = sentForTrialDate;
	}

	public Integer getCaseGroupNumber() {
		return caseGroupNumber;
	}

	public void setCaseGroupNumber(Integer caseGroupNumber) {
		this.caseGroupNumber = caseGroupNumber;
	}

	public Date getDateTransTo() {
		return dateTransTo;
	}

	public void setDateTransTo(Date dateTransTo) {
		this.dateTransTo = dateTransTo;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
}

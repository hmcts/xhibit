package uk.gov.courtservice.xhibit.business.vos.services.casehistory;

import java.util.Date;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseHistoryBasicValue;

/**
 * <p>
 * Title: CaseHistoryValue
 * </p>
 * <p>
 * Description: This object contains all columns in xhb_case_history
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Kudzin
 * @version 1.0
 */

public class CaseHistoryValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private CaseHistoryBasicValue caseHistoryBV;

	private Integer caseHistoryId;
	private String caseType;
	private Integer courtId;
	private Integer caseNumber;
	private String psdCTCode;
	private Date committalDate;
	private String reasonDeleted;
	private String caseTitle;
	private Date dateArchived;
	private Date sentForTrial;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private String lastUpdatedBy;
	private Integer version;

	public CaseHistoryValue() {
	}

	public CaseHistoryValue(Integer caseHistoryId, String caseType, Integer courtId, Integer caseNumber,
			String psdCTCode, Date committalDate, String reasonDeleted, String caseTitle, Date dateArchived,
			Date sentForTrial, Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy,
			Integer version) throws CreateException {
		setCaseHistoryId(caseHistoryId);
		setCaseType(caseType);
		setCourtId(courtId);
		setCaseNumber(caseNumber);
		setPsdCTCode(psdCTCode);
		setCommittalDate(committalDate);
		setReasonDeleted(reasonDeleted);
		setCaseTitle(caseTitle);
		setDateArchived(dateArchived);
		setSentForTrial(sentForTrial);
		setLastUpdateDate(lastUpdateDate);
		setCreationDate(creationDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setVersion(version);
	}

	public Integer getCaseHistoryId() {
		return caseHistoryId;
	}

	public void setCaseHistoryId(Integer caseHistoryId) {
		this.caseHistoryId = caseHistoryId;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Integer getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getPsdCTCode() {
		return psdCTCode;
	}

	public void setPsdCTCode(String psdCTCode) {
		this.psdCTCode = psdCTCode;
	}

	public Date getCommittalDate() {
		return committalDate;
	}

	public void setCommittalDate(Date committalDate) {
		this.committalDate = committalDate;
	}

	public String getReasonDeleted() {
		return reasonDeleted;
	}

	public void setReasonDeleted(String reasonDeleted) {
		this.reasonDeleted = reasonDeleted;
	}

	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	public Date getDateArchived() {
		return dateArchived;
	}

	public void setDateArchived(Date dateArchived) {
		this.dateArchived = dateArchived;
	}

	public Date getSentForTrial() {
		return sentForTrial;
	}

	public void setSentForTrial(Date sentForTrial) {
		this.sentForTrial = sentForTrial;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public CaseHistoryBasicValue getCaseHistoryBasicValue() {
		return caseHistoryBV;
	}

	public void setCaseHistoryBasicValue(CaseHistoryBasicValue caseHistoryBV) {
		this.caseHistoryBV = caseHistoryBV;
	}

}

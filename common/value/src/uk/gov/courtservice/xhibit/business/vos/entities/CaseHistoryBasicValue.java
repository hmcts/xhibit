package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class CaseHistoryBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

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

	public CaseHistoryBasicValue() {
		// Empty
	}

	public CaseHistoryBasicValue(Integer id, Integer version) {
		super(id, version);
	}

	public CaseHistoryBasicValue(Integer caseHistoryId, String caseType, Integer courtId, Integer caseNumber,
			String psdCTCode, Date committalDate, String reasonDeleted, String caseTitle, Date dateArchived,
			Date sentForTrial, Integer version) {

		this(caseHistoryId, version);
		this.caseType = caseType;
		this.courtId = courtId;
		this.caseNumber = caseNumber;
		this.psdCTCode = psdCTCode;
		this.committalDate = committalDate;
		this.reasonDeleted = reasonDeleted;
		this.caseTitle = caseTitle;
		this.dateArchived = dateArchived;
		this.sentForTrial = sentForTrial;
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

}

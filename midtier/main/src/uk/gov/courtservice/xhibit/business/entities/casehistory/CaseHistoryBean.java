package uk.gov.courtservice.xhibit.business.entities.casehistory;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class CaseHistoryBean extends CSEntityBean {

	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer caseHistoryId, String caseType, Integer courtId, Integer caseNumber,
			String psdCTCode, java.sql.Timestamp committalDate, String reasonDeleted, String caseTitle,
			java.sql.Timestamp dateArchived, java.sql.Timestamp sentForTrial, String createdBy, String lastUpdatedBy,
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
		setSentForTrialDate(sentForTrial);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setVersion(version);
		return null;
	}

	public void ejbPostCreate(Integer caseHistoryId, String caseType, Integer courtId, Integer caseNumber,
			String psdCTCode, java.sql.Timestamp committalDate, String reasonDeleted, String caseTitle,
			java.sql.Timestamp dateArchived, java.sql.Timestamp sentForTrial, String createdBy, String lastUpdatedBy,
			Integer version) throws CreateException {
	}

	public abstract Integer getCaseHistoryId();

	public abstract void setCaseHistoryId(Integer caseHistoryId);

	public abstract String getCaseType();

	public abstract void setCaseType(String caseType);

	public abstract Integer getCourtId();

	public abstract void setCourtId(Integer courtId);

	public abstract Integer getCaseNumber();

	public abstract void setCaseNumber(Integer caseNumber);

	public abstract String getPsdCTCode();

	public abstract void setPsdCTCode(String psdCTCode);

	public abstract java.sql.Timestamp getCommittalDate();

	public abstract void setCommittalDate(java.sql.Timestamp committalDate);

	public abstract String getReasonDeleted();

	public abstract void setReasonDeleted(String reasonDeleted);

	public abstract String getCaseTitle();

	public abstract void setCaseTitle(String caseTitle);

	public abstract java.sql.Timestamp getDateArchived();

	public abstract void setDateArchived(java.sql.Timestamp dateArchived);

	public abstract java.sql.Timestamp getSentForTrialDate();

	public abstract void setSentForTrialDate(java.sql.Timestamp sentForTrial);

}

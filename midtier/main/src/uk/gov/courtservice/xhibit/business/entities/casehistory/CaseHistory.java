package uk.gov.courtservice.xhibit.business.entities.casehistory;

import java.sql.Timestamp;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface CaseHistory extends CSEntityLocal {

	public Integer getCaseHistoryId();

	public void setCaseHistoryId(Integer caseHistoryId);

	public java.lang.String getCaseType();

	public void setCaseType(java.lang.String caseType);

	public Integer getCourtId();

	public void setCourtId(Integer courtId);

	public Integer getCaseNumber();

	public void setCaseNumber(Integer caseNumber);

	public java.lang.String getPsdCTCode();

	public void setPsdCTCode(java.lang.String psdCTCode);

	public Timestamp getCommittalDate();

	public void setCommittalDate(Timestamp committalDate);

	public java.lang.String getReasonDeleted();

	public void setReasonDeleted(java.lang.String reasonDeleted);

	public java.lang.String getCaseTitle();

	public void setCaseTitle(java.lang.String caseTitle);

	public Timestamp getDateArchived();

	public void setDateArchived(Timestamp dateArchived);

	public Timestamp getSentForTrialDate();

	public void setSentForTrialDate(Timestamp sentForTrial);

}

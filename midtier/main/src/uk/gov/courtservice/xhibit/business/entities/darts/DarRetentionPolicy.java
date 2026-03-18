package uk.gov.courtservice.xhibit.business.entities.darts;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface DarRetentionPolicy extends CSEntityLocal {
	public Integer getDarRetentionPolicyId();
	public void setDarRetentionPolicyId(Integer darRetentionPolicyId);
	public Integer getDisposal2Id();
	public void setDisposal2Id(Integer disposal2Id);
	public Integer getCaseId();
	public void setCaseId(Integer caseId);
	public Integer getDefendantOnCaseId();
	public void setDefendantOnCaseId(Integer defendantOnCaseId);
	public Integer getDefendantOnOffenceId();
	public void setDefendantOnOffenceId(Integer defendantOnOffenceId);	
	public Integer getRefDispRetentionPolicyId();
	public void setRefDispRetentionPolicyId(Integer refDispRetentionPolicyId);
	public Integer getRefDarRetentionPolicyId();
	public void setRefDarRetentionPolicyId(Integer refDarRetentionPolicyId);
	public Integer getDurationDays();
	public void setDurationDays(Integer durationDays);
	public Integer getDurationMonths();
	public void setDurationMonths(Integer durationMonths);
	public Integer getDurationYears();
	public void setDurationYears(Integer durationYears);
	public String getHasLife();
	public void setHasLife(String hasLife);	
	public String getIsConsecutive();
	public void setIsConsecutive(String isConsecutive);
	public String getIsUpdated();
	public void setIsUpdated(String isUpdated);
	public String getObsInd();
	public void setObsInd(String obsInd);
	public String getLastUpdatedBy();
	public void setLastUpdatedBy(String lastUpdatedBy);
	public Date getLastUpdateDate();
	public void setLastUpdateDate(Date lastUpdateDate);
	public Date getCreationDate();
	public void setCreationDate(Date creationDate);
	public String getCreatedBy();
	public void setCreatedBy(String createdBy) ;
}
package uk.gov.courtservice.xhibit.business.entities.darts;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class DarRetentionPolicyBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer darRetentionPolicyId, Integer disposal2Id,
			Integer caseId, Integer defendantOnCaseId, Integer defendantOnOffenceId,
			Integer refDispRetentionPolicyId, Integer refDarRetentionPolicyId, 
			Integer durationDays, Integer durationMonths, Integer durationYears,
			String hasLife, String isConsecutive, String isUpdated, String obsInd,
			String userDisplayName) throws CreateException {
		setDarRetentionPolicyId(darRetentionPolicyId);
		setDisposal2Id(disposal2Id);
		setCaseId(caseId);
		setDefendantOnCaseId(defendantOnCaseId);
		setDefendantOnOffenceId(defendantOnOffenceId);	
		setRefDispRetentionPolicyId(refDispRetentionPolicyId);
		setRefDarRetentionPolicyId(refDarRetentionPolicyId);
		setDurationDays(durationDays);
		setDurationMonths(durationMonths);
		setDurationYears(durationYears);
		setHasLife(hasLife);
		setObsInd(obsInd);
		setIsConsecutive(isConsecutive);
		setIsUpdated(isUpdated);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer darRetentionPolicyId, Integer disposal2Id,
			Integer caseId, Integer defendantOnCaseId, Integer defendantOnOffenceId,
			Integer refDispRetentionPolicyId, Integer refDarRetentionPolicyId, 
			Integer durationDays, Integer durationMonths, Integer durationYears,
			String hasLife, String isConsecutive, String isUpdated, String obsInd,
			String userDisplayName) throws CreateException {
	}

	public abstract Integer getDarRetentionPolicyId();
	public abstract void setDarRetentionPolicyId(Integer darRetentionPolicyId);
	public abstract Integer getDisposal2Id();
	public abstract void setDisposal2Id(Integer disposal2Id);
	public abstract Integer getCaseId();
	public abstract void setCaseId(Integer caseId);
	public abstract Integer getDefendantOnCaseId();
	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);
	public abstract Integer getDefendantOnOffenceId();
	public abstract void setDefendantOnOffenceId(Integer defendantOnOffenceId);	
	public abstract Integer getRefDispRetentionPolicyId();
	public abstract void setRefDispRetentionPolicyId(Integer refDispRetentionPolicyId);
	public abstract Integer getRefDarRetentionPolicyId();
	public abstract void setRefDarRetentionPolicyId(Integer refDarRetentionPolicyId);
	public abstract Integer getDurationDays();
	public abstract void setDurationDays(Integer durationDays);
	public abstract Integer getDurationMonths();
	public abstract void setDurationMonths(Integer durationMonths);
	public abstract Integer getDurationYears();
	public abstract void setDurationYears(Integer durationYears);
	public abstract String getHasLife();
	public abstract void setHasLife(String hasLife);	
	public abstract String getIsConsecutive();
	public abstract void setIsConsecutive(String isConsecutive);
	public abstract String getIsUpdated();
	public abstract void setIsUpdated(String isUpdated);
	public abstract String getObsInd();
	public abstract void setObsInd(String obsInd);
	public abstract String getLastUpdatedBy();
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract Date getLastUpdateDate();
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	public abstract Date getCreationDate();
	public abstract void setCreationDate(Date creationDate);
	public abstract String getCreatedBy();
	public abstract void setCreatedBy(String createdBy) ;
}
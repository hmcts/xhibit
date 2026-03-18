package uk.gov.courtservice.xhibit.business.entities.defendantonoffence;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface DefendantOnOffence extends CSEntityLocal {
   
	public String getInterimD20();
	public void setInterimD20(String interimD20);
	public Integer getDefendantOnOffenceId();
	public void setDefendantOnOffenceId(Integer defendantOnOffenceId);
	public String getAppealAgainstType();
	public void setAppealAgainstType(String appealAgainstType);
	public Integer getDefendantOnCaseId();
	public void setDefendantOnCaseId(Integer defendantOnCaseId);
	public Integer getOffenceId();
	public void setOffenceId(Integer offenceId);
	public Date getLastUpdateDate();
	public void setLastUpdateDate(Date lastUpdateDate);
	public Date getCreationDate();
	public void setCreationDate(Date creationDate);
	public String getCreatedBy();
	public void setCreatedBy(String createdBy);
	public String getLastUpdatedBy();
	public void setLastUpdatedBy(String lastUpdatedBy);
	public Integer getVersion();
	public void setVersion(Integer version);
	public String getObsInd();
	public void setObsInd(String obsInd);
	public String getIsStayed();
	public void setIsStayed(String isStayed);
	public String getCrnId();
	public void setCrnId(String crnId);
	public String getVcoFlag();
	public void setVcoFlag(String vcoFlag);
	public Date getVcoDate();
	public void setVcoDate(Date vcoDate);
	public Integer getSeqNo();
	public void setSeqNo(Integer seqNo);
	public Date getArrestDate();
	public void setArrestDate(Date arrestDate);
	public Date getChargeDate();
	public void setChargeDate(Date chargeDate);
	public String getIsCommittedOnBail();
	public void setIsCommittedOnBail(String isCommittedOnBail);
	public Integer getDarRetentionPolicyId();
	public void setDarRetentionPolicyId(Integer darRetentionPolicyId);
}
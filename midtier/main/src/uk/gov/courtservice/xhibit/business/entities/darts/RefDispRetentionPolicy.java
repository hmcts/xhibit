package uk.gov.courtservice.xhibit.business.entities.darts;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefDispRetentionPolicy extends CSEntityLocal {
	public Integer getRefDispRetentionPolicyId();
	public void setRefDispRetentionPolicyId(Integer refDispRetentionPolicyId);
	public String getDisposalCode();
	public void setDisposalCode(String disposalCode);
	public Integer getRefDarRetentionPolicyId();
	public void setRefDarRetentionPolicyId(Integer refDarRetentionPolicyId);
	public String getAllowsConsecutive();
	public void setAllowsConsecutive(String allowsConsecutive);
	public String getHasLife();
	public void setHasLife(String hasLife);
	public String getHasDuration();
	public void setHasDuration(String hasDuration);
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
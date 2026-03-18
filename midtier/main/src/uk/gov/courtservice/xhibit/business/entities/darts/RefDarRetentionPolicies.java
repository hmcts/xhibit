package uk.gov.courtservice.xhibit.business.entities.darts;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefDarRetentionPolicies extends CSEntityLocal {
	public Integer getRefDarRetentionPolicyId();
	public void setRefDarRetentionPolicyId(Integer refDarRetentionPolicyId);
	public Integer getPolicyNo();
	public void setPolicyNo(Integer policyNo);
	public String getPolicyDescription();
	public void setPolicyDescription(String policyDescription);
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
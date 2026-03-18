package uk.gov.courtservice.xhibit.business.entities.darts;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefDarRetentionPoliciesBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer refDarRetentionPolicyId, Integer policyNo, String policyDescription,
			String userDisplayName) throws CreateException {
		setRefDarRetentionPolicyId(refDarRetentionPolicyId);
		setPolicyNo(policyNo);
		setPolicyDescription(policyDescription);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer refDarRetentionPolicyId, Integer policyNo, String policyDescription,
			String userDisplayName) throws CreateException {
	}

	public abstract Integer getRefDarRetentionPolicyId();
	public abstract void setRefDarRetentionPolicyId(Integer refDarRetentionPolicyId);
	public abstract Integer getPolicyNo();
	public abstract void setPolicyNo(Integer policyNo);
	public abstract String getPolicyDescription();
	public abstract void setPolicyDescription(String policyDescription);
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
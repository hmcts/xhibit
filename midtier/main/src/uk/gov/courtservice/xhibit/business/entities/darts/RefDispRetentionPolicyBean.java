package uk.gov.courtservice.xhibit.business.entities.darts;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefDispRetentionPolicyBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer refDispRetentionPolicyId, String disposalCode,
			Integer refDarRetentionPolicyId, String allowsConsecutive,
			String hasLife, String hasDuration,
			String userDisplayName) throws CreateException {
		setRefDispRetentionPolicyId(refDispRetentionPolicyId);
		setDisposalCode(disposalCode);
		setRefDarRetentionPolicyId(refDarRetentionPolicyId);
		setAllowsConsecutive(allowsConsecutive);
		setHasLife(hasLife);
		setHasDuration(hasDuration);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer refDispRetentionPolicyId, String disposalCode,
			Integer refDarRetentionPolicyId, String allowsConsecutive,
			String hasLife, String hasDuration,
			String userDisplayName) throws CreateException {
	}

	public abstract Integer getRefDispRetentionPolicyId();
	public abstract void setRefDispRetentionPolicyId(Integer refDispRetentionPolicyId);
	public abstract String getDisposalCode();
	public abstract void setDisposalCode(String disposalCode);
	public abstract Integer getRefDarRetentionPolicyId();
	public abstract void setRefDarRetentionPolicyId(Integer refDarRetentionPolicyId);
	public abstract String getAllowsConsecutive();
	public abstract void setAllowsConsecutive(String allowsConsecutive);
	public abstract String getHasLife();
	public abstract void setHasLife(String hasLife);
	public abstract String getHasDuration();
	public abstract void setHasDuration(String hasDuration);
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
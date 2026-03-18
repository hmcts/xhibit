package uk.gov.courtservice.xhibit.business.entities.defendantonoffence;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

public abstract class DefendantOnOffenceBean extends CSEntityBean implements EntityBean {
    
	private static final long serialVersionUID = 1L;
	
    public Integer ejbCreate(Integer defendantOnOffenceId, String userDisplayName)
            throws CreateException {
    	setDefendantOnOffenceId(defendantOnOffenceId);
        setCreatedBy(userDisplayName);
    	return null;
    }
    
    public void ejbPostCreate(Integer defendantOnOffenceId, String userDisplayName)
            throws CreateException {
    	 // Empty
    }

	public abstract Integer getDefendantOnOffenceId();
	public abstract void setDefendantOnOffenceId(Integer defendantOnOffenceId);
	public abstract String getAppealAgainstType();
	public abstract void setAppealAgainstType(String appealAgainstType);
	public abstract Integer getDefendantOnCaseId();
	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);
	public abstract Integer getOffenceId();
	public abstract void setOffenceId(Integer offenceId);
	public abstract Date getLastUpdateDate();
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	public abstract Date getCreationDate();
	public abstract void setCreationDate(Date creationDate);
	public abstract String getCreatedBy();
	public abstract void setCreatedBy(String createdBy);
	public abstract String getLastUpdatedBy();
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract Integer getVersion();
	public abstract void setVersion(Integer version);
	public abstract String getObsInd();
	public abstract void setObsInd(String obsInd);
	public abstract String getIsStayed();
	public abstract void setIsStayed(String isStayed);
	public abstract String getCrnId();
	public abstract void setCrnId(String crnId);
	public abstract String getVcoFlag();
	public abstract void setVcoFlag(String vcoFlag);
	public abstract Date getVcoDate();
	public abstract void setVcoDate(Date vcoDate);
	public abstract Integer getSeqNo();
	public abstract void setSeqNo(Integer seqNo);
	public abstract Date getArrestDate();
	public abstract void setArrestDate(Date arrestDate);
	public abstract Date getChargeDate();
	public abstract void setChargeDate(Date chargeDate);
	public abstract String getIsCommittedOnBail();
	public abstract void setIsCommittedOnBail(String isCommittedOnBail);
	public abstract String getInterimD20();
	public abstract void setInterimD20(String interimD20);
	public abstract Integer getDarRetentionPolicyId();
	public abstract void setDarRetentionPolicyId(Integer darRetentionPolicyId);
}
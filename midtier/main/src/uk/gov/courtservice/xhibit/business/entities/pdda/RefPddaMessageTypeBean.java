package uk.gov.courtservice.xhibit.business.entities.pdda;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefPddaMessageTypeBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer refPddaMessageTypeId, String pddaMessageType,
			String pddaMessageTypeDescription, String obsInd,
			String userDisplayName) throws CreateException {
		setRefPddaMessageTypeId(refPddaMessageTypeId);
		setPddaMessageType(pddaMessageType);
		setPddaMessageTypeDescription(pddaMessageTypeDescription);
		setObsInd(obsInd);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer refPddaMessageTypeId, String pddaMessageType,
			String pddaMessageTypeDescription, String obsInd,
			String userDisplayName) throws CreateException {
	}

	public abstract Integer getRefPddaMessageTypeId();
	public abstract void setRefPddaMessageTypeId(Integer refPddaMessageTypeId);
	public abstract String getPddaMessageType();
	public abstract void setPddaMessageType(String pddaMessageType);
	public abstract String getPddaMessageTypeDescription();
	public abstract void setPddaMessageTypeDescription(String pddaMessageTypeDescription);
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
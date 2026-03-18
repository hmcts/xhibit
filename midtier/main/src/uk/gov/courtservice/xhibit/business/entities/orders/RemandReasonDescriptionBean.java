package uk.gov.courtservice.xhibit.business.entities.orders;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RemandReasonDescriptionBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer remandReasonDescriptionId, String reasonCategory,
			String reasonDescription, 
			String obsInd, String userDisplayName) throws CreateException {
		setRemandReasonDescriptionId(remandReasonDescriptionId);
		setReasonCategory(reasonCategory);
		setReasonDescription(reasonDescription);
		setObsInd(obsInd);  
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer remandReasonDescriptionId, String reasonCategory,
			String reasonDescription, 
			String obsInd, String userDisplayName) throws CreateException {
	}

	// ------------------------------CMP
	// Fields------------------------------------ 	
	public abstract Integer getRemandReasonDescriptionId();
	public abstract String getReasonCategory();
	public abstract String getReasonDescription();
	public abstract String getObsInd();
	public abstract String getCreatedBy();
	public abstract String getLastUpdatedBy();
	public abstract Date getCreationDate();
	public abstract Date getLastUpdateDate();

	public abstract void setRemandReasonDescriptionId(Integer remandReasonDescriptionId);
	public abstract void setReasonCategory(String reasonCategory);	
	public abstract void setReasonDescription(String reasonDescription);
	public abstract void setObsInd(String obsInd);
	public abstract void setCreatedBy(String createdBy);
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract void setCreationDate(Date creationDate);
	public abstract void setLastUpdateDate(Date lastUpdateDate);
}
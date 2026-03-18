package uk.gov.courtservice.xhibit.business.entities.orders;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RemandReasonsBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer remandReasonsId, Integer defendantOnCaseId, Integer orderId, 
			Integer remandReasonDescriptionId, String additionalInformation,
			String obsInd, String userDisplayName) throws CreateException {
		setRemandReasonsId(remandReasonsId);
		setDefendantOnCaseId(defendantOnCaseId);
		setOrderId(orderId);
		setRemandReasonDescriptionId(remandReasonDescriptionId);
		setAdditionalInformation(additionalInformation);
		setObsInd(obsInd);  
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer remandReasonsId, Integer defendantOnCaseId, Integer orderId, 
			Integer remandReasonDescriptionId, String additionalInformation,
			String obsInd, String userDisplayName) throws CreateException {
	}

	// ------------------------------CMP
	// Fields------------------------------------    
	public abstract Integer getRemandReasonsId();
	public abstract Integer getDefendantOnCaseId();
	public abstract Integer getOrderId();
	public abstract Integer getRemandReasonDescriptionId();
	public abstract String getAdditionalInformation();
	public abstract String getObsInd();
	public abstract String getCreatedBy();
	public abstract String getLastUpdatedBy();
	public abstract Date getCreationDate();
	public abstract Date getLastUpdateDate();

	public abstract void setRemandReasonsId(Integer remandReasonsId);
	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);	
	public abstract void setOrderId(Integer orderId);
	public abstract void setRemandReasonDescriptionId(Integer remandReasonDescriptionId);
	public abstract void setAdditionalInformation(String additionalInformation);
	public abstract void setObsInd(String obsInd);
	public abstract void setCreatedBy(String createdBy);
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract void setCreationDate(Date creationDate);
	public abstract void setLastUpdateDate(Date lastUpdateDate);
}
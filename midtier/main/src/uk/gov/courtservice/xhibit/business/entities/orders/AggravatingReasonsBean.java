package uk.gov.courtservice.xhibit.business.entities.orders;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class AggravatingReasonsBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer aggravatingReasonsId, Integer defendantOnCaseId, Integer refAggravatingReasonsId, 
			String obsInd, String userDisplayName) throws CreateException {
		setAggravatingReasonsId(aggravatingReasonsId);
		setDefendantOnCaseId(defendantOnCaseId);
		setRefAggravatingReasonsId(refAggravatingReasonsId);
		setObsInd(obsInd);  
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer aggravatingReasonsId, Integer defendantOnCaseId, Integer refAggravatingReasonsId, 
			String obsInd, String userDisplayName) throws CreateException {
	}

	// ------------------------------CMP
	// Fields------------------------------------    
	public abstract Integer getAggravatingReasonsId();
	public abstract Integer getDefendantOnCaseId();
	public abstract Integer getRefAggravatingReasonsId();
	public abstract String getObsInd();
	public abstract String getCreatedBy();
	public abstract String getLastUpdatedBy();
	public abstract Date getCreationDate();
	public abstract Date getLastUpdateDate();

	public abstract void setAggravatingReasonsId(Integer aggravatingReasonsId);
	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);
	public abstract void setRefAggravatingReasonsId(Integer refAggravatingReasonsId);
	public abstract void setObsInd(String obsInd);
	public abstract void setCreatedBy(String createdBy);
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract void setCreationDate(Date creationDate);
	public abstract void setLastUpdateDate(Date lastUpdateDate);
}
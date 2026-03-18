package uk.gov.courtservice.xhibit.business.entities.orders;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface AggravatingReasons extends CSEntityLocal {

	public Integer getAggravatingReasonsId();
	public Integer getDefendantOnCaseId();
	public Integer getRefAggravatingReasonsId();
	public String getObsInd();
	public String getCreatedBy();
	public String getLastUpdatedBy();
	public Date getCreationDate();
	public Date getLastUpdateDate();
	
	public void setAggravatingReasonsId(Integer aggravatingReasonsId);
	public void setDefendantOnCaseId(Integer defendantOnCaseId);
	public void setRefAggravatingReasonsId(Integer refAggravatingReasonsId);
	public void setObsInd(String obsInd);
	public void setCreatedBy(String createdBy);
	public void setLastUpdatedBy(String lastUpdatedBy);
	public void setCreationDate(Date creationDate);
	public void setLastUpdateDate(Date lastUpdateDate);	
}
package uk.gov.courtservice.xhibit.business.entities.orders;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RemandReasonDescription extends CSEntityLocal {

	public Integer getRemandReasonDescriptionId();
	public String getReasonCategory();
	public String getReasonDescription();
	public String getObsInd();
	public String getCreatedBy();
	public String getLastUpdatedBy();
	public Date getCreationDate();
	public Date getLastUpdateDate();
	
	public void setRemandReasonDescriptionId(Integer remandReasonDescriptionId);
	public void setReasonCategory(String reasonCategory);
	public void setReasonDescription(String reasonDescription);
	public void setObsInd(String obsInd);
	public void setCreatedBy(String createdBy);
	public void setLastUpdatedBy(String lastUpdatedBy);
	public void setCreationDate(Date creationDate);
	public void setLastUpdateDate(Date lastUpdateDate);	
}
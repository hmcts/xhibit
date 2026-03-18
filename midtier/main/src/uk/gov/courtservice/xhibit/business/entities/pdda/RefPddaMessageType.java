package uk.gov.courtservice.xhibit.business.entities.pdda;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefPddaMessageType extends CSEntityLocal {
	public Integer getRefPddaMessageTypeId();
	public void setRefPddaMessageTypeId(Integer refPddaMessageTypeId);
	public String getPddaMessageType();
	public void setPddaMessageType(String pddaMessageType);
	public String getPddaMessageTypeDescription();
	public void setPddaMessageTypeDescription(String pddaMessageTypeDescription);
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
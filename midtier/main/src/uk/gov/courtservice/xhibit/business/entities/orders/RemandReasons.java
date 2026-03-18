package uk.gov.courtservice.xhibit.business.entities.orders;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RemandReasons extends CSEntityLocal {

	public Integer getRemandReasonsId();
	public Integer getDefendantOnCaseId();
	public Integer getOrderId();
	public Integer getRemandReasonDescriptionId();
	public String getAdditionalInformation();
	public String getObsInd();
	public String getCreatedBy();
	public String getLastUpdatedBy();
	public Date getCreationDate();
	public Date getLastUpdateDate();
	
	public void setRemandReasonsId(Integer remandReasonsId);
	public void setDefendantOnCaseId(Integer defendantOnCaseId);
	public void setOrderId(Integer orderId);
	public void setRemandReasonDescriptionId(Integer remandReasonDescriptionId);
	public void setAdditionalInformation(String additionalInformation);
	public void setObsInd(String obsInd);
	public void setCreatedBy(String createdBy);
	public void setLastUpdatedBy(String lastUpdatedBy);
	public void setCreationDate(Date creationDate);
	public void setLastUpdateDate(Date lastUpdateDate);	
}
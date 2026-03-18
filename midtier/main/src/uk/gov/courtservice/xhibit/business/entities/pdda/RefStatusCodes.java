package uk.gov.courtservice.xhibit.business.entities.pdda;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefStatusCodes extends CSEntityLocal {
	public Integer getRefStatusCodeId();
	public void setRefStatusCodeId(Integer refStatusCodeId);
	public String getStatusCodeType();
	public void setStatusCodeType(String statusCodeType);
	public String getStatusCode();
	public void setStatusCode(String statusCode);
	public String getStatusCodeDescription();
	public void setStatusCodeDescription(String statusCodeDescription);
	public String getObsInd();
	public void setObsInd(String obsInd);
	public String getLastUpdatedBy();
	public void setLastUpdatedBy(String lastUpdatedBy);
	public Date getLastUpdateDate();
	public void setLastUpdateDate(Date lastUpdateDate);
	public Date getCreationDate();
	public void setCreationDate(Date creationDate);
	public String getCreatedBy();
	public void setCreatedBy(String createdBy);
}
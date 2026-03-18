package uk.gov.courtservice.xhibit.business.entities.pdda;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefStatusCodesBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer refStatusCodeId, String statusCodeType,
			String statusCode, String statusCodeDescription, String obsInd,
			String userDisplayName) throws CreateException {
		setRefStatusCodeId(refStatusCodeId);
		setStatusCodeType(statusCodeType);
		setStatusCode(statusCode);
		setStatusCodeDescription(statusCodeDescription);
		setObsInd(obsInd);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer refStatusCodeId, String statusCodeType,
			String statusCode, String statusCodeDescription, String obsInd,
			String userDisplayName) throws CreateException {
	}

	public abstract Integer getRefStatusCodeId();
	public abstract void setRefStatusCodeId(Integer refStatusCodeId);
	public abstract String getStatusCodeType();
	public abstract void setStatusCodeType(String statusCodeType);
	public abstract String getStatusCode();
	public abstract void setStatusCode(String statusCode);
	public abstract String getStatusCodeDescription();
	public abstract void setStatusCodeDescription(String statusCodeDescription);
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
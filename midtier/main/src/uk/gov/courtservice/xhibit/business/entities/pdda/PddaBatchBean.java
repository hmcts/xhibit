package uk.gov.courtservice.xhibit.business.entities.pdda;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class PddaBatchBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer pddaBatchId, Integer noOfRecordsInBatch, 
			Date batchOpenedDatetime, Date batchClosedDatetime,
			Integer batchStatusId, String batchMessage, 
			Integer batchNoResends, Date batchSentTime,
			String obsInd, String userDisplayName) throws CreateException {
		setPddaBatchId(pddaBatchId);
		setNoOfRecordsInBatch(noOfRecordsInBatch);
		setBatchOpenedDatetime(batchOpenedDatetime);
		setBatchClosedDatetime(batchClosedDatetime);
		setBatchStatusId(batchStatusId);
		setBatchMessage(batchMessage);
		setBatchNoResends(batchNoResends);
		setBatchSentTime(batchSentTime);
		setObsInd(obsInd);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer pddaBatchId, Integer noOfRecordsInBatch, 
			Date batchOpenedDatetime, Date batchClosedDatetime,
			Integer batchStatusId, String batchMessage, 
			Integer batchNoResends, Date batchSentTime,
			String obsInd, String userDisplayName) throws CreateException {
	}

	public abstract Integer getPddaBatchId();
	public abstract void setPddaBatchId(Integer pddaBatchId);
	public abstract Integer getNoOfRecordsInBatch();
	public abstract void setNoOfRecordsInBatch(Integer noOfRecordsInBatch);
	public abstract Date getBatchOpenedDatetime();
	public abstract void setBatchOpenedDatetime(Date batchOpenedDatetime);
	public abstract Date getBatchClosedDatetime();
	public abstract void setBatchClosedDatetime(Date batchClosedDatetime);
	public abstract Integer getBatchStatusId();
	public abstract void setBatchStatusId(Integer batchStatusId);
	public abstract String getBatchMessage();
	public abstract void setBatchMessage(String batchMessage);
	public abstract Integer getBatchNoResends();
	public abstract void setBatchNoResends(Integer batchNoResends);
	public abstract Date getBatchSentTime();
	public abstract void setBatchSentTime(Date batchSentTime);
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
package uk.gov.courtservice.xhibit.business.entities.pdda;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface PddaBatch extends CSEntityLocal {
    
		public java.lang.Integer getPddaBatchId();
		public void setPddaBatchId(java.lang.Integer pddaBatchId);
		public java.lang.Integer getNoOfRecordsInBatch();
		public void setNoOfRecordsInBatch(java.lang.Integer noOfRecordsInBatch);
		public java.util.Date getBatchOpenedDatetime();
		public void setBatchOpenedDatetime(java.util.Date batchOpenedDatetime);
		public java.util.Date getBatchClosedDatetime();
		public void setBatchClosedDatetime(java.util.Date batchClosedDatetime);
		public java.lang.Integer getBatchStatusId();
		public void setBatchStatusId(java.lang.Integer batchStatusId);
		public java.lang.String getBatchMessage();	
		public void setBatchMessage(java.lang.String batchMessage);
		public java.lang.Integer getBatchNoResends();
		public void setBatchNoResends(java.lang.Integer batchNoResends);
		public java.util.Date getBatchSentTime();
		public void setBatchSentTime(java.util.Date batchSentTime);
		public java.lang.String getObsInd();
		public void setObsInd(java.lang.String obsInd);
		public java.lang.String getLastUpdatedBy();
		public void setLastUpdatedBy(java.lang.String lastUpdatedBy);
		public java.util.Date getLastUpdateDate();
		public void setLastUpdateDate(java.util.Date lastUpdateDate);
		public java.util.Date getCreationDate();
		public void setCreationDate(java.util.Date creationDate);
		public java.lang.String getCreatedBy();
		public void setCreatedBy(java.lang.String createdBy);
}
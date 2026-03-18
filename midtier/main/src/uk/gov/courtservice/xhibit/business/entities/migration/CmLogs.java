package uk.gov.courtservice.xhibit.business.entities.migration;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface CmLogs extends CSEntityLocal {
    
		public java.lang.Integer getCmLogsId();
		public void setCmLogsId(java.lang.Integer cmLogsId);
		public java.lang.String getOriginalUploadedFilename();
		public void setOriginalUploadedFilename(java.lang.String originalUploadedFilename);
		public java.lang.Long getOriginalUploadedFileClobId();
		public void setOriginalUploadedFileClobId(java.lang.Long originalUploadedFileClobId);
		public java.lang.String getProcessingStatus();
		public void setProcessingStatus(java.lang.String processingStatus);
		public java.util.Date getProcessingDatetime();
		public void setProcessingDatetime(java.util.Date processingDatetime);
		public java.lang.Long getLogsClobId();
		public void setLogsClobId(java.lang.Long logsClobId);
		public java.lang.String getFileChecksum();
		public void setFileChecksum(java.lang.String fileChecksum);
		public java.lang.Integer getUploadSize();
		public void setUploadSize(java.lang.Integer uploadSize);
		public java.lang.Integer getNoOfCasesInFile();
		public void setNoOfCasesInFile(java.lang.Integer noOfCasesInFile);
		public java.util.Date getLastUpdateDate();
		public void setLastUpdateDate(java.util.Date lastUpdateDate);
		public java.util.Date getCreationDate();
		public void setCreationDate(java.util.Date creationDate);
		public java.lang.String getCreatedBy();
		public void setCreatedBy(java.lang.String createdBy);
		public java.lang.String getLastUpdatedBy();
		public void setLastUpdatedBy(java.lang.String lastUpdatedBy);
}
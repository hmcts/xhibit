package uk.gov.courtservice.xhibit.business.entities.migration;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class CmLogsBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer cmLogsId, String originalUploadedFilename, Long originalUploadedFileClobId,
			String processingStatus, Date processingDatetime, Long logsClobId, String fileChecksum, Integer uploadSize,
			Integer noOfCasesInFile, String userDisplayName) throws CreateException {
		setCmLogsId(cmLogsId);
		setOriginalUploadedFilename(originalUploadedFilename);
		setOriginalUploadedFileClobId(originalUploadedFileClobId);
		setProcessingStatus(processingStatus);
		setProcessingDatetime(processingDatetime);
		setLogsClobId(logsClobId);
		setFileChecksum(fileChecksum);
		setUploadSize(uploadSize);
		setNoOfCasesInFile(noOfCasesInFile);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer cmLogsId, String originalUploadedFilename, Long originalUploadedFileClobId,
			String processingStatus, Date processingDatetime, Long logsClobId, String fileChecksum, Integer uploadSize,
			Integer noOfCasesInFile, String userDisplayName) throws CreateException {
	}

	public abstract Integer getCmLogsId();
	public abstract void setCmLogsId(Integer cmLogsId);
	public abstract String getOriginalUploadedFilename();
	public abstract  void setOriginalUploadedFilename(String originalUploadedFilename);
	public abstract Long getOriginalUploadedFileClobId();
	public abstract void setOriginalUploadedFileClobId(Long originalUploadedFileClobId);
	public abstract String getProcessingStatus();
	public abstract void setProcessingStatus(String processingStatus);
	public abstract Date getProcessingDatetime();
	public abstract void setProcessingDatetime(Date processingDatetime);
	public abstract Long getLogsClobId();
	public abstract void setLogsClobId(Long logsClobId);
	public abstract String getFileChecksum();
	public abstract void setFileChecksum(String fileChecksum);
	public abstract Integer getUploadSize();
	public abstract void setUploadSize(Integer uploadSize);
	public abstract Integer getNoOfCasesInFile();
	public abstract void setNoOfCasesInFile(Integer noOfCasesInFile);
	public abstract Date getLastUpdateDate();
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	public abstract Date getCreationDate();
	public abstract void setCreationDate(Date creationDate);
	public abstract String getCreatedBy();
	public abstract void setCreatedBy(String createdBy);
	public abstract String getLastUpdatedBy();
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
}
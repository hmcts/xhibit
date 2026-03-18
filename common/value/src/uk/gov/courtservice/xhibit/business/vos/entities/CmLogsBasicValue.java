package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Basic Value object to retrieve information from the 
 * XHB_CM_LOGS table.
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import java.util.Date;

public class CmLogsBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer cmLogsId;
	private String originalUploadedFilename;
	private Long originalUploadedFileClobId;
	private String processingStatus;
	private Date processingDatetime;
	private Long logsClobId;
	private String fileChecksum;
	private Integer uploadSize;
	private Integer noOfCasesInFile;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private Integer version;

	public CmLogsBasicValue() {
		// Empty
	}

	public CmLogsBasicValue(Integer id, Integer version) {
		super(id, version);
		this.cmLogsId = id;
	}
	
	public CmLogsBasicValue(Integer cmLogsId, String originalUploadedFilename, Long originalUploadedFileClobId, String processingStatus, Date processingDatetime,
			Long logsClobId, String fileChecksum, Integer uploadSize, Integer noOfCasesInFile) {
		this.cmLogsId = cmLogsId;
		this.originalUploadedFilename = originalUploadedFilename;
		this.originalUploadedFileClobId=originalUploadedFileClobId;
		this.processingStatus = processingStatus;
		this.processingDatetime=processingDatetime;
		this.logsClobId=logsClobId;
		this.fileChecksum=fileChecksum;
		this.uploadSize=uploadSize;
		this.noOfCasesInFile=noOfCasesInFile;
	}

	public Integer getCmLogsId() {
		return cmLogsId;
	}

	public void setCmLogsId(Integer cmLogsId) {
		this.cmLogsId = cmLogsId;
	}
	
	public String getOriginalUploadedFilename() {
		return originalUploadedFilename;
	}

	public void setOriginalUploadedFilename(String originalUploadedFilename) {
		this.originalUploadedFilename = originalUploadedFilename;
	}

	public Long getOriginalUploadedFileClobId() {
		return originalUploadedFileClobId;
	}

	public void setOriginalUploadedFileClobId(Long originalUploadedFileClobId) {
		this.originalUploadedFileClobId = originalUploadedFileClobId;
	}
	
	public String getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}

	public Date getProcessingDatetime() {
		return processingDatetime;
	}

	public void setProcessingDatetime(Date processingDatetime) {
		this.processingDatetime = processingDatetime;
	}
	
	public Long getLogsClobId() {
		return logsClobId;
	}

	public void setLogsClobId(Long logsClobId) {
		this.logsClobId = logsClobId;
	}

	public String getFileChecksum() {
		return fileChecksum;
	}

	public void setFileChecksum(String fileChecksum) {
		this.fileChecksum = fileChecksum;
	}

	public Integer getUploadSize() {
		return uploadSize;
	}

	public void setUploadSize(Integer uploadSize) {
		this.uploadSize = uploadSize;
	}
	
	public Integer getNoOfCasesInFile() {
		return noOfCasesInFile;
	}

	public void setNoOfCasesInFile(Integer noOfCasesInFile) {
		this.noOfCasesInFile = noOfCasesInFile;
	}
	
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
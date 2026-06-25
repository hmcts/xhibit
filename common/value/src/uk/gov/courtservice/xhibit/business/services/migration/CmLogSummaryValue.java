package uk.gov.courtservice.xhibit.business.services.migration;

import java.io.Serializable;
import java.util.Date;

public class CmLogSummaryValue implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int cmLogsId;
	private String fileName;
	private String status;
	private Date uploadDate;
	private Long originalUploadedFileClobId;
	private Long logsClobId;
	
	public CmLogSummaryValue(int cmLogsId, String fileName, String status, Date uploadDate, Long originalUploadedFileClobId, Long logsClobId) {
		super();
		this.cmLogsId = cmLogsId;
		this.fileName = fileName;
		this.status = status;
		this.uploadDate = uploadDate;
		this.originalUploadedFileClobId = originalUploadedFileClobId;
		this.logsClobId = logsClobId;
	}
	
	public int getCmLogsId() {
		return cmLogsId;
	}
	
	public void setCmLogsId(int cmLogsId) {
		this.cmLogsId = cmLogsId;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getUploadDate() {
		return uploadDate;
	}
	
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Long getOriginalUploadedFileClobId() {
		return originalUploadedFileClobId;
	}

	public void setOriginalUploadedFileClobId(Long fileContentsClobId) {
		this.originalUploadedFileClobId = fileContentsClobId;
	}

	public Long getLogsClobId() {
		return logsClobId;
	}

	public void setLogsClobId(Long fileLogsClobId) {
		this.logsClobId = fileLogsClobId;
	}
}
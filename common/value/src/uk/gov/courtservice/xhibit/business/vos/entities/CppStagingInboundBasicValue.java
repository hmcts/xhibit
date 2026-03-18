package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Basic Value object to retrieve information from the 
 * XHB_CPP_STAGING_INBOUND table.
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import java.util.Date;

public class CppStagingInboundBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer cppStagingInboundId;
	private String documentName;
	private Integer courtCode;
	private String documentType;
	private Date timeLoaded;
	private Long clobId;
	private String validationStatus;
	private String acknowledgmentStatus;
	private String processingStatus;
	private String validationErrorMessage;
	private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private String errorMessage;

	public CppStagingInboundBasicValue() {
		// Empty
	}

	public CppStagingInboundBasicValue(Integer id, Integer version) {
		super(id, version);
		this.cppStagingInboundId = id;
	}
	
	public CppStagingInboundBasicValue(Integer cppStagingInboundId, String documentName, Integer courtCode, String documentType, Date timeLoaded,
			Long clobId, String validationStatus, String acknowledgmentStatus, String processingStatus, String validationErrorMessage) {
		this.cppStagingInboundId = cppStagingInboundId;
		this.documentName = documentName;
		this.courtCode=courtCode;
		this.documentType = documentType;
		this.timeLoaded=timeLoaded;
		this.clobId=clobId;
		this.validationStatus=validationStatus;
		this.acknowledgmentStatus=acknowledgmentStatus;
		this.processingStatus=processingStatus;
		this.validationErrorMessage=validationErrorMessage;
	}

	public Integer getCppStagingInboundId() {
		return cppStagingInboundId;
	}

	public void setCppStagingInboundId(Integer cppStagingInboundId) {
		this.cppStagingInboundId = cppStagingInboundId;
	}
	
	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public Integer getCourtCode() {
		return courtCode;
	}

	public void setCourtCode(Integer courtCode) {
		this.courtCode = courtCode;
	}
	
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Date getTimeLoaded() {
		return timeLoaded;
	}

	public void setTimeLoaded(Date timeLoaded) {
		this.timeLoaded = timeLoaded;
	}
	
	public Long getClobId() {
		return clobId;
	}

	public void setClobId(Long clobId) {
		this.clobId = clobId;
	}

	public String getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(String validationStatus) {
		this.validationStatus = validationStatus;
	}

	public String getAcknowledgmentStatus() {
		return acknowledgmentStatus;
	}

	public void setAcknowledgmentStatus(String acknowledgmentStatus) {
		this.acknowledgmentStatus = acknowledgmentStatus;
	}
	
	public String getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}
	
	public String getValidationErrorMessage() {
		return validationErrorMessage;
	}

	public void setValidationErrorMessage(String validationErrorMessage) {
		this.validationErrorMessage = validationErrorMessage;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Basic Value object to retrieve information from the 
 * XHB_CPP_FORMATTING table.
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import java.util.Date;

public class CppFormattingBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer cppFormattingId;
	private Integer stagingTableId;
	private Date dateIn;
	private String formatStatus;
	private String documentType;
	private Integer courtId;
	private Long xmlDocumentClobId;
	private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private String errorMessage;

	public CppFormattingBasicValue() {
		// Empty
	}

	public CppFormattingBasicValue(Integer id, Integer version) {
		super(id, version);
		this.cppFormattingId = id;
	}
	
	public CppFormattingBasicValue(Integer cppFormattingId, Integer stagingTableId, Date dateIn,
			String formatStatus, String documentType, Integer courtId, Long xmlDocumentClobId, String errorMessage) {
		this.cppFormattingId = cppFormattingId;
		this.stagingTableId=stagingTableId;
		this.dateIn=dateIn;
		this.formatStatus=formatStatus;
		this.documentType=documentType;
		this.courtId=courtId;
		this.xmlDocumentClobId=xmlDocumentClobId;
		this.setErrorMessage(errorMessage);
	}

	public Integer getCppFormattingId() {
		return cppFormattingId;
	}

	public void setCppFormattingId(Integer cppFormattingId) {
		this.cppFormattingId = cppFormattingId;
	}

	public Integer getStagingTableId() {
		return stagingTableId;
	}

	public void setStagingTableId(Integer stagingTableId) {
		this.stagingTableId = stagingTableId;
	}

	public Date getDateIn() {
		return dateIn;
	}

	public void setDateIn(Date dateIn) {
		this.dateIn = dateIn;
	}

	public String getFormatStatus() {
		return formatStatus;
	}

	public void setFormatStatus(String formatStatus) {
		this.formatStatus = formatStatus;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Long getXmlDocumentClobId() {
		return xmlDocumentClobId;
	}

	public void setXmlDocumentClobId(Long xmlDocumentClobId) {
		this.xmlDocumentClobId = xmlDocumentClobId;
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
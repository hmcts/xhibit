package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Basic Value object to retrieve information from the 
 * XHB_CPP_FORMATTING_MERGE table.
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import java.util.Date;

public class CppFormattingMergeBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	private Integer cppFormattingMergeId;
	private Integer cppFormattingId;
	private Integer formattingId;
	private Integer courtId;
	private String language;
	private Long xhibitClobId;
	private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;

	public CppFormattingMergeBasicValue() {
		// Empty
	}

	public CppFormattingMergeBasicValue(Integer id, Integer version) {
		super(id, version);
		this.cppFormattingId = id;
	}
	
	public CppFormattingMergeBasicValue(Integer cppFormattingMergeId, Integer cppFormattingId, Integer formattingId, Integer courtId, String language, Long xhibitClobId) {
		this.cppFormattingMergeId=cppFormattingMergeId;
		this.cppFormattingId=cppFormattingId;
		this.formattingId=formattingId;
		this.courtId=courtId;
		this.language=language;
		this.xhibitClobId=xhibitClobId;
	}

	public Integer getCppFormattingMergeId() {
		return cppFormattingMergeId;
	}

	public void setCppFormattingMergeId(Integer cppFormattingMergeId) {
		this.cppFormattingMergeId = cppFormattingMergeId;
	}

	public Integer getCppFormattingId() {
		return cppFormattingId;
	}

	public void setCppFormattingId(Integer cppFormattingId) {
		this.cppFormattingId = cppFormattingId;
	}

	public Integer getFormattingId() {
		return formattingId;
	}

	public void setFormattingId(Integer formattingId) {
		this.formattingId = formattingId;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Long getXhibitClobId() {
		return xhibitClobId;
	}

	public void setXhibitClobId(Long xhibitClobId) {
		this.xhibitClobId = xhibitClobId;
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


}
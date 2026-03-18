package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Basic Value object to retrieve information from the 
 * XHB_CPP_LIST table.
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import java.util.Date;

public class CppListBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	public static interface Status {
        public static final String NOT_PROCESSED = "NP";
        public static final String IN_PROGRESS = "IP";
        public static final String MERGE_SUCCESSFUL = "MS";
        public static final String MERGE_FAILED = "MF";        
    } 
	 
	private Integer cppListId;
	private Integer courtCode;
	private String listType;
	private Date timeLoaded;
	private Date listStartDate;
	private Date listEndDate;
	private Long listClobId;
	private Long mergedClobId;
	private String status;
	private String errorMessage;
	private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;

	public CppListBasicValue() {
	}

	public CppListBasicValue(Integer cppListId, Integer version) {
		super(cppListId, version);
	}

	public Integer getCppListId() {
		return cppListId;
	}

	public void setCppListId(Integer cppListId) {
		this.cppListId = cppListId;
	}

	public Integer getCourtCode() {
		return courtCode;
	}

	public void setCourtCode(Integer courtCode) {
		this.courtCode = courtCode;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public Date getTimeLoaded() {
		return timeLoaded;
	}

	public void setTimeLoaded(Date timeLoaded) {
		this.timeLoaded = timeLoaded;
	}

	public Date getListStartDate() {
		return listStartDate;
	}

	public void setListStartDate(Date listStartDate) {
		this.listStartDate = listStartDate;
	}

	public Date getListEndDate() {
		return listEndDate;
	}

	public void setListEndDate(Date listEndDate) {
		this.listEndDate = listEndDate;
	}

	public Long getListClobId() {
		return listClobId;
	}

	public void setListClobId(Long listClobId) {
		this.listClobId = listClobId;
	}

	public Long getMergedClobId() {
		return mergedClobId;
	}

	public void setMergedClobId(Long mergedClobId) {
		this.mergedClobId = mergedClobId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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
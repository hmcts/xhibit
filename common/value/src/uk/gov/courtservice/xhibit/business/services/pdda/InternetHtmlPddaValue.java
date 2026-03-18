package uk.gov.courtservice.xhibit.business.services.pdda;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Represents what is returned from functions in the package XHB_PDDA_PKG
 * This is essentially a representation of the XHB_INTERNET_HTML table plus
 * an extra column for the row number
 * 
 * @author Scott Atwell
 *
 */
public class InternetHtmlPddaValue extends CSAbstractValue {

	private Integer internetHtmlId;
	private String status;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private String lastUpdatedBy;
	private Integer version;
	private Integer courtId;
	private Long htmlBlobId;
	private Integer rowNumber;
	
	private static final long serialVersionUID = 136306301587389631L;
	
	public InternetHtmlPddaValue() {}
	
	// Full-args constructor
    public InternetHtmlPddaValue(Integer internetHtmlId, String status, Date lastUpdateDate, 
                                 Date creationDate, String createdBy, String lastUpdatedBy, 
                                 Integer version, Integer courtId, Long htmlBlobId, 
                                 Integer rowNumber) {
        this.internetHtmlId = internetHtmlId;
        this.status = status;
        this.lastUpdateDate = lastUpdateDate;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.version = version;
        this.courtId = courtId;
        this.htmlBlobId = htmlBlobId;
        this.rowNumber = rowNumber;
    }
    
    

    public Integer getInternetHtmlId() {
		return internetHtmlId;
	}

	public void setInternetHtmlId(Integer internetHtmlId) {
		this.internetHtmlId = internetHtmlId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Long getHtmlBlobId() {
		return htmlBlobId;
	}

	public void setHtmlBlobId(Long htmlBlobId) {
		this.htmlBlobId = htmlBlobId;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	@Override
    public String toString() {
        return "InternetHtmlPddaValue{" +
                "internetHtmlId=" + internetHtmlId +
                ", status='" + status + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                ", creationDate=" + creationDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", version=" + version +
                ", courtId=" + courtId +
                ", htmlBlobId=" + htmlBlobId +
                ", rowNumber=" + rowNumber +
                '}';
    }
}

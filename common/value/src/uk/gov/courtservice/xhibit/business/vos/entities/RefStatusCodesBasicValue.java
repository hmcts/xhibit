package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Ref Status Codes Basic Value.
 * 
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Luke Gittins
 * @version 1.0
 */
public class RefStatusCodesBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	private Integer refStatusCodeId;
	private String statusCodeType;
	private String statusCode;
	private String statusCodeDescription;
	private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;


    public RefStatusCodesBasicValue() {
    }

    public RefStatusCodesBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
	public Integer getRefStatusCodeId() {
		return refStatusCodeId;
	}

	public void setRefStatusCodeId(Integer refStatusCodeId) {
		this.refStatusCodeId = refStatusCodeId;
	}

	public String getStatusCodeType() {
		return statusCodeType;
	}

	public void setStatusCodeType(String statusCodeType) {
		this.statusCodeType = statusCodeType;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCodeDescription() {
		return statusCodeDescription;
	}

	public void setStatusCodeDescription(String statusCodeDescription) {
		this.statusCodeDescription = statusCodeDescription;
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
package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Ref PDDA Message Type Basic Value.
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
public class RefPddaMessageTypeBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	private Integer refPddaMessageTypeId;
	private String pddaMessageType;
	private String pddaMessageTypeDescription;
	private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;


    public RefPddaMessageTypeBasicValue() {
    }

    public RefPddaMessageTypeBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
	public Integer getRefPddaMessageTypeId() {
		return refPddaMessageTypeId;
	}

	public void setRefPddaMessageTypeId(Integer refPddaMessageTypeId) {
		this.refPddaMessageTypeId = refPddaMessageTypeId;
	}

	public String getPddaMessageType() {
		return pddaMessageType;
	}

	public void setPddaMessageType(String pddaMessageType) {
		this.pddaMessageType = pddaMessageType;
	}

	public String getPddaMessageTypeDescription() {
		return pddaMessageTypeDescription;
	}

	public void setPddaMessageTypeDescription(String pddaMessageTypeDescription) {
		this.pddaMessageTypeDescription = pddaMessageTypeDescription;
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
package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Darts Disposal Retention Policies Basic Value.
 * 
 * <p>
 * Copyright: Copyright (c) 2021
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class RefDispRetentionPolicyBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	private Integer refDispRetentionPolicyId;
	private String disposalCode;
	private Integer refDarRetentionPolicyId;
    private String allowsConsecutive;
    private String hasLife;
    private String hasDuration;
    private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;

    public RefDispRetentionPolicyBasicValue() {
    }

    public RefDispRetentionPolicyBasicValue(Integer id, Integer version) {
        super(id, version);
    }

	public Integer getRefDispRetentionPolicyId() {
		return refDispRetentionPolicyId;
	}

	public void setRefDispRetentionPolicyId(Integer refDispRetentionPolicyId) {
		this.refDispRetentionPolicyId = refDispRetentionPolicyId;
	}

	public String getDisposalCode() {
		return disposalCode;
	}

	public void setDisposalCode(String disposalCode) {
		this.disposalCode = disposalCode;
	}
	
	public Integer getRefDarRetentionPolicyId() {
		return refDarRetentionPolicyId;
	}

	public void setRefDarRetentionPolicyId(Integer refDarRetentionPolicyId) {
		this.refDarRetentionPolicyId = refDarRetentionPolicyId;
	}

	public String getAllowsConsecutive() {
		return allowsConsecutive;
	}

	public void setAllowsConsecutive(String allowsConsecutive) {
		this.allowsConsecutive = allowsConsecutive;
	}

	public String getHasLife() {
		return hasLife;
	}

	public void setHasLife(String hasLife) {
		this.hasLife = hasLife;
	}

	public String getHasDuration() {
		return hasDuration;
	}

	public void setHasDuration(String hasDuration) {
		this.hasDuration = hasDuration;
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
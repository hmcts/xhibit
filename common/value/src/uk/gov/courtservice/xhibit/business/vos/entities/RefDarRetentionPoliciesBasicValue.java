package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Darts Retention Policies Basic Value.
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
public class RefDarRetentionPoliciesBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	private Integer refDarRetentionPolicyId;
    private Integer policyNo;
    private String policyDescription;
    private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;

    public RefDarRetentionPoliciesBasicValue() {
    }

    public RefDarRetentionPoliciesBasicValue(Integer id, Integer version) {
        super(id, version);
    }

	public Integer getRefDarRetentionPolicyId() {
		return refDarRetentionPolicyId;
	}

	public void setRefDarRetentionPolicyId(Integer refDarRetentionPolicyId) {
		this.refDarRetentionPolicyId = refDarRetentionPolicyId;
	}

	public Integer getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(Integer policyNo) {
		this.policyNo = policyNo;
	}

	public String getPolicyDescription() {
		return policyDescription;
	}

	public void setPolicyDescription(String policyDescription) {
		this.policyDescription = policyDescription;
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
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
public class DarRetentionPolicyBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	private Integer darRetentionPolicyId;
	private Integer disposal2Id;
	private Integer caseId; 
	private Integer defendantOnCaseId; 
	private Integer defendantOnOffenceId;
	private Integer refDispRetentionPolicyId;
	private Integer refDarRetentionPolicyId;
	private Integer durationDays;
	private Integer durationMonths;
	private Integer durationYears;
	private String hasLife;
	private String isConsecutive;
	private String isUpdated;
	private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;


    public DarRetentionPolicyBasicValue() {
    }

    public DarRetentionPolicyBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
	public Integer getDarRetentionPolicyId() {
		return darRetentionPolicyId;
	}

	public void setDarRetentionPolicyId(Integer darRetentionPolicyId) {
		this.darRetentionPolicyId = darRetentionPolicyId;
	}

	public Integer getDisposal2Id() {
		return disposal2Id;
	}

	public void setDisposal2Id(Integer disposal2Id) {
		this.disposal2Id = disposal2Id;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}

	public Integer getDefendantOnOffenceId() {
		return defendantOnOffenceId;
	}

	public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
		this.defendantOnOffenceId = defendantOnOffenceId;
	}

	public Integer getRefDispRetentionPolicyId() {
		return refDispRetentionPolicyId;
	}

	public void setRefDispRetentionPolicyId(Integer refDispRetentionPolicyId) {
		this.refDispRetentionPolicyId = refDispRetentionPolicyId;
	}

	public Integer getRefDarRetentionPolicyId() {
		return refDarRetentionPolicyId;
	}

	public void setRefDarRetentionPolicyId(Integer refDarRetentionPolicyId) {
		this.refDarRetentionPolicyId = refDarRetentionPolicyId;
	}
	
	public Integer getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(Integer durationDays) {
		this.durationDays = durationDays;
	}

	public Integer getDurationMonths() {
		return durationMonths;
	}

	public void setDurationMonths(Integer durationMonths) {
		this.durationMonths = durationMonths;
	}

	public Integer getDurationYears() {
		return durationYears;
	}

	public void setDurationYears(Integer durationYears) {
		this.durationYears = durationYears;
	}

	public String getHasLife() {
		return hasLife;
	}

	public void setHasLife(String hasLife) {
		this.hasLife = hasLife;
	}

	public String getIsConsecutive() {
		return isConsecutive;
	}

	public void setIsConsecutive(String isConsecutive) {
		this.isConsecutive = isConsecutive;
	}

	public String getIsUpdated() {
		return isUpdated;
	}

	public void setIsUpdated(String isUpdated) {
		this.isUpdated = isUpdated;
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
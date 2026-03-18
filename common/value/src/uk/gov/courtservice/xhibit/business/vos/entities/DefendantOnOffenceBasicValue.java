package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class DefendantOnOffenceBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer defendantOnOffenceId;
	private String appealAgainstType;
	private Integer defendantOnCaseId;
	private Integer offenceId;
	private java.util.Date lastUpdateDate;
	private java.util.Date creationDate;
	private String createdBy;
	private String lastUpdatedBy;
	private Integer version;
	private String obsInd;
	private String isStayed;
	private String crnId;
	private String vcoFlag;
	private java.util.Date vcoDate;
	private Integer seqNo;
	private java.util.Date arrestDate;
	private java.util.Date chargeDate;
	private String isCommittedOnBail;
	private String interimD20;
	private Integer darRetentionPolicyId;

	public String getInterimD20() {
		return interimD20;
	}

	public void setInterimD20(String interimD20) {
		this.interimD20 = interimD20;
	}

	public DefendantOnOffenceBasicValue(Integer defendantOnOffenceId, Integer version) {
		super(defendantOnOffenceId, version);
	}

	public Integer getPrimaryKey() {
		return getDefendantOnOffenceId();
	}

	public Integer getDefendantOnOffenceId() {
		return this.defendantOnOffenceId;
	}

	public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
		this.defendantOnOffenceId = defendantOnOffenceId;
	}

	public String getAppealAgainstType() {
		return this.appealAgainstType;
	}

	public void setAppealAgainstType(String appealAgainstType) {
		this.appealAgainstType = appealAgainstType;
	}

	public Integer getDefendantOnCaseId() {
		return this.defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}

	public Integer getOffenceId() {
		return this.offenceId;
	}

	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}

	public java.util.Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(java.util.Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public java.util.Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(java.util.Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getObsInd() {
		return this.obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}

	public String getIsStayed() {
		return this.isStayed;
	}

	public void setIsStayed(String isStayed) {
		this.isStayed = isStayed;
	}

	public String getCrnId() {
		return this.crnId;
	}

	public void setCrnId(String crnId) {
		this.crnId = crnId;
	}

	public String getVcoFlag() {
		return this.vcoFlag;
	}

	public void setVcoFlag(String vcoFlag) {
		this.vcoFlag = vcoFlag;
	}

	public java.util.Date getVcoDate() {
		return this.vcoDate;
	}

	public void setVcoDate(java.util.Date vcoDate) {
		this.vcoDate = vcoDate;
	}

	public Integer getSeqNo() {
		return this.seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public java.util.Date getArrestDate() {
		return this.arrestDate;
	}

	public void setArrestDate(java.util.Date arrestDate) {
		this.arrestDate = arrestDate;
	}

	public java.util.Date getChargeDate() {
		return this.chargeDate;
	}

	public void setChargeDate(java.util.Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	public String getIsCommittedOnBail() {
		return this.isCommittedOnBail;
	}

	public void setIsCommittedOnBail(String isCommittedOnBail) {
		this.isCommittedOnBail = isCommittedOnBail;
	}

	public DefendantOnOffenceBasicValue(Integer defendantOnOffenceId, String appealAgainstType,
			Integer defendantOnCaseId, Integer offenceId, java.util.Date lastUpdateDate, java.util.Date creationDate,
			String createdBy, String lastUpdatedBy, Integer version, String obsInd, String isStayed, String crnId,
			String vcoFlag, java.util.Date vcoDate, Integer seqNo, java.util.Date arrestDate, java.util.Date chargeDate,
			String isCommittedOnBail, String interimD20) {
		setDefendantOnOffenceId(defendantOnOffenceId);
		setAppealAgainstType(appealAgainstType);
		setDefendantOnCaseId(defendantOnCaseId);
		setOffenceId(offenceId);
		setLastUpdateDate(lastUpdateDate);
		setCreationDate(creationDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setVersion(version);
		setObsInd(obsInd);
		setIsStayed(isStayed);
		setCrnId(crnId);
		setVcoFlag(vcoFlag);
		setVcoDate(vcoDate);
		setSeqNo(seqNo);
		setArrestDate(arrestDate);
		setChargeDate(chargeDate);
		setIsCommittedOnBail(isCommittedOnBail);
		setInterimD20(interimD20);
	}

	public DefendantOnOffenceBasicValue(Integer defendantOnOffenceId) {
		setDefendantOnOffenceId(defendantOnOffenceId);
	}

	public Integer getDarRetentionPolicyId() {
		return darRetentionPolicyId;
	}

	public void setDarRetentionPolicyId(Integer darRetentionPolicyId) {
		this.darRetentionPolicyId = darRetentionPolicyId;
	}

}
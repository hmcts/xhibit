package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class DARTSRetentionPolicyValue extends CSAbstractValue{
    private static final long serialVersionUID = 1L;

	private String lastUpdateDate;
	private String caseType;
	private Integer caseNumber;
	private Integer policyNumber;
	private String policyDescription;
	private Integer durationDays;
	private Integer durationMonths;
	private Integer durationYears;
	private String hasLife;
	
	
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
	public String getCaseType() {
		return caseType;
	}
	
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	
	
	public Integer getCaseNumber() {
		return caseNumber;
	}
	
	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}
	
	
	public Integer getPolicyNumber() {
		return policyNumber;
	}
	
	public void setPolicyNumber(Integer policyNumber) {
		this.policyNumber = policyNumber;
	}
	
	
	public String getPolicyDescription() {
		return policyDescription;
	}
	
	public void setPolicyDescription(String policyDescription) {
		this.policyDescription = policyDescription;
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
}

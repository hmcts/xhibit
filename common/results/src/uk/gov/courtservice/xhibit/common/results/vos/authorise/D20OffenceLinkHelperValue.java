package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
public class D20OffenceLinkHelperValue extends CSAbstractValue {
	private static final long serialVersionUID = 2537605543014670338L;
	
	private Integer defendantOnCaseId;
	
	private Integer defendantId;
	
	private String defendantName; 
	
	private Integer xhibitCourtId;
	
	private String caseId;
	
	private String userName;
	
	private Integer xhibitCaseId;
	
	private Integer hearingID;
	
	private String d20Interim = "N";
	
	private boolean trialCase, sentenceCase, breachCase, appealCase;
	
	private Collection<String> refD20Offences;
	
	/**
	 * Getters and setters
	 * 
	 */
	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}

	public Integer getDefendantId() {
		return defendantId;
	}

	public void setDefendantId(Integer defendantId) {
		this.defendantId = defendantId;
	}

	public Collection<String> getRefD20Offences() {
		return refD20Offences;
	}

	public void setRefD20Offences(Collection<String> refD20Offences) {
		this.refD20Offences = refD20Offences;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public Integer getXhibitCaseId() {
		return xhibitCaseId;
	}

	public void setXhibitCaseId(Integer xhibitCaseId) {
		this.xhibitCaseId = xhibitCaseId;
	}


	public void setD20Interim(String d20Interim) {
		this.d20Interim = d20Interim;
	}

	public boolean isD20Interim() {
		return d20Interim.equals("Y") ? true : false;
	}

	public Integer getHearingID() {
		return hearingID;
	}

	public void setHearingID(Integer hearingID) {
		this.hearingID = hearingID;
	}

	public boolean isBreachCase() {
		return breachCase;
	}

	public void setBreachCase(boolean bCase) {
		this.breachCase = bCase;
	}

	public boolean isSentenceCase() {
		return sentenceCase;
	}

	public void setSentenceCase(boolean sCase) {
		this.sentenceCase = sCase;
	}

	public boolean isTrialCase() {
		return trialCase;
	}

	public void setTrialCase(boolean tCase) {
		this.trialCase = tCase;
	}

	public Integer getXhibitCourtId() {
		return xhibitCourtId;
	}

	public void setXhibitCourtId(Integer xhibitCourtId) {
		this.xhibitCourtId = xhibitCourtId;
	}

	public String getDefendantName() {
		return defendantName;
	}

	public void setDefendantName(String defendantName) {
		this.defendantName = defendantName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isaCase() {
		return appealCase;
	}

	public void setaCase(boolean aCase) {
		this.appealCase = aCase;
	}
}
package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;


public class RefAggravatingReasonsBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	private Integer refAggravatingReasonsId;
	private String reasonDescription;
	private String obsInd;
 	
	public RefAggravatingReasonsBasicValue() {
		super();
	}
	
	public RefAggravatingReasonsBasicValue(Integer id, Integer version) {
		super(id, version);
	}

	public Integer getRefAggravatingReasonsId() {
		return refAggravatingReasonsId;
	}

	public void setRefAggravatingReasonsId(Integer refAggravatingReasonsId) {
		this.refAggravatingReasonsId = refAggravatingReasonsId;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}
	
	public String getObsInd() {
		return obsInd;
	}
	
	public boolean isAssaultOnWorkers() {
		return isContainingWord("assaults") && isContainingWord("workers");
	}
	
	public boolean isTerroristConnection() {
		return isContainingWord("terrorist") && isContainingWord("connection");
	}
	
	public boolean isEmergencyWorkers() {
		return isContainingWord("emergency") && isContainingWord("worker");
	}
	
	public boolean isHostility() {
		return isContainingWord("hostility");	
	}
	
	public boolean isSexualOrientation() {
		return isContainingWord("sexual") && isContainingWord("orientation") && 
				!isContainingWord("victim");
	}
	
	public boolean isSexualOrientationOfVictim() {
		return isContainingWord("sexual") && isContainingWord("orientation") && 
				isContainingWord("victim");
	}
	
	public boolean isTransgender() {
		return isContainingWord("transgender") && !isContainingWord("victim");	
	}
	
	public boolean isTransgenderOfVictim() {
		return isContainingWord("transgender") && isContainingWord("victim");	
	}
	
	private boolean isContainingWord(String word) {
		return getReasonDescription() != null &&
				getReasonDescription().toLowerCase().contains(word);
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
}

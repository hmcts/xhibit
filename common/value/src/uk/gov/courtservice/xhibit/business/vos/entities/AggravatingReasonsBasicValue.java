package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;


public class AggravatingReasonsBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	private Integer aggravatingReasonsId;
	private Integer defendantOnCaseId;
	private Integer refAggravatingReasonsId;
	private String obsInd;
 	
	public AggravatingReasonsBasicValue() {
		super();
	}
	
	public AggravatingReasonsBasicValue(Integer id, Integer version) {
		super(id, version);
	}
 	
	public Integer getAggravatingReasonsId() {
		return aggravatingReasonsId;
	}

	public void setAggravatingReasonsId(Integer aggravatingReasonsId) {
		this.aggravatingReasonsId = aggravatingReasonsId;
	}	

	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}	

	public Integer getRefAggravatingReasonsId() {
		return refAggravatingReasonsId;
	}

	public void setRefAggravatingReasonsId(Integer refAggravatingReasonsId) {
		this.refAggravatingReasonsId = refAggravatingReasonsId;
	}
	
	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
}

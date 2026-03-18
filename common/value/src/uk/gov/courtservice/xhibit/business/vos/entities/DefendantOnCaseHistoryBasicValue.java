package uk.gov.courtservice.xhibit.business.vos.entities;


import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Basic value object used to represent the values returned from Defendant 
 * On Case History table.
 * @author waltersn
 *
 */
public class DefendantOnCaseHistoryBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = -250618477828730767L;
	private Integer defendantOnCaseHistoryId;
	private Integer defendantHistoryId;
	private Integer caseHistoryId;
	private Integer defendantNumber;
	
	public DefendantOnCaseHistoryBasicValue() {
        super();
    }
	
    public DefendantOnCaseHistoryBasicValue(Integer id, Integer version) {
        super(id, version);
    }

	public Integer getDefendantOnCaseHistoryId() {
		return defendantOnCaseHistoryId;
	}

	public void setDefendantOnCaseHistoryId(Integer defendantOnCaseHistoryId) {
		this.defendantOnCaseHistoryId = defendantOnCaseHistoryId;
	}

	public Integer getDefendantHistoryId() {
		return defendantHistoryId;
	}

	public void setDefendantHistoryId(Integer defendantHistoryId) {
		this.defendantHistoryId = defendantHistoryId;
	}

	public Integer getCaseHistoryId() {
		return caseHistoryId;
	}

	public void setCaseHistoryId(Integer caseHistoryId) {
		this.caseHistoryId = caseHistoryId;
	}

	public Integer getDefendantNumber() {
		return defendantNumber;
	}

	public void setDefendantNumber(Integer defendantNumber) {
		this.defendantNumber = defendantNumber;
	}

}

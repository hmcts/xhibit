package uk.gov.courtservice.xhibit.client.listings.list.common;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;


public class RemoveCaseFromListModel {

	private CaseOnListComplexValue caseOnListComplexValue;
	private	String hearingTypeCode;
	private boolean modalSave = false;

	/**
	 * Constructor
	 * @param CaseOnListComplexValue
	 */
	public RemoveCaseFromListModel(CaseOnListComplexValue caseOnListComplexValue) {
		this(caseOnListComplexValue, false);
	}
	
	/**
	 * Constructor
	 * @param CaseOnListComplexValue
	 */
	public RemoveCaseFromListModel(CaseOnListComplexValue caseOnListComplexValue, boolean isModalSave) {
		setCaseOnListComplexValue(caseOnListComplexValue);
		setModalSave(isModalSave);
	}

	/**
	 * Retrieves the CaseDiaryFixtureComplexValue object
	 * 
	 * @return CaseDiaryFixtureComplexValue
	 */
	public CaseOnListComplexValue getCaseOnListComplexValue() {
		return caseOnListComplexValue;
	}

	/**
	 * Sets the CaseDiaryFixtureComplexValue object
	 * 
	 * @param  CaseDiaryFixtureComplexValue
	 */
	public void setCaseOnListComplexValue(CaseOnListComplexValue caseOnListComplexValue) {
		this.caseOnListComplexValue = caseOnListComplexValue;
	}
	
	/**
	 * @return the hearingTypeCode
	 */
	public String getHearingTypeCode() {
		return hearingTypeCode;
	}

	/**
	 * @param hearingTypeCode the hearingTypeCode to set
	 */
	public void setHearingTypeCode(String hearingTypeCode) {
		this.hearingTypeCode = hearingTypeCode;
	}

	public boolean isModalSave() {
		return modalSave;
	}

	public void setModalSave(boolean modalSave) {
		this.modalSave = modalSave;
	}	
}
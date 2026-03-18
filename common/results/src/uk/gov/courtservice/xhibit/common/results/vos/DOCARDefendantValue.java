package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class DOCARDefendantValue extends CSAbstractValue{
    private static final long serialVersionUID = 1L;

	private String caseType;
	private Integer caseNumber;
	private Integer defendantNumber;
	private String firstName;
	private String middleName;
	private String surname;
	private String formNGDate;
	
	/**
	 * @return the caseNumber
	 */
	public Integer getCaseNumber() {
		return caseNumber;
	}

	/**
	 * @param caseNumber the caseNumber to set
	 */
	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}

	/**
	 * @return the defendantNumber
	 */
	public Integer getDefendantNumber() {
		return defendantNumber;
	}

	/**
	 * @param defendantNumber the defendantNumber to set
	 */
	public void setDefendantNumber(Integer defendantNumber) {
		this.defendantNumber = defendantNumber;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the formNGDate
	 */
	public String getFormNGDate() {
		return formNGDate;
	}

	/**
	 * @param date the formNGDate to set
	 */
	public void setFormNGDate(String date) {
		this.formNGDate = date;
	}

	/**
	 * @return the caseType
	 */
	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;		
	}

}

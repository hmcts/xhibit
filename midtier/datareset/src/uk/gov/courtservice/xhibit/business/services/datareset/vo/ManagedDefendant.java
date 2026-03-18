package uk.gov.courtservice.xhibit.business.services.datareset.vo;

import java.util.Date;

/**
 * <p>
 * Title: Managed Defendant
 * </p>
 * <p>
 * Description: A ManagedDefendant object represents a defendant in a managed case;
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * 
 */
public class ManagedDefendant implements java.io.Serializable {//, java.lang.Comparable {

	private String firstName;
	private String surname;
	private String middleName;
	private Integer defendantOnCaseId;
	private Integer defendantId;
	private Date dateOfBirth;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
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
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	
	
}

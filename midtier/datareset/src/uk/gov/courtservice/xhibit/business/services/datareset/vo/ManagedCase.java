package uk.gov.courtservice.xhibit.business.services.datareset.vo;

import java.util.Date;

/**
 * <p>
 * Title: Managed Case
 * </p>
 * <p>
 * Description: A ManagedCase object represents a managed case;
 * ;a combo of managedcase, case, defendantoncase and defendant.
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
public class ManagedCase implements java.io.Serializable {
	
	private static final long serialVersionUID = -8754922427159403131L;
//, java.lang.Comparable {

	private Integer caseNumber;
	private Integer caseId;
	private Integer courtId;
	private String courtName;
	private Integer managedCaseId;
	private String caseType;
	private Integer defendantId;
	private String defendantName;
	private String defendantFirstName;
	private String defendantMiddleName;
	private String defendantSurname;
	private Date defendantDOB;
	
	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	
	public String getDefendantFirstName() {
		return defendantFirstName;
	}

	public void setDefendantFirstName(String defendantFirstName) {
		this.defendantFirstName = defendantFirstName;
	}

	public String getDefendantMiddleName() {
		return defendantMiddleName;
	}

	public void setDefendantMiddleName(String defendantMiddleName) {
		this.defendantMiddleName = defendantMiddleName;
	}

	public String getDefendantSurname() {
		return defendantSurname;
	}

	public void setDefendantSurname(String defendantSurname) {
		this.defendantSurname = defendantSurname;
	}
	
	public String getDefendantName() {
		return defendantName;
	}

	public void setDefendantName(String defendantName) {
		this.defendantName = defendantName;
	}

	public Date getDefendantDOB() {
		return defendantDOB;
	}

	public void setDefendantDOB(Date defendantDOB) {
		this.defendantDOB = defendantDOB;
	}

	public Integer getDefendantId() {
		return defendantId;
	}

	public void setDefendantId(Integer defendantId) {
		this.defendantId = defendantId;
	}

	private ManagedDefendantOnCase mdoc;

	public Integer getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Integer getManagedCaseId() {
		return managedCaseId;
	}

	public void setManagedCaseId(Integer managedCaseId) {
		this.managedCaseId = managedCaseId;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public ManagedDefendantOnCase getMdoc() {
		return mdoc;
	}

	public void setMdoc(ManagedDefendantOnCase mdoc) {
		this.mdoc = mdoc;
	}
	
	
	
}

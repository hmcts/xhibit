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
public class ManagedDefendantOnCase implements java.io.Serializable {//, java.lang.Comparable {

	private Integer caseId;
	private Integer defendantOnCaseId;
	
	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}

	public ManagedDefendant getMd() {
		return md;
	}

	public void setMd(ManagedDefendant md) {
		this.md = md;
	}

	private ManagedDefendant md;
	
	
	
}

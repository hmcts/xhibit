package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CaseNonAvailDaysBasicValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class CaseNonAvailDaysBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer nadId;
	private Integer caseId;
	private Date startDate;
	private Date endDate;
	private String reason;
	private String obsInd;

	public CaseNonAvailDaysBasicValue() {
		super();
	}

	public CaseNonAvailDaysBasicValue(Integer id, Integer version) {
		super(id, version);
	}

	public Integer getNadId() {
		return nadId;
	}

	public void setNadId(Integer nadId) {
		this.nadId = nadId;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
}
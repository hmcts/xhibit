package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: FixtureDeftAttendingBasicValue
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
 * @author Jasvir Boparai
 * @version 1.0
 */
public class FixtureDeftAttendingBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer fixtureDeftAttendingId;
	private Integer defendantOnCaseId;
	private Integer caseDiaryFixtureId;
	private String attending;
	private String obsInd;

	public FixtureDeftAttendingBasicValue() {
		super();
	}

	public FixtureDeftAttendingBasicValue(Integer id, Integer version) {
		super(id, version);
	}
	
	public Integer getFixtureDeftAttendingId() {
		return fixtureDeftAttendingId;
	}

	public void setFixtureDeftAttendingId(Integer fixtureDeftAttendingId) {
		this.fixtureDeftAttendingId = fixtureDeftAttendingId;
	}

	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}

	public Integer getCaseDiaryFixtureId() {
		return caseDiaryFixtureId;
	}

	public void setCaseDiaryFixtureId(Integer caseDiaryFixtureId) {
		this.caseDiaryFixtureId = caseDiaryFixtureId;
	}

	public String getAttending() {
		return attending;
	}

	public void setAttending(String attending) {
		this.attending = attending;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
}
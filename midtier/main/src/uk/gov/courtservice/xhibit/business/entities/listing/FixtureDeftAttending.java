package uk.gov.courtservice.xhibit.business.entities.listing;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface FixtureDeftAttending extends CSEntityLocal {
 
	public Integer getFixtureDeftAttendingId();
	public Integer getDefendantOnCaseId();
	public Integer getCaseDiaryFixtureId();
	public String getAttending();
	public String getObsInd();
	
	public void setFixtureDeftAttendingId(Integer fixtureDeftAttendingId);
	public void setDefendantOnCaseId(Integer defendantOnCaseId);
	public void setCaseDiaryFixtureId(Integer caseDiaryFixtureId);
	public void setAttending(String attending);
	public void setObsInd(String obsInd);
}
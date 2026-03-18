package uk.gov.courtservice.xhibit.business.entities.listing;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class FixtureDeftAttendingBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer defendantOnCaseId,Integer caseDiaryFixtureId,
    		String attending,String obsInd, String userDisplayName) throws CreateException {
    	setDefendantOnCaseId(defendantOnCaseId);
    	setCaseDiaryFixtureId(caseDiaryFixtureId);
    	setAttending(attending);
    	setObsInd(obsInd);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(Integer defendantOnCaseId,Integer caseDiaryFixtureId,
    		String attending, String obsInd, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------
	public abstract Integer getFixtureDeftAttendingId();
	public abstract Integer getDefendantOnCaseId();
	public abstract Integer getCaseDiaryFixtureId();
	public abstract String getAttending();
	public abstract String getObsInd();
    public abstract void setFixtureDeftAttendingId(Integer fixtureDeftAttendingId);
	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);
	public abstract void setCaseDiaryFixtureId(Integer caseDiaryFixtureId);
	public abstract void setAttending(String attending);
	public abstract void setObsInd(String obsInd);
    
}
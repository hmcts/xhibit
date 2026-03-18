package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface FixtureDeftAttendingHome extends EJBLocalHome {
    public FixtureDeftAttending create(Integer defendantOnCaseId,Integer caseDiaryFixtureId,
    		String attending, String obsInd, String userDisplayName) throws CreateException;

    public FixtureDeftAttending findByPrimaryKey(Integer fixtureDeftAttendingId) throws FinderException;

    public Collection findByCaseDiaryFixtureId(Integer caseDiaryFixtureId) throws FinderException;
}
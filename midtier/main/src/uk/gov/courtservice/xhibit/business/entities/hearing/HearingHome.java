package uk.gov.courtservice.xhibit.business.entities.hearing;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface HearingHome extends EJBLocalHome {
    public Hearing create(Integer caseId, Integer refHearingTypeId, Integer courtId, String mpHearingType,
            Timestamp hearingStartDate, Timestamp hearingEndDate, Integer linkedHearingId, String userDisplayName) throws CreateException;

    public Hearing findByPrimaryKey(Integer hearingId) throws FinderException;

    public Collection findByCaseId(Integer caseId) throws FinderException;

    /**
     * Find all hearings for the specified linkedhearing id.
     * 
     * @param hearingId
     * @return Collection of hearings for the specified linkedhearingid.
     * @throws FinderException
     */
    public Collection findByLinkedHearingId(Integer linkedHearingId) throws FinderException;
}

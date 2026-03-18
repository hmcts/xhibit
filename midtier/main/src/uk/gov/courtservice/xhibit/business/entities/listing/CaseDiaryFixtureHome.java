package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface CaseDiaryFixtureHome extends EJBLocalHome
{
	public CaseDiaryFixture create(Integer caseListingEntryId, Date listingDate, String fixtureNoticeRequired,
			Integer hearingTypeId, Integer listNotePreDefinedId, String listNoteText, Integer preDefNoteClassId, Integer freeTextNoteClassId,
			Integer vacationPreDefinedRsonId, String vacationFreetextReason, String status, Integer courtSiteId, 
			Date dateVacated, String userDisplayName)
			throws CreateException;

	public CaseDiaryFixture findByPrimaryKey(Integer pk) 
			throws FinderException;

	public Collection findByCaseListingEntryIdAndStatus(Integer caseListingEntryId, String status) throws FinderException;
	
	public Collection findByCaseListingEntryIdAndStatusAndListingDate(Integer caseListingEntryId, String status, Date fromDate) throws FinderException;
	
	public Collection findByCaseIdAndListingDate(Integer caseId, Date fromDate, Date toDate) throws FinderException;
	
	public Collection findByCaseIdAndListingDateAndDefendantOnCaseId(Integer caseId, Date fromDate, Date toDate, Integer defendantOnCaseId) throws FinderException;
	
	public Collection findCaseDiaryFixturesImpacted(Integer caseEntryId, Date startDate, Date endDate, Date currentDate) throws FinderException;
	
	public Collection findByListingDateAndCourtId(Date fixtureDate, Integer courtId) throws FinderException;
}

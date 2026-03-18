package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface CaseOnListHome extends EJBLocalHome {
    public CaseOnList create(Integer caseOnListId, Integer caseId, Integer listId, Integer sittingOnListId, Integer courtSiteId,
			Integer courtRoomId, String reserved, String floaterCase, Integer timeMarkingId, Date timeListed,
			String isCourtRoomListEntry, Integer hearingTypeId, String reasonForRemoval, Integer crackedIneffectiveId,
			String obsInd, String userDisplayName, Integer seqNo, Integer caseDiaryFixtureId, Date dateOfRemoval,
			Integer listNotePredefinedId, String listNoteText, Integer parentCaseOnListId, String nhaFirmList,
			Integer vacationPreDefinedRsonId) throws CreateException;

    public CaseOnList findByPrimaryKey(Integer caseOnListId) throws FinderException;

    public Collection findByCaseId(Integer caseId) throws FinderException;

    public Collection findByListId(Integer listId) throws FinderException;
    
    public Collection findByCourtSiteIdAndCourtRoomId(Integer courtSiteId, Integer courtRoomId) throws FinderException;
}
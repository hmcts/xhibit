package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * Case Listing Entry bean.
 */
abstract public class CaseDiaryFixtureBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer caseListingEntryId, Date listingDate, String fixtureNoticeRequired,
			Integer hearingTypeId, Integer listNotePreDefinedId, String listNoteText, Integer preDefNoteClassId, Integer freeTextNoteClassId,
			Integer vacationPreDefinedRsonId, String vacationFreetextReason, String status, Integer courtSiteId, 
			Date dateVacated, String userDisplayName) throws CreateException {		

		setCaseListingEntryId(caseListingEntryId);
		setListingDate(listingDate) ;
		setFixtureNoticeRequired(fixtureNoticeRequired);
		setHearingTypeId(hearingTypeId);
		setListNotePreDefinedId(listNotePreDefinedId);
		setListNoteText(listNoteText);
		setPreDefNoteClassId(preDefNoteClassId);
		setFreeTextNoteClassId(freeTextNoteClassId);
		setVacationPreDefinedRsonId(vacationPreDefinedRsonId);
		setVacationFreetextReason(vacationFreetextReason);
		setStatus(status);
		setCourtSiteId(courtSiteId);
		setDateVacated(dateVacated);
		setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
		return null;
	}
    
    public void ejbPostCreate(Integer caseListingEntryId, Date listingDate, String fixtureNoticeRequired,
			Integer hearingTypeId, Integer listNotePreDefinedId, String listNoteText, Integer preDefNoteClassId, Integer freeTextNoteClassId,
			Integer vacationPreDefinedRsonId, String vacationFreetextReason, String status, Integer courtSiteId, 
			Date dateVacated, String userDisplayName) throws CreateException {
    }
	
    public abstract Integer getCaseDiaryFixtureId();
    public abstract Integer getCaseListingEntryId();
    public abstract Date getListingDate();
    public abstract String getFixtureNoticeRequired();
    public abstract Integer getHearingTypeId();
    public abstract Integer getListNotePreDefinedId();
    public abstract String getListNoteText();
    public abstract Integer getPreDefNoteClassId();
    public abstract Integer getFreeTextNoteClassId();
    public abstract Integer getVacationPreDefinedRsonId();
    public abstract String getVacationFreetextReason();
    public abstract String getStatus();
    public abstract String getObsInd();
    public abstract Integer getCourtSiteId();
	public abstract Date getDateVacated();
    public abstract void setCaseDiaryFixtureId(Integer caseDiaryFixtureId) ;
    public abstract void setCaseListingEntryId(Integer caseListingEntryId);
    public abstract void setListingDate(Date listingDate);
    public abstract void setFixtureNoticeRequired(String fixtureNoticeRequired);
	public abstract void setHearingTypeId( java.lang.Integer hearingTypeId);
    public abstract void setListNotePreDefinedId( java.lang.Integer listNotePreDefinedId);
	public abstract void setListNoteText( java.lang.String listNoteText);
	public abstract void setPreDefNoteClassId( java.lang.Integer preDefinedClassId) ;
	public abstract void setFreeTextNoteClassId(Integer freeTextNoteClassId);
	public abstract void setVacationPreDefinedRsonId(Integer vacationPreDefinedRsonId);
	public abstract void setVacationFreetextReason(String vacationFreetextReason);
	public abstract void setStatus(String status);
	public abstract void setObsInd(String obsInd);
	public abstract void setCourtSiteId(java.lang.Integer courtSiteId);
	public abstract void setDateVacated(Date dateVacated);
}
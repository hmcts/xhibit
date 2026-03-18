package uk.gov.courtservice.xhibit.business.entities.listing;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface CaseDiaryFixture extends CSEntityLocal {
    
	   public java.lang.Integer getCaseDiaryFixtureId(  ) ;
	   public void setCaseDiaryFixtureId( java.lang.Integer caseDiaryFixtureId ) ;
	   
	   public java.lang.Integer getCaseListingEntryId(  ) ;
	   public void setCaseListingEntryId( java.lang.Integer caseListingEntryId ) ;

	   public java.util.Date getListingDate(  ) ;
	   public void setListingDate( java.util.Date listingDate ) ;
	   
	   public java.lang.String getFixtureNoticeRequired(  ) ;
	   public void setFixtureNoticeRequired( java.lang.String fixtureNoticeRequired ) ;

	   public java.lang.Integer getHearingTypeId(  ) ;
	   public void setHearingTypeId( java.lang.Integer hearingTypeId ) ;

	   public java.lang.Integer getListNotePreDefinedId(  ) ;
	   public void setListNotePreDefinedId( java.lang.Integer listNotePreDefinedId ) ;

	   public java.lang.String getListNoteText(  ) ;
	   public void setListNoteText( java.lang.String listNoteText ) ;

	   public java.lang.Integer getPreDefNoteClassId(  ) ;
	   public void setPreDefNoteClassId( java.lang.Integer preDefNoteClassId ) ;

	   public java.lang.Integer getFreeTextNoteClassId(  ) ;
	   public void setFreeTextNoteClassId( java.lang.Integer freeTextNoteClassId ) ;

	   public java.lang.Integer getVacationPreDefinedRsonId(  ) ;
	   public void setVacationPreDefinedRsonId( java.lang.Integer vacationPreDefinedRsonId ) ;

	   public java.lang.String getVacationFreetextReason(  ) ;
	   public void setVacationFreetextReason( java.lang.String vacationFreetextReason ) ;

	   public java.lang.String getStatus(  ) ;
	   public void setStatus( java.lang.String status ) ;

	   public java.lang.String getObsInd( );
	   public void setObsInd(java.lang.String obsInd ) ;
	   
	   public java.lang.Integer getCourtSiteId(  ) ;
	   public void setCourtSiteId( java.lang.Integer courtSiteId ) ;
	   
	   public java.util.Date getDateVacated(  ) ;
	   public void setDateVacated( java.util.Date dateVacated ) ;
}
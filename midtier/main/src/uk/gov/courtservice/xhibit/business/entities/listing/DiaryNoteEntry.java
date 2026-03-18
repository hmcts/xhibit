package uk.gov.courtservice.xhibit.business.entities.listing;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface DiaryNoteEntry extends CSEntityLocal {
    
	   public java.lang.Integer getDiaryNoteEntryId(  ) ;
	   public void setDiaryNoteEntryId( java.lang.Integer diaryNoteEntryId ) ;
	   
	   public java.lang.Integer getCaseListingEntryId(  ) ;
	   public void setCaseListingEntryId( java.lang.Integer caseListingEntryId ) ;
	
	   public java.lang.Integer getNoteTypeId(  ) ;
	   public void setNoteTypeId( java.lang.Integer noteTypeId ) ;
	   
	   public java.lang.Integer getNoteClassificationId(  ) ;
	   public void setNoteClassificationId( java.lang.Integer noteClassificationId ) ;
	   
	   public java.lang.String getDiaryNoteText(  ) ;
	   public void setDiaryNoteText( java.lang.String diaryNoteText ) ;
	   
	   public java.lang.Integer getDiaryNotePreDefinedId(  ) ;
	   public void setDiaryNotePreDefinedId( java.lang.Integer diaryNotePreDefinedId ) ;
	   
	   public java.util.Date getDiaryDate(  ) ;
	   public void setDiaryDate( java.util.Date diaryDate ) ;
	   
	   public java.lang.Integer getCourtId(  ) ;
	   public void setCourtId(java.lang.Integer courtId);
	   
	   public java.lang.String getLastUpdatedBy();
	   public void setLastUpdatedBy(java.lang.String lastUpdatedBy); 

	   public java.lang.Integer getCaseId(  ) ;
	   public void setCaseId(java.lang.Integer caseId);

	   public java.util.Date getLastUpdateDate();
	   public void setLastUpdateDate(java.util.Date lastUpdateDate);

	   public java.lang.String getObsInd();
	   public void setObsInd(java.lang.String obsInd);
	   
	   public java.util.Date getCreationDate();
	   public void setCreationDate(java.util.Date creationDate);
}
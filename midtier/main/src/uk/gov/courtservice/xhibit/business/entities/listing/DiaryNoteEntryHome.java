package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface DiaryNoteEntryHome extends javax.ejb.EJBLocalHome
{
	
	public DiaryNoteEntry create(Integer caseListingEntryId, Integer noteTypeId, 
			Integer noteClassificationId, String diaryNoteText, Integer diaryNotePreDefinedId, 
			Date diaryDate, Integer courtId, String userDisplayName, Integer caseId, String obsInd)
			throws CreateException;

	public DiaryNoteEntry findByPrimaryKey(Integer pk) 
			throws FinderException;

	public Collection findByCaseListingEntryIdAndNoteTypeId(Integer caseListingEntryId, Integer noteTypeId) 
			throws FinderException;
	
	public Collection findDiaryNotesByCourtIdAndDateAndNoteTypeId(Integer courtId, Date diaryDate, Integer noteTypeId)
			throws FinderException;
	
	public Collection findDiaryNotesByCourtIdAndDatesAndNoteTypeId(Integer courtId, Date diaryStartDate,
			Date diaryEndDate, Integer noteTypeId) throws FinderException;

	public Collection findByCaseListingEntryIdAndCaseId(Integer caseListingEntryId, Integer caseId)
			throws FinderException;
	
}

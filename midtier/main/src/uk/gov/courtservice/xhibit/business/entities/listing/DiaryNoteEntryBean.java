package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * Case Listing Entry bean.
 */
abstract public class DiaryNoteEntryBean extends CSEntityBean implements EntityBean {
	
	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer caseListingEntryId, Integer noteTypeId, 
			Integer noteClassificationId, String diaryNoteText, Integer diaryNotePreDefinedId, 
			Date diaryDate,Integer courtId, String userDisplayName, Integer caseId, String obsInd) throws CreateException {
		setCaseListingEntryId(caseListingEntryId);
		setNoteTypeId(noteTypeId);
		setNoteClassificationId(noteClassificationId);
		setDiaryNoteText(diaryNoteText);
		setDiaryNotePreDefinedId(diaryNotePreDefinedId);
		setDiaryDate(diaryDate);
		setCourtId(courtId);
		setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setCaseId(caseId);
        setObsInd(obsInd);
		return null;
	}
    
    public void ejbPostCreate(Integer caseListingEntryId, Integer noteTypeId, 
			Integer noteClassificationId, String diaryNoteText, Integer diaryNotePreDefinedId, 
			Date diaryDate, Integer courtId, String userDisplayName, Integer caseId, String obsInd) throws CreateException {
    }
	
    public abstract Integer getCaseListingEntryId();
    public abstract Integer getDiaryNoteEntryId();
    public abstract Integer getNoteTypeId();
    public abstract Integer getNoteClassificationId();
    public abstract String getDiaryNoteText();
    public abstract Integer getDiaryNotePreDefinedId();
    public abstract Date getDiaryDate();
    public abstract Integer getCourtId();
    public abstract String getLastUpdatedBy();
    public abstract Date getLastUpdateDate();
    public abstract Integer getCaseId();
    public abstract String getObsInd();
    public abstract Date getCreationDate();
    
    public abstract void setDiaryNoteEntryId(Integer diaryNoteEntryId);
    public abstract void setCaseListingEntryId(Integer caseListingEntryId);
    public abstract void setNoteTypeId(Integer noteTypeId);
    public abstract void setNoteClassificationId(Integer noteClassificationId);
    public abstract void setDiaryNoteText(String diaryNoteText);
    public abstract void setDiaryNotePreDefinedId(Integer diaryNotePreDefinedId);
    public abstract void setDiaryDate(Date diaryDate);
    public abstract void setCourtId(Integer courtId);
    public abstract void setCaseId(Integer caseId);
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract void setObsInd(String obsInd);
	public abstract void setCreationDate(Date creationDate);
}
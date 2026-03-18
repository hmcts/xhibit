package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntry;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.DiaryNoteEntry;
import uk.gov.courtservice.xhibit.business.entities.listing.DiaryNoteEntryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingData;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;

/**
 * <p>
 * Title: DiaryNoteHelper
 * </p>
 * <p>
 * Description: Provides and abstract layer between the session facade and the
 * maintainer class. It is used to construct the necessary value objects and
 * contains any business logic.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * @author westalll
 *
 */
public class DiaryNoteEntryHelper {
	
	private final CaseListingEntryMaintainer caseListingEntryMaintainer = new CaseListingEntryMaintainer();
	private final DiaryNoteEntryMaintainer diaryNoteEntryMaintainer = new DiaryNoteEntryMaintainer();
	private final RefListingDataMaintainer refListingDataMaintainer = new RefListingDataMaintainer();
	
	private static final Logger LOG = CSServices.getLogger(DiaryNoteEntryHelper.class);

	/**
	 * Description: Find Collection of General Diary Notes by courtId and date
	 * 
	 * @param courtId
	 * @param diaryDate
	 * @return Collection
	 * @throws FinderException 
	 */
	@SuppressWarnings("unchecked")
	public Collection<DiaryNoteEntryComplexValue> findGeneralDiaryNotesByCourtIdAndDate(Integer courtId, Date diaryDate) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: findGeneralDiaryNotesByCourtIdAndDate(courtId="+courtId+", diaryDate="+diaryDate+")");
       	}
		try {
			RefListingDataBasicValue generalDiaryNoteType = getNoteTypeByDataValue(
					RefListingDataBasicValue.DataValue.GENERAL_DIARY_NOTE);
			Collection<DiaryNoteEntry> locals = diaryNoteEntryMaintainer.findDiaryNotesByCourtIdAndDateAndNoteTypeId(courtId, diaryDate,
					generalDiaryNoteType.getRefListingDataId());
			Collection<DiaryNoteEntryComplexValue> values = diaryNoteEntryMaintainer.getComplexValues(locals);
			
			return populateComplexValues(values);
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
		}
	}
	
	/**
	 * Description: Find Collection of General Diary Notes by courtId and date. (Between start and End Date).
	 * 
	 * @param courtId
	 * @param diaryStartDate
	 * @param diaryEndDate
	 * @return Collection
	 * @throws FinderException 
	 */
	@SuppressWarnings("unchecked")
	public Collection<DiaryNoteEntryComplexValue> findGeneralDiaryNotesByCourtIdAndDates(final Integer courtId, final Date diaryStartDate, final Date diaryEndDate) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: findGeneralDiaryNotesByCourtIdAndDates(courtId="+courtId+", diaryStartDate="+diaryStartDate+", diaryEndDate="+diaryEndDate+")");
       	}
		try {
			RefListingDataBasicValue generalDiaryNoteType = getNoteTypeByDataValue(
					RefListingDataBasicValue.DataValue.GENERAL_DIARY_NOTE);
			Collection<DiaryNoteEntry> locals = diaryNoteEntryMaintainer.findDiaryNotesByCourtIdAndDatesAndNoteTypeId(courtId, diaryStartDate, diaryEndDate,
					generalDiaryNoteType.getRefListingDataId());
			Collection<DiaryNoteEntryComplexValue> values = diaryNoteEntryMaintainer.getComplexValues(locals);
			
			return populateComplexValues(values);
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
		}
	}
	
	/**
	 * Description: Find Collection of Case Notes by case Id 
	 * 
	 * @param caseId
	 * @return Collection
	 * @throws FinderException 
	 */
	@SuppressWarnings("unchecked")
	public Collection findCaseNotesByCaseId(Integer caseId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: findCaseNotesByCaseId(caseId="+caseId+")");
       	}
		try {
			Integer caseListingEntryId; 
			try {
				CaseListingEntry caseListingEntry = caseListingEntryMaintainer.findByCaseId(caseId);
				caseListingEntryId = caseListingEntry != null ? caseListingEntry.getCaseListingEntryId() : null;
			} catch (ObjectNotFoundException ex) {
				// If no listing entry exists then do not throw an exception
				caseListingEntryId = null;
			}			
			Collection<DiaryNoteEntry> locals = diaryNoteEntryMaintainer.findByCaseListingEntryIdAndCaseId(caseListingEntryId, caseId);
			Collection<DiaryNoteEntryComplexValue> values = diaryNoteEntryMaintainer.getComplexValues(locals);
			
			return populateComplexValues(values);
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
			throw ex;
		}
	}

	/**
	 * Description: Save Diary Note
	 * 
	 * @param DiaryNoteEntryBasicValue basicValue
	 * @param String user name 
	 * @return Integer
	 * @throws OptimisticLockException, CreateException, FinderException
	 */
    public Integer saveDiaryNoteEntry(DiaryNoteEntryBasicValue basicValue, String userDisplayName) throws OptimisticLockException, CreateException, FinderException {
    	if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: saveDiaryNoteEntry(basicValue="+basicValue+", userDisplayName="+userDisplayName+")");
       	}
    	try {
        	if (basicValue.getDiaryNoteEntryId() == null) {        		
	    		DiaryNoteEntry localCreated = (DiaryNoteEntry) diaryNoteEntryMaintainer.create(basicValue, userDisplayName);
	    		basicValue.setDiaryNoteEntryId(localCreated.getDiaryNoteEntryId());
	    	} else {
	    		diaryNoteEntryMaintainer.update(basicValue, userDisplayName);
	    	}
	    	return basicValue.getDiaryNoteEntryId();
        } catch (OptimisticLockException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        } catch (FinderException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw ex;
	    } catch (EJBException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw ex;
	    }    	
    }

    /**
	 * Description: Delete Diary Note
	 * 
	 * @param DiaryNoteEntryBasicValue basicValue
	 * @param String user name
	 * @throws OptimisticLockException, FinderException
	 */
    public void deleteDiaryNoteEntry(DiaryNoteEntryBasicValue basicValue, String userDisplayName) throws OptimisticLockException, FinderException {
    	if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: deleteDiaryNoteEntry(basicValue="+basicValue+", userDisplayName="+userDisplayName+")");
       	}
    	try {
        	// Logical deletion
    		diaryNoteEntryMaintainer.delete(basicValue, userDisplayName);
        } catch (OptimisticLockException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        } catch (FinderException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw ex;
        }
    }
    
    public boolean isTransferredOutCase(Integer caseId) throws ListingsControllerException {
    	if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: isTransferredOutCase(caseId="+caseId+")");
       	}
    	boolean result = false;
    	if (caseId != null) {
			try {
				Case caseEntity = getCase(caseId);
				result = (caseEntity.getDateTransTo() != null);
			} catch (ObjectNotFoundException e) {
				// Do nothing, leave the result as false
			}
    	}
    	return result;
    }

	private Collection<DiaryNoteEntryComplexValue> populateComplexValues(Collection<DiaryNoteEntryComplexValue> values) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: populateComplexValues(values="+values+")");
       	}
		Collection<DiaryNoteEntryComplexValue> results = new ArrayList<DiaryNoteEntryComplexValue>();
		for (DiaryNoteEntryComplexValue value : values) {
			results.add(populateComplexValue(value));
		}
		return results;
	}
	
	private DiaryNoteEntryComplexValue populateComplexValue(DiaryNoteEntryComplexValue value) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: populateComplexValue(value="+value+")");
       	}
		DiaryNoteEntryComplexValue complexValue = value;
		if (value.getCaseId() != null) {
			Case caseEntity = getCase(value.getCaseId());
			complexValue.setCaseType(caseEntity.getCaseType());
			complexValue.setCaseNumber(caseEntity.getCaseNumber());
			complexValue.setDateTransTo(caseEntity.getDateTransTo());
		}
		
		if (value.getNoteClassificationId() != null) {
			RefListingDataBasicValue noteClassification = getNoteTypeById(value.getNoteClassificationId());
			complexValue.setNoteClassification(noteClassification);
		}	
		
		if (value.getDiaryNotePreDefinedId() != null) {
			RefListingDataBasicValue preDefinedNote = getNoteTypeById(value.getDiaryNotePreDefinedId());
			complexValue.setPreDefinedNote(preDefinedNote);
		}
		
		if (value.getNoteTypeId() != null) {
			RefListingDataBasicValue noteType = getNoteTypeById(value.getNoteTypeId());
			complexValue.setNoteType(noteType);
		}	
		return complexValue;
	}
	
	private Case getCase(final Integer caseId) throws ObjectNotFoundException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: getCase(caseId="+caseId+")");
       	}
		final CaseMaintainer caseMaintainer = new CaseMaintainer();	
		return caseMaintainer.findByPrimaryKey(caseId);
	}
	
	private RefListingDataBasicValue getNoteTypeById(Integer id) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: getNoteTypeById(id="+id+")");
       	}
        try {     	
			RefListingData local = refListingDataMaintainer.findByPrimaryKey(id);    
			RefListingDataBasicValue result = refListingDataMaintainer.getBasicValue(local);
        	return result;
        } catch (ObjectNotFoundException ex) {
        	CSServices.getDefaultErrorHandler().handleError(ex, getClass());
        	throw ex;
        }   
    }
	
	private RefListingDataBasicValue getNoteTypeByDataValue(String dataValue) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: getNoteTypeByDataValue(dataValue="+dataValue+")");
       	}
        try {     	
			RefListingData local = refListingDataMaintainer.findNoteTypeByDataValue(dataValue);
			RefListingDataBasicValue result = local !=  null ? refListingDataMaintainer.getBasicValue(local) : null;
        	return result;
        } catch (ObjectNotFoundException ex) {
        	CSServices.getDefaultErrorHandler().handleError(ex, getClass());
        	throw ex;
        }   
    }
}

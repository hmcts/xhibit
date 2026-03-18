package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;
import java.sql.SQLException;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCase;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntry;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.DiaryNoteEntry;
import uk.gov.courtservice.xhibit.business.entities.listing.DiaryNoteEntryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudge;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudgeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingData;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingDataMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCodeMaintainer;
import uk.gov.courtservice.xhibit.business.services.createcase.GenerateCaseNumberControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseListingEntryValue;

/**
 * <p>
 * Title: CaseListingEntryHelper
 * </p>
 * <p>
 * Description: Provides and abstract layer between the session facade and the
 * maintainer class. It is used to construct the necessary value objects and
 * contains any business logic.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class CaseListingEntryHelper {
	private static final Logger LOG = CSServices.getLogger(CaseListingEntryHelper.class);

	private static final String ACTIVE = "A";
	private static final String CHARGE_IMPORT_INDICATOR_TYPE_O = "O";
	
	private CaseListingEntryMaintainer caseListingEntryMaintainer;
	private DiaryNoteEntryMaintainer diaryNoteEntryMaintainer;
	private RefJudgeMaintainer refJudgeMaintainer;
	private RefListingDataMaintainer refListingDataMaintainer;
	private CaseDiaryFixtureHelper caseDiaryFixtureHelper;
	private CaseMaintainer caseMaintainer;
	private DefendantHelper defendantHelper;
	private DefendantMaintainer defendantMaintainer;
	private DefendantOnCaseMaintainer defendantOnCaseMaintainer;
	private RefSystemCodeMaintainer refSystemCodeMaintainer;
	private DirectionsForCaseMaintainer directionsForCaseMaintainer; 
	
	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public CaseListingEntryHelper() {
		caseListingEntryMaintainer = new CaseListingEntryMaintainer();
		diaryNoteEntryMaintainer = new DiaryNoteEntryMaintainer();
		refJudgeMaintainer = new RefJudgeMaintainer();
		refListingDataMaintainer = new RefListingDataMaintainer();
		caseDiaryFixtureHelper = new CaseDiaryFixtureHelper();
		caseMaintainer = new CaseMaintainer();
		defendantHelper = new DefendantHelper();
		defendantMaintainer = new DefendantMaintainer();
		defendantOnCaseMaintainer = new DefendantOnCaseMaintainer();
		refSystemCodeMaintainer = new RefSystemCodeMaintainer();
		directionsForCaseMaintainer = new DirectionsForCaseMaintainer();
	}
	
	/**
     * Get the case for the given id.
     * 
     * @param caseId
     *            the id of the case to retrieve.
     * @return CaseBasicValue
     * @throws FinderException
     */
    public CaseBasicValue getCase(Integer caseId) throws FinderException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: getCase(caseId="+caseId+")");
       	}
        try {
            Case cs = caseMaintainer.findByPrimaryKey(caseId);
            CaseBasicValue caseBasic = caseMaintainer.getCaseBasicValue(cs);
            return caseBasic;
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        }
    }

    /**
     * Gets defendant value objects for a given case
     * 
     * @param caseID
     *            The case id
     * @return collection of defendant value objects
     * @throws CSBusinessException
     */
    @SuppressWarnings("unchecked")
	public Collection<DefendantValue> getDefendants(Integer caseID) throws CSBusinessException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: getDefendants(caseID="+caseID+")");
       	}
        ArrayList defendantValues = new ArrayList();

        // Find the defendants by caseId and populate them into collection -
        // defendantValues
        try {
            Collection defendantBeans = defendantOnCaseMaintainer.findByCaseId(caseID);
            Iterator defendantIterator = defendantBeans.iterator();
            while (defendantIterator.hasNext()) {
                DefendantOnCase defendantOnCase = (DefendantOnCase) defendantIterator.next();
                DefendantValue defendantValue = defendantHelper.getDefendantDetails(defendantOnCase.getDefendant()
                        .getDefendantId(), defendantOnCase.getCaseId());
                defendantValues.add(defendantValue);
            }

        } catch (CSBusinessException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw e;
        }

        return defendantValues;
    }
    
	/**
	 * Description: Find Case Listing Entry by caseId and courtId
	 * 
	 * @param caseId
	 * @param courtId
	 * @return CaseListingEntry
	 * @throws FinderException, CSBusinessException 
	 */
	@SuppressWarnings("unchecked")
	public CaseListingEntryComplexValue findCaseListingEntryByCaseIdAndCourtId(Integer caseId, Integer courtId) throws FinderException, CSBusinessException {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: findCaseListingEntryByCaseIdAndCourtId(caseId="+caseId+", courtId="+courtId+")");
       	}
		CaseListingEntryComplexValue result = null;
		try {
			try {
				CaseListingEntry cle = caseListingEntryMaintainer.findByCaseId(caseId);	
				 result = caseListingEntryMaintainer.getComplexValue(cle);
			} catch (ObjectNotFoundException ex) {
				// If no case listing entry exists then return a new entry
				result = new CaseListingEntryComplexValue();
				result.setCaseId(caseId);
				result.setCourtId(courtId);
			}

        	// Get the Case
        	CaseBasicValue caseBasicValue = getCase(caseId);
    		result.setCaseBasicValue(caseBasicValue);
			
    		// Get the Judge Type
    		RefSystemCodeBasicValue refJudgeType = getRefSystemCode(result.getRefJudgeTypeId());
    		result.setRefJudgeType(refJudgeType);
    		
    		// Get the Judge 
    		RefJudgeBasicValue refJudge = getRefJudge(result.getJudgeId());
    		result.setRefJudge(refJudge);
    		
    		// Get the ticket type
    		RefSystemCodeBasicValue ticketType = getRefSystemCode(caseBasicValue.getTicketTypeCode());
    		result.setTicketType(ticketType);
    		    		
    		// Get the defendants
    		Collection defendants = getDefendants(caseId);
			result.setDefendants(defendants);				
			
			// Get the directions
			DirectionsForCaseBasicValue directionsForCase = getDirectionsForCaseByCaseId(caseId);
			result.setDirectionsForCase(directionsForCase);
			
			// Get the Highlight Note Type
			RefListingDataBasicValue highlightNoteType = getNoteTypeByDataValue(RefListingDataBasicValue.DataValue.HIGHLIGHT_NOTE);
			result.setHighlightNoteType(highlightNoteType);
			
			// Get the Default Case Note Type
			RefListingDataBasicValue defaultCaseNoteType = getNoteTypeByDataValue(RefListingDataBasicValue.DataValue.DEFAULT_CASE_NOTE);
			result.setDefaultCaseNoteType(defaultCaseNoteType);
			
			// Get the Interpreter Note Type
			RefListingDataBasicValue interpreterNoteType = getNoteTypeByDataValue(RefListingDataBasicValue.DataValue.INTERPRETER_NOTE);
			result.setInterpreterNoteType(interpreterNoteType);
			
			// Get the Highlight Diary Note Entry
			DiaryNoteEntryBasicValue highlightDiaryNoteEntry = getDiaryNoteEntryByCaseListingIdAndNoteTypeId(result.getCaseListingEntryId(), 
					highlightNoteType.getRefListingDataId());
			result.setHighlightDiaryNoteEntry(highlightDiaryNoteEntry);
		
			// Get the Default Case Diary Note Entries
			Collection defaultCaseDiaryNoteEntries = getDiaryNoteEntriesByCaseListingIdAndNoteTypeId(result.getCaseListingEntryId(), 
					defaultCaseNoteType.getRefListingDataId());
    		for (DiaryNoteEntryBasicValue dcnBasicValue : (Collection<DiaryNoteEntryBasicValue>) defaultCaseDiaryNoteEntries) {
    			if (dcnBasicValue.getDiaryNotePreDefinedId() != null) {
    				result.setPreDefinedDiaryNoteEntry(dcnBasicValue);	
    			} else {
            		result.setFreeTextDiaryNoteEntry(dcnBasicValue);	
    			}    		
    		}    		
		
			// Get the Interpreter Diary Note Entry
			DiaryNoteEntryBasicValue interpreterDiaryNoteEntry = getDiaryNoteEntryByCaseListingIdAndNoteTypeId(result.getCaseListingEntryId(), 
					interpreterNoteType.getRefListingDataId());	
    		result.setInterpreterDiaryNoteEntry(interpreterDiaryNoteEntry);
    		
    		// If its an existing entry
			if (result.getCaseListingEntryId() != null) {
	    		// Get the fixtures
	    		Collection fixtures = getCaseDiaryFixturesByCaseListingId(result.getCaseListingEntryId());
	    		result.setFixtures(fixtures);
			}
		}  catch (CSBusinessException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
    		throw ex;
		} catch (FinderException ex) {
    		CSServices.getDefaultErrorHandler().handleError(ex, getClass());
    		throw ex;
    	}

		return result;
	}
	
	    
	/**
	 * Description: Save Case Listing Entry 
	 * 
	 * @param caseListingEntryValue
	 * @throws FinderException, CreateException, OptimisticLockException 
	 */
	public void saveCaseListingEntry(CaseListingEntryValue caseListingEntryValue, String userDisplayName) throws OptimisticLockException, CreateException, FinderException {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: saveCaseListingEntry(caseListingEntryValue="+caseListingEntryValue+", userDisplayName="+userDisplayName+")");
       	}
        
    	// Update the case listing entry
        CaseListingEntryBasicValue caseListingEntryBasicValue = caseListingEntryValue.getCaseListingEntryBasicValue();
    	saveCaseListingEntryBasicValue(caseListingEntryBasicValue, userDisplayName);
        
        // Update case
        CaseBasicValue caseBasicValue = caseListingEntryValue.getCaseBasicValue();
        saveCaseBasicValue(caseBasicValue, userDisplayName);
    	
        // Update defendants on case
        if (caseListingEntryValue.getDefendants() != null && !caseListingEntryValue.getDefendants().isEmpty()) {
        	for (DefendantValue defendantValue : caseListingEntryValue.getDefendants()) {
        		saveDefendantValue(defendantValue, userDisplayName);
        		saveDefendantOnCaseBasicValue((DefendantOnCaseBasicValue) defendantValue.getDefOnCaseBasicValue(), userDisplayName);	
        	}
        }
        
        // Update directions for case 
        DirectionsForCaseBasicValue directionsForCase = caseListingEntryValue.getDirectionsForCase();
        saveDirectionsForCase(directionsForCase, userDisplayName);
        
        // Update the HN diary note
        DiaryNoteEntryBasicValue highlightDiaryNoteEntry = caseListingEntryValue.getHighlightDiaryNoteEntry();
        if (highlightDiaryNoteEntry.getCaseListingEntryId() == null) {
        	highlightDiaryNoteEntry.setCaseListingEntryId(caseListingEntryBasicValue.getCaseListingEntryId());
        }  
        saveDiaryNoteEntry(highlightDiaryNoteEntry, userDisplayName);
        
        // Update the IN diary note
        DiaryNoteEntryBasicValue interpreterDiaryNoteEntry = caseListingEntryValue.getInterpreterDiaryNoteEntry();
        if (interpreterDiaryNoteEntry.getCaseListingEntryId() == null) {
        	interpreterDiaryNoteEntry.setCaseListingEntryId(caseListingEntryBasicValue.getCaseListingEntryId());
        }  
        saveDiaryNoteEntry(interpreterDiaryNoteEntry, userDisplayName);
        
        // Update the DCN diary note (predefined)
        DiaryNoteEntryBasicValue preDefinedDiaryNoteEntry = caseListingEntryValue.getPreDefinedDiaryNoteEntry();
        if (preDefinedDiaryNoteEntry.getCaseListingEntryId() == null) {
        	preDefinedDiaryNoteEntry.setCaseListingEntryId(caseListingEntryBasicValue.getCaseListingEntryId());
        }        
        saveDiaryNoteEntry(preDefinedDiaryNoteEntry, userDisplayName);	
       
        // Update the DCN diary note (free-text)
        DiaryNoteEntryBasicValue freeTextDiaryNoteEntry = caseListingEntryValue.getFreeTextDiaryNoteEntry();
        if (freeTextDiaryNoteEntry.getCaseListingEntryId() == null) {
        	freeTextDiaryNoteEntry.setCaseListingEntryId(caseListingEntryBasicValue.getCaseListingEntryId());
        }        
        saveDiaryNoteEntry(freeTextDiaryNoteEntry, userDisplayName);
	}

	@SuppressWarnings("unchecked")
	private DiaryNoteEntryBasicValue getDiaryNoteEntryByCaseListingIdAndNoteTypeId(Integer caseListingEntryId, Integer noteTypeId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: getDiaryNoteEntryByCaseListingIdAndNoteTypeId(caseListingEntryId="+caseListingEntryId+", noteTypeId="+noteTypeId+")");
       	}
        DiaryNoteEntryBasicValue result = null;
        Collection basicValues = getDiaryNoteEntriesByCaseListingIdAndNoteTypeId(caseListingEntryId, noteTypeId);
        if (!basicValues.isEmpty()) {
        	result = ((ArrayList<DiaryNoteEntryBasicValue>) basicValues).get(0);
        }
        return result;
	}
	
	@SuppressWarnings("unchecked")
	private Collection getDiaryNoteEntriesByCaseListingIdAndNoteTypeId(Integer caseListingEntryId, Integer noteTypeId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: getDiaryNoteEntriesByCaseListingIdAndNoteTypeId(caseListingEntryId="+caseListingEntryId+", noteTypeId="+noteTypeId+")");
       	}
    	try {
    		Collection result = new ArrayList<DiaryNoteEntryBasicValue>();       	 
            Collection locals = diaryNoteEntryMaintainer.findByCaseListingEntryIdAndNoteTypeId(caseListingEntryId, noteTypeId);
            result.addAll(diaryNoteEntryMaintainer.getBasicValues(locals));	
			return result;
    	} catch (ObjectNotFoundException ex) {
    		CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
    	}       	     	
    }
    
    private Collection getCaseDiaryFixturesByCaseListingId(Integer caseListingEntryId) throws FinderException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: getCaseDiaryFixturesByCaseListingId(caseListingEntryId="+caseListingEntryId+")");
       	}
        Collection results = caseDiaryFixtureHelper.getPendingCaseDiaryFixturesByCaseListingIdAndStatus(caseListingEntryId, ACTIVE);
        return results;
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
	
	/**
	 * Get the Judge by Id.
	 * @param refJudgeId
	 * @return RefJudgeBasicValue for the selected Judge.
	 * @throws FinderException
	 */
	public RefJudgeBasicValue getRefJudge(Integer refJudgeId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: getRefJudge(refJudgeId="+refJudgeId+")");
       	}
        RefJudgeBasicValue result = null;
        if (refJudgeId != null) {
        	try {
            	RefJudge rj = refJudgeMaintainer.findByPrimaryKey(refJudgeId);
            	result = refJudgeMaintainer.getBasicValue(rj);
    	    } catch (ObjectNotFoundException ex) {
    	    	CSServices.getDefaultErrorHandler().handleError(ex, getClass());
    	    	throw ex;
    	    }
        }   
    	return result;
	}

	private DirectionsForCaseBasicValue getDirectionsForCaseByCaseId(Integer caseId) {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: getDirectionsForCaseByCaseId(caseId="+caseId+")");
       	}
        DirectionsForCaseBasicValue result;
        
       	try {
       		DirectionsForCase local = directionsForCaseMaintainer.findByCaseId(caseId);
       		result = directionsForCaseMaintainer.getBasicValue(local);
   	    } catch (ObjectNotFoundException ex) {
   	    	result = new DirectionsForCaseBasicValue();
   	    	result.setCaseId(caseId);
        }   
       	return result;
	}

	private RefSystemCodeBasicValue getRefSystemCode(Integer id) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: getRefSystemCode(id="+id+")");
       	}
        RefSystemCodeBasicValue result = null;
        if (id != null) {
        	try {
            	RefSystemCode rsc = refSystemCodeMaintainer.findByPrimaryKey(id);
            	result = refSystemCodeMaintainer.getBasicValue(rsc);
    	    } catch (ObjectNotFoundException ex) {
    	    	CSServices.getDefaultErrorHandler().handleError(ex, getClass());
    	    	throw ex;
    	    }
        }   
    	return result;
	}
	   
    private void saveCaseBasicValue(CaseBasicValue basicValue, String userDisplayName) throws OptimisticLockException, FinderException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: saveCaseBasicValue(basicValue="+basicValue+", userDisplayName="+userDisplayName+")");
       	}
        try {
            caseMaintainer.updateListing(basicValue, userDisplayName);
        } catch (OptimisticLockException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        }
    }
    
	private Integer getCaseListingEntryId(Integer caseId) {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: getCaseListingEntryId(caseId="+caseId+")");
       	}
		Integer caseListingEntryId;
		try {
			CaseListingEntry local = caseListingEntryMaintainer.findByCaseId(caseId);
			caseListingEntryId = local.getCaseListingEntryId();
		} catch (ObjectNotFoundException e) {
			caseListingEntryId = null;
		}
		return caseListingEntryId;
	}

    private void saveCaseListingEntryBasicValue(CaseListingEntryBasicValue basicValue, String userDisplayName) throws OptimisticLockException, CreateException, FinderException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: saveCaseListingEntryBasicValue(basicValue="+basicValue+", userDisplayName="+userDisplayName+")");
       	}
        try {
			// Get the latest id (if one was created before this save)
        	if (basicValue.getCaseListingEntryId() == null ) {
        		Integer caseListingEntryId = getCaseListingEntryId(basicValue.getCaseId());
        		basicValue.setCaseListingEntryId(caseListingEntryId);
        		basicValue.setId(caseListingEntryId);
        	}
        	// Create / Update
        	if (basicValue.getCaseListingEntryId() == null ) {
        		CaseListingEntry localCreated = (CaseListingEntry) caseListingEntryMaintainer.create(basicValue, userDisplayName);
        		basicValue.setCaseListingEntryId(localCreated.getCaseListingEntryId());                	
        	} else {
        		caseListingEntryMaintainer.update(basicValue, userDisplayName);
        	}
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
    
    private void saveDefendantOnCaseBasicValue(DefendantOnCaseBasicValue basicValue, String userDisplayName) throws OptimisticLockException, FinderException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: saveDefendantOnCaseBasicValue(basicValue="+basicValue+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	defendantOnCaseMaintainer.updateListing(basicValue, userDisplayName);
        } catch (OptimisticLockException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        } catch (FinderException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw ex;
        } 
    }
    
    private void saveDefendantValue(DefendantValue value, String userDisplayName) throws OptimisticLockException, FinderException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: saveDefendantValue(value="+value+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	defendantMaintainer.updateListing(value, userDisplayName);
        } catch (OptimisticLockException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        } catch (FinderException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw ex;            
        } 
    }
    
	private Integer getDirectionsForCaseId(Integer caseId) {
		if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: getDirectionsForCaseId(caseId="+caseId+")");
       	}
		Integer directionsForCaseId;
		try {
			DirectionsForCase local = directionsForCaseMaintainer.findByCaseId(caseId);
			directionsForCaseId = local.getDirectionsForCaseId();
		} catch (ObjectNotFoundException e) {
			directionsForCaseId = null;
		}
		return directionsForCaseId;
	}

    private void saveDirectionsForCase(DirectionsForCaseBasicValue basicValue, String userDisplayName) throws OptimisticLockException, CreateException, FinderException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: saveDirectionsForCase(basicValue="+basicValue+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	// Get the latest id (if one was created before this save)
        	if (basicValue.getDirectionsForCaseId() == null) {
        		Integer directionsForCaseId = getDirectionsForCaseId(basicValue.getCaseId());
        		basicValue.setDirectionsForCaseId(directionsForCaseId);
        		basicValue.setId(directionsForCaseId);
        	}
        	// Create or Update the record
        	if (basicValue.getDirectionsForCaseId() == null) {
        		DirectionsForCase localCreated = (DirectionsForCase) directionsForCaseMaintainer.create(basicValue, userDisplayName);
        		basicValue.setDirectionsForCaseId(localCreated.getDirectionsForCaseId());
        	} else {
        		directionsForCaseMaintainer.update(basicValue, userDisplayName);
        	}
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
    
    private void saveDiaryNoteEntry(DiaryNoteEntryBasicValue basicValue, String userDisplayName) throws OptimisticLockException, CreateException, FinderException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: saveDiaryNoteEntry(basicValue="+basicValue+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	if (basicValue.hasNote()) {
	        	if (basicValue.getDiaryNoteEntryId() == null) {        		
	        		DiaryNoteEntry localCreated = (DiaryNoteEntry) diaryNoteEntryMaintainer.create(basicValue, userDisplayName);
	        		basicValue.setDiaryNoteEntryId(localCreated.getDiaryNoteEntryId());
	        	} else {
	        		diaryNoteEntryMaintainer.update(basicValue, userDisplayName);
	        	}
        	} else {
        		if (basicValue.getDiaryNoteEntryId() != null) {
        			diaryNoteEntryMaintainer.delete(basicValue.getDiaryNoteEntryId(), basicValue.getVersion());
        		}
        	}
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
     * @param AddCaseValue
     * @return AddCaseValue
     * @throws CreateException
     */
    public CaseBasicValue createNewUorBCase(final AddCaseValue addCaseValue, String userDisplayName) throws CreateException {
    	if ( LOG.isDebugEnabled() ) {
    		LOG.debug("START: createNewUorBCase(addCaseValue="+addCaseValue+", userDisplayName="+userDisplayName+")");
       	}
        try {
            CaseBasicValue cs = new CaseBasicValue();
            cs.setCaseTitle(addCaseValue.getCaseTitle());
            cs.setCaseType(addCaseValue.getCaseType());
            cs.setCourtID(addCaseValue.getCourtID());
            cs.setChargeImportIndicator(CHARGE_IMPORT_INDICATOR_TYPE_O);
            cs.setDefaultHearingType(addCaseValue.getHearingTypeId());
            
        	final String caseNumber = GenerateCaseNumberControllerBeanBusinessDelegate.DelegateFactory.getInstance()
					.generateCaseNumber(cs.getCourtID(), cs.getCaseType());
        	
        	if ( LOG.isDebugEnabled() ) {
        		LOG.debug("Generated case number "+ caseNumber);
        	}

        	cs.setCaseNumber(Integer.parseInt(caseNumber.substring(1)));
    	
        	final Case caze = caseMaintainer.createCase(cs, userDisplayName);
        	final CaseBasicValue caseBasic = caseMaintainer.getCaseBasicValue(caze);
    	
            return caseBasic;
        } catch(SQLException e){
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        }
        catch (final EJBException e) {
        	CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw e;
        }
    }
}
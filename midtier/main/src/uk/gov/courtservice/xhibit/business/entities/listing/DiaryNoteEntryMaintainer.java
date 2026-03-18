package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;

/**
 * Maintainer for diary note entry entity.
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version $Revision: 1.0 $
 */
public class DiaryNoteEntryMaintainer extends AbstractEntityMaintainer {
    private DiaryNoteEntryHome home = null;

    private static final String YES = "Y";
    private static final String NO = "N";
    
    /**
     * Default constructor.
     */
    public DiaryNoteEntryMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public DiaryNoteEntry findByPrimaryKey(Integer key) throws ObjectNotFoundException {
        try {
            log.debug("Entered: findByPrimaryKey");
            return this.getHome().findByPrimaryKey(key);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }

    /**
     * Find the entity using the case id.
     * 
     * @param Integer
     *            Case entry listing id to use when performing the search
     * @param Integer
     *            Note Type id to use when performing the search
     * @return The local interface of the returned collection
     * @throws ObjectNotFoundException
     */
    public Collection findByCaseListingEntryIdAndNoteTypeId(Integer caseListingEntryId, Integer noteTypeId) throws ObjectNotFoundException {
        try {
			log.debug("Entered: findByCaseListingEntryIdAndNoteTypeId");
            return this.getHome().findByCaseListingEntryIdAndNoteTypeId(caseListingEntryId, noteTypeId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }
    
    
    /**
     * Find the entity using the court, date and note type id.
     * 
     * @param Integer
     *            Id of the court
     * @param Date
     *            Date of the diary note
     * @param Integer
     *            Note Type id to use when performing the search
     * @return The local interface of the returned collection
     * @throws ObjectNotFoundException
     */
    public Collection findDiaryNotesByCourtIdAndDateAndNoteTypeId(Integer courtId, Date diaryDate, Integer noteTypeId) throws ObjectNotFoundException {
        try {
			log.debug("Entered: findDiaryNotesByCourtIdAndDateAndNoteTypeId");
            return this.getHome().findDiaryNotesByCourtIdAndDateAndNoteTypeId(courtId, diaryDate, noteTypeId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }
    
    /**
     * Find the entity using the court, date and note type id.
     * 
     * @param Integer
     *            Id of the court
     * @param Date
     *            Start Date of the diary note
     * @param Date
     *            End Date of the diary note
     * @param Integer
     *            Note Type id to use when performing the search
     * @return The local interface of the returned collection
     * @throws ObjectNotFoundException
     */
    public Collection findDiaryNotesByCourtIdAndDatesAndNoteTypeId(Integer courtId, Date diaryStartDate, Date diaryEndDate, Integer noteTypeId) throws ObjectNotFoundException {
        try {
			log.debug("Entered: findDiaryNotesByCourtIdAndDatesAndNoteTypeId");
            return this.getHome().findDiaryNotesByCourtIdAndDatesAndNoteTypeId(courtId, diaryStartDate, diaryEndDate, noteTypeId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }
    

    /**
     * Find the entity using the case id.
     * 
     * @param Integer
     *            Id of the case listing entry
     * @param Integer
     *            Id of the case
     * @return The local interface of the returned collection
     * @throws ObjectNotFoundException
     */
    public Collection findByCaseListingEntryIdAndCaseId(Integer caseListingEntryId, Integer caseId) throws ObjectNotFoundException {
        try {
			log.debug("Entered: findByCaseListingEntryIdAndCaseId");
            return this.getHome().findByCaseListingEntryIdAndCaseId(caseListingEntryId, caseId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }
    
    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            DiaryNoteEntry
     * @return DiaryNoteEntryBasicValue
     */
    public DiaryNoteEntryBasicValue getBasicValue(DiaryNoteEntry local) {
        log.debug("Entered: getBasicValue");
        DiaryNoteEntryBasicValue value = new DiaryNoteEntryBasicValue((Integer) local.getPrimaryKey(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            DiaryNoteEntry
     * @return DiaryNoteEntryComplexValue
     */
    public DiaryNoteEntryComplexValue getComplexValue(DiaryNoteEntry local) {
        log.debug("Entered: getComplexValue");
        DiaryNoteEntryComplexValue value = new DiaryNoteEntryComplexValue((Integer) local.getPrimaryKey(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }
    
    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            DiaryNoteEntry
     * @return Collection of DiaryNoteEntryBasicValue
     */
    public Collection getBasicValues(Collection locals) {
        log.debug("Entered: getBasicValues");
        return getCollectionValues(false, locals);
    }
    
    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            DiaryNoteEntry
     * @return Collection of DiaryNoteEntryComplexValue
     */
    public Collection getComplexValues(Collection locals) {
        log.debug("Entered: getComplexValues");
        return getCollectionValues(true, locals);
    }
    
	@SuppressWarnings("unchecked")
	private <T> Collection<T> getCollectionValues(boolean complexValue, Collection locals) {
        log.debug("Entered: getCollectionValues");
        if (locals == null)
            return null;

        Collection<T> values = new ArrayList<T>();
        Iterator it = locals.iterator();
        T rowValue = null;
        DiaryNoteEntry element = null;
        while (it.hasNext()) {
        	element = (DiaryNoteEntry) it.next();
        	if (complexValue) {
        		rowValue = (T) getComplexValue(element);
        	} else {
        		rowValue = (T) getBasicValue(element);	
        	}        	
        	values.add(rowValue);
        }
        return values;
    }
    
    /**
     * The DiaryNoteEntry Home.
     * 
     * @return DiaryNoteEntryHome
     */
    public DiaryNoteEntryHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise DiaryNoteEntryHome");
            this.home = (DiaryNoteEntryHome) CSServices.getServiceLocator().getLocalHome(DiaryNoteEntryHome.class);
        }
        return this.home;
    }

    protected void loadValue(DiaryNoteEntryBasicValue value, DiaryNoteEntry local) {
    	value.setDiaryNoteEntryId(local.getDiaryNoteEntryId());
    	value.setCaseListingEntryId(local.getCaseListingEntryId());
    	value.setNoteTypeId(local.getNoteTypeId());
    	value.setNoteClassificationId(local.getNoteClassificationId());
    	value.setDiaryNoteText(local.getDiaryNoteText());
    	value.setDiaryNotePreDefinedId(local.getDiaryNotePreDefinedId());
    	value.setDiaryDate(local.getDiaryDate());
    	value.setCourtId(local.getCourtId());
		value.setLastUpdateDate(local.getLastUpdateDate());
    	value.setLastUpdatedBy(local.getLastUpdatedBy());
    	value.setCaseId(local.getCaseId());
        value.setCreationDate(local.getCreationDate());
    }
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        if (!(value instanceof DiaryNoteEntryBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
        try {
        	DiaryNoteEntryBasicValue basicValue = (DiaryNoteEntryBasicValue) value;
        	DiaryNoteEntry result = getHome().create(
					basicValue.getCaseListingEntryId(), basicValue.getNoteTypeId(), 
					basicValue.getNoteClassificationId(), basicValue.getDiaryNoteText(), basicValue.getDiaryNotePreDefinedId(), 
					basicValue.getDiaryDate(), basicValue.getCourtId(),
					userDisplayName, basicValue.getCaseId(), NO);
			return result;
		} catch (CreateException e) {
		       CSServices.getDefaultErrorHandler().handleError(e, getClass());
	            throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		
        if (!(value instanceof DiaryNoteEntryBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
        try {
        	// Get the current db values
        	DiaryNoteEntry dne = getHome().findByPrimaryKey(value.getId());
	        
	        // Check the version
	        if (dne.getVersion() == null || value.getVersion() == null || !dne.getVersion().equals(value.getVersion())) {
	            throw new OptimisticLockException("Optimistic Lock Error");
	        }
	        DiaryNoteEntryBasicValue basicValue = (DiaryNoteEntryBasicValue) value;
	        
	        // Update the record
	        dne.setCourtId(basicValue.getCourtId());
	        dne.setDiaryDate(basicValue.getDiaryDate());
	        dne.setCaseListingEntryId(basicValue.getCaseListingEntryId());
	        dne.setNoteTypeId(basicValue.getNoteTypeId());
	        dne.setNoteClassificationId(basicValue.getNoteClassificationId());
	        dne.setDiaryNoteText(basicValue.getDiaryNoteText());
	        dne.setDiaryNotePreDefinedId(basicValue.getDiaryNotePreDefinedId());	        
	        dne.setUpdated(userDisplayName);
	        dne.setCaseId(basicValue.getCaseId());
	        
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw new EJBException(ex);
        }
	}

	/*
	 * Physical deletion (as used from caseListingEntry)
	 */
	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
        try {
        	DiaryNoteEntry local = getHome().findByPrimaryKey(id);

            if (!local.getVersion().equals(version)) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.remove();                
            }
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        } catch (RemoveException r) {
            CSServices.getDefaultErrorHandler().handleError(r, getClass(), r.toString());
            throw new EJBException(r);
        } 		
	}
		
	/*
	 * Logical deletion (used in case notes, list officers diary, etc)
	 */	
	public void delete(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		
        if (!(value instanceof DiaryNoteEntryBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
        try {
        	// Get the current db values
        	DiaryNoteEntry dne = getHome().findByPrimaryKey(value.getId());
	        
	        // Check the version
	        if (dne.getVersion() == null || value.getVersion() == null || !dne.getVersion().equals(value.getVersion())) {
	            throw new OptimisticLockException("Optimistic Lock Error");
	        }

	        // Update the record
	        dne.setObsInd(YES);
	        dne.setUpdated(userDisplayName);	        
	        
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw new EJBException(ex);
        }
	}
}
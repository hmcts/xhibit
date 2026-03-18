package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;

/**
 * Maintainer for case diary fixture entity.
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
public class CaseDiaryFixtureMaintainer extends AbstractEntityMaintainer {
    private CaseDiaryFixtureHome home = null;

    /**
     * Default constructor.
     */
    public CaseDiaryFixtureMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public CaseDiaryFixture findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     * Find the entity using the case listing entry id and status.
     * 
     * @param Integer
     *            Case listing entry id to use when performing the search
     * @param String 
     *            Status of the fixture
     * @return The local interface of the returned collection
     * @throws ObjectNotFoundException
     */
    public Collection findByCaseListingEntryIdAndStatus(Integer caseListingEntryId, String status) throws ObjectNotFoundException {
        try {
			log.debug("Entered: findByCaseListingEntryIdAndStatus");
            return this.getHome().findByCaseListingEntryIdAndStatus(caseListingEntryId, status);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw e;
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }
  
    /**
     * Find the entity using the case listing entry id and status.
     * 
     * @param Integer
     *            Case listing entry id to use when performing the search
     * @param String 
     *            Status of the fixture
     * @param Date
     *            From date 
     * @return The local interface of the returned collection
     * @throws ObjectNotFoundException
     */
    public Collection findByCaseListingEntryIdAndStatusAndListingDate(Integer caseListingEntryId, String status, Date fromDate) throws ObjectNotFoundException {
        try {
			log.debug("Entered: findByCaseListingEntryIdAndStatusAndListingDate");
            return this.getHome().findByCaseListingEntryIdAndStatusAndListingDate(caseListingEntryId, status, fromDate);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw e;
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }
    
    /**
	 * Find fixtures using the case id, start date and end date
	 * 
	 * @param caseId
	 * 			  Case id to use when performing the search
	 * @param fromDate
	 * 			  Start date to use when performing the search
	 * @param toDate
	 * 			  End date to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	 public Collection findByCaseIdAndListingDate(Integer caseId, Date fromDate, Date toDate) throws ObjectNotFoundException {
		 try {
			 log.debug("findByCaseIdAndListingDate");
			 return this.getHome().findByCaseIdAndListingDate(caseId, DateTimeUtilities.stripTimeToUtilDate(fromDate),
					 											DateTimeUtilities.stripTimeToUtilDate(toDate));
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw e;
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
	 }
    
    /**
	 * Find fixtures using the case id, start date and end date with the
	 * defendantOnCaseId marked as attending on any of the fixtures
	 * 
	 * @param caseId
	 * 			  Case id to use when performing the search
	 * @param fromDate
	 * 			  Start date to use when performing the search
	 * @param toDate
	 * 			  End date to use when performing the search
	 * @param defendantOnCaseId
	 * 			  Defendant On Case Id to use when performing the search
	 * @return The local interface of the returned collection
	 * @throws ObjectNotFoundException
	 */
	 public Collection findByCaseIdAndListingDateAndDefendantOnCaseId(Integer caseId, Date fromDate, Date toDate, Integer defendantOnCaseId) throws ObjectNotFoundException {
		 try {
			 log.debug("findByCaseIdAndListingDateAndDefendantOnCaseId");
			 return this.getHome().findByCaseIdAndListingDateAndDefendantOnCaseId(caseId, DateTimeUtilities.stripTimeToUtilDate(fromDate),
					 															DateTimeUtilities.stripTimeToUtilDate(toDate), defendantOnCaseId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw e;
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
	 }
    
    /**
	 * Find fixtures using the case entry id, start date and end date
	 * 
	 * @param integer
	 * 			  Case entry id to use when performing the search
	 * @param date
	 * 			  Start date to use when performing the search
	 * @param date
	 * 			  End date to use when performing the search
	 * @return The number of rows
	 * @throws ObjectNotFoundException
	 */
	 public Collection findCaseDiaryFixturesImpacted(Integer caseEntryId, Date startDate, Date endDate, Date currentDate) throws ObjectNotFoundException {
		 try {
			 log.debug("findCaseDiaryFixturesImpacted");
			 return this.getHome().findCaseDiaryFixturesImpacted(caseEntryId, startDate, endDate, currentDate);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw e;
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
	 }
	 
	 /**
	  * Find Fixtures for Today by date.
	  * @param fixtureDate The date of the fixture you are searching for.
	  * @param courtId The court Id the cases belong to. 
	  * @return  The local interface of the returned collection
	  */
	 public Collection findByListingDateAndCourtId(final Date fixtureDate, final Integer courtId) {
		 try {
			 log.debug("findByListingDateAndCourtId");
			 return this.getHome().findByListingDateAndCourtId(fixtureDate, courtId);
		 } catch (FinderException f) {
				CSServices.getDefaultErrorHandler().handleError(f, getClass());
				throw new EJBException(f);
		} 
	 }

    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            CaseDiaryFixture
     * @return CaseDiaryFixtureBasicValue
     */
    public CaseDiaryFixtureBasicValue getBasicValue(CaseDiaryFixture local) {
        log.debug("Entered: getBasicValue");
        CaseDiaryFixtureBasicValue value = new CaseDiaryFixtureBasicValue((Integer) local.getPrimaryKey(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            CaseDiaryFixture
     * @return CaseDiaryFixtureComplexValue
     */
    public CaseDiaryFixtureComplexValue getComplexValue(CaseDiaryFixture local) {
        log.debug("Entered: getComplexValue");
        CaseDiaryFixtureComplexValue value = new CaseDiaryFixtureComplexValue((Integer) local.getPrimaryKey(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }
    
    /**
     * Create and return a Collection given a local entity.
     * 
     * @param locals
     *            Collection
     * @return Collection<CaseDiaryFixtureBasicValue>
     */
    public Collection<CaseDiaryFixtureBasicValue> getBasicValues(Collection locals) {
        log.debug("Entered: getBasicValues"); 
        return getCollectionValues(false, locals);
    }

    /**
     * Create and return a Collection given a local entity.
     * 
     * @param locals
     *            Collection
     * @return Collection<CaseDiaryFixtureComplexValue>
     */
    public Collection<CaseDiaryFixtureComplexValue> getComplexValues(Collection locals) {
        log.debug("Entered: getComplexValues"); 
        return getCollectionValues(true, locals);
    }
    
    /**
     * The CaseDiaryFixture Home.
     * 
     * @return CaseDiaryFixtureHome
     */
    public CaseDiaryFixtureHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise CaseDiaryFixtureHome");
            this.home = (CaseDiaryFixtureHome) CSServices.getServiceLocator().getLocalHome(CaseDiaryFixtureHome.class);
        }
        return this.home;
    }

    protected void loadValue(CaseDiaryFixtureBasicValue value, CaseDiaryFixture local) {
    	value.setCaseDiaryFixtureId(local.getCaseDiaryFixtureId()) ;
    	value.setCaseListingEntryId(local.getCaseListingEntryId());
    	value.setListingDate(local.getListingDate());
    	value.setFixtureNoticeRequired(local.getFixtureNoticeRequired());	   
    	value.setHearingTypeId(local.getHearingTypeId());
    	value.setListNoteText(local.getListNoteText());	   
    	value.setListNotePreDefinedId(local.getListNotePreDefinedId());
    	value.setPreDefNoteClassId(local.getPreDefNoteClassId());
    	value.setFreeTextNoteClassId(local.getFreeTextNoteClassId());
    	value.setVacationPreDefinedRsonId(local.getVacationPreDefinedRsonId());
    	value.setVacationFreetextReason(local.getVacationFreetextReason());	   
    	value.setStatus(local.getStatus());
    	value.setObsInd(local.getObsInd());
    	value.setCourtSiteId(local.getCourtSiteId());
		value.setDateVacated(local.getDateVacated());
    }
    
	@SuppressWarnings("unchecked")
	private <T> Collection<T> getCollectionValues(boolean complexValue, Collection locals) {
        log.debug("Entered: getCollectionValues");
        if (locals == null)
            return null;

        Collection<T> values = new ArrayList<T>();
        Iterator it = locals.iterator();
        T rowValue = null;
        CaseDiaryFixture element = null;
        while (it.hasNext()) {
        	element = (CaseDiaryFixture) it.next();
        	if (complexValue) {
        		rowValue = (T) getComplexValue(element);
        	} else {
        		rowValue = (T) getBasicValue(element);	
        	}        	
        	values.add(rowValue);
        }
        return values;
    }

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        if (!(value instanceof CaseDiaryFixtureBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
		try {
			CaseDiaryFixtureBasicValue basicValue = (CaseDiaryFixtureBasicValue) value;
			CaseDiaryFixture result = getHome().create(
					basicValue.getCaseListingEntryId(), basicValue.getListingDate(),
					basicValue.getFixtureNoticeRequired(), basicValue.getHearingTypeId(), basicValue.getListNotePreDefinedId(),
					basicValue.getListNoteText(), basicValue.getPreDefNoteClassId(), basicValue.getFreeTextNoteClassId(),
					basicValue.getVacationPreDefinedRsonId(), basicValue.getVacationFreetextReason(), basicValue.getStatus(),
					basicValue.getCourtSiteId(), basicValue.getDateVacated(), userDisplayName);
			return result;
		} catch (CreateException e) {
		       CSServices.getDefaultErrorHandler().handleError(e, getClass());
	            throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		
        if (!(value instanceof CaseDiaryFixtureBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
        try {        
        	// Get the current db values
        	CaseDiaryFixture local = getHome().findByPrimaryKey(value.getId());
        	        	
	        // Check the version
	        if (local.getVersion() == null || value.getVersion() == null || !local.getVersion().equals(value.getVersion())) {
	            throw new OptimisticLockException("Optimistic Lock Error");
	        }
        	CaseDiaryFixtureBasicValue basicValue = (CaseDiaryFixtureBasicValue) value;

        	// Update the record
        	local.setCaseListingEntryId(basicValue.getCaseListingEntryId());
        	local.setListingDate(basicValue.getListingDate());
        	local.setFixtureNoticeRequired(basicValue.getFixtureNoticeRequired());
        	local.setHearingTypeId(basicValue.getHearingTypeId());
        	local.setListNotePreDefinedId(basicValue.getListNotePreDefinedId());
        	local.setListNoteText(basicValue.getListNoteText());
        	local.setPreDefNoteClassId(basicValue.getPreDefNoteClassId());
        	local.setFreeTextNoteClassId(basicValue.getFreeTextNoteClassId());
        	local.setStatus(basicValue.getStatus());
        	local.setCourtSiteId(basicValue.getCourtSiteId());
        	local.setVacationFreetextReason(basicValue.getVacationFreetextReason());
        	local.setVacationPreDefinedRsonId(basicValue.getVacationPreDefinedRsonId());
        	local.setDateVacated(basicValue.getDateVacated());
        	local.setObsInd(basicValue.getObsInd());;
        	local.setUpdated(userDisplayName);
        	
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw new EJBException(ex);
        }
	}

	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		throw new UnsupportedOperationException();
	}
		
	public void delete(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {	
		if (!(value instanceof CaseDiaryFixtureBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
		try {
			// Get the current db values        	        	
	    	CaseDiaryFixture local = getHome().findByPrimaryKey(value.getId());
	    	        	
	        // Check the version
	        if (local.getVersion() == null || value.getVersion() == null || !local.getVersion().equals(value.getVersion())) {
	            throw new OptimisticLockException("Optimistic Lock Error");
	        }
	    	CaseDiaryFixtureBasicValue basicValue = (CaseDiaryFixtureBasicValue) value;
	 
	    	// Update the record
			local.setVacationPreDefinedRsonId(basicValue.getVacationPreDefinedRsonId());
			local.setVacationFreetextReason(basicValue.getVacationFreetextReason());
			local.setStatus(basicValue.getStatus());
			local.setUpdated(userDisplayName);
			if ("Y".equals(local.getObsInd()) && basicValue.getDateVacated() != null) {
				local.setDateVacated(basicValue.getDateVacated());
			} else {
				local.setDateVacated(new Date());
				local.setObsInd("Y");
			}
			
	    } catch (ObjectNotFoundException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw ex;
	    } catch (FinderException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw new EJBException(ex);
	    }		
	}
}
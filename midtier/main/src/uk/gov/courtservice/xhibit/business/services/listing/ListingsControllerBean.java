package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.listing.DefOnCaseOnListMaintainer;
import uk.gov.courtservice.xhibit.business.services.caze.CaseHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseFilterResultComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseIdValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingHistoryInformation;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseNonAvailDaysBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListSaveResult;
import uk.gov.courtservice.xhibit.business.vos.entities.ListingResultsInformation;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseDiaryFixtureValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseListingEntryValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseListingFilterCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CreateListOptionsValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.ListValue;



/**
 * <p>
 * Title: ListingsControllerBean
 * </p>
 * <p>
 * Description: Session bean for manipulating listings objects
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="ListingsController" description="Listings Session Bean"
 *           type="Stateless" view-type="both" jndi-name="ListingsControllerHome"
 *           local-jndi-name="ListingsControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class ListingsControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;
	
	private static final String CREATE_EXCEPTION = "listingscontroller.createException";

	private static final String FIND_EXCEPTION = "listingscontroller.findException";
	private static final String SAVE_EXCEPTION = "listingscontroller.saveException";
	private static final String DELETE_EXCEPTION = "listingscontroller.deleteException";
	private static final String PUBLISH_EXCEPTION = "listingscontroller.publishException";
	private static final String CASE_TRANSFERRED_OUT_EXCEPTION = "listingscontroller.CaseTransferredOutException";

	private CaseListingEntryHelper caseListingEntryHelper = new CaseListingEntryHelper();
	private CaseDiaryFixtureHelper caseDiaryFixtureHelper = new CaseDiaryFixtureHelper();
	private CaseNonAvailDaysHelper caseNonAvailDaysHelper = new CaseNonAvailDaysHelper();
	private CaseOnListHelper caseOnListHelper = new CaseOnListHelper();
	private DiaryNoteEntryHelper diaryNoteEntryHelper = new DiaryNoteEntryHelper();
	private FixtureDeftAttendingHelper fixtureDeftAttendingHelper = new FixtureDeftAttendingHelper(); 
	private ListHelper listHelper = new ListHelper();
	private CaseHelper caseHelper = new CaseHelper();
	private SittingOnListHelper sittingOnListHelper = new SittingOnListHelper();
	private ListingsDatabaseManager listDatabaseManager = new ListingsDatabaseManager();
	private DefOnCaseOnListMaintainer defOnCaseOnListMaintainer;
	
	public ListingsControllerBean() {
		defOnCaseOnListMaintainer = new DefOnCaseOnListMaintainer();
	}
    /**
     * Returns case listing entry by caseId and courtId
     * 
     * @param caseId
	 *            ID Integer of the case 
	 * @return CaseListingEntryComplexValue
	 * 
     * @ejb.interface-method view-type="both"
     * 
	 * @throws ListingsControllerException 
     */
    public CaseListingEntryComplexValue getCaseListingEntryByCaseIdAndCourtId(Integer caseId, Integer courtId) throws ListingsControllerException {
       	if ( log.isDebugEnabled() ) {
       		log.debug("START: getCaseListingEntryByCaseIdAndCourtId(caseId="+caseId+", courtId="+courtId+")");
       	}
        CaseListingEntryComplexValue result = null;
    	try {
    		result =  caseListingEntryHelper.findCaseListingEntryByCaseIdAndCourtId(caseId, courtId); 
    		return result;
    	} catch (CSBusinessException e) {
    		handleWrapAndRethrowException(FIND_EXCEPTION, e);
    	} catch (FinderException e) {
    		handleWrapAndRethrowException(FIND_EXCEPTION, e);
    	}    	
    	return result;
    }
    
	/**
	 * Returns a non available days basic value object for the given id
	 * 
	 * @param caseNonAvailDaysId
	 *            ID Integer of the case non-available day 
	 * @return CaseNonAvailDaysBasicValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public CaseNonAvailDaysBasicValue findCaseNonAvailDays(Integer caseNonAvailDaysId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseNonAvailDays(caseNonAvailDaysId="+caseNonAvailDaysId+")");
       	}
		CaseNonAvailDaysBasicValue result = null;
		try {
			result = caseNonAvailDaysHelper.findByPrimaryKey(caseNonAvailDaysId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}

	/**;
	 * Returns a collection of non available days basic value objects for the given caseId
	 * 
	 * @param caseId
	 *            ID Integer of the case id 
	 * @return Collection
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Collection<CaseNonAvailDaysBasicValue> findCaseNonAvailDaysByCaseId(Integer caseId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseNonAvailDaysByCaseId(caseId="+caseId+")");
       	}
		Collection<CaseNonAvailDaysBasicValue> results = null;
		try {
			results = caseNonAvailDaysHelper.findByCaseId(caseId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}
    
	/**
	 * Returns true if non available days exist for caseId and date
	 * 
	 * @param caseId
	 *            ID Integer of the case
	 * @param date
	 *            date to check for a non-available day 
	 * @return true if non-available day for case
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Boolean isCaseNonAvailDay(Integer caseId, Date date) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: isCaseNonAvailDay(caseId="+caseId+", date="+date+")");
       	}
		Boolean result = false;
		try {
			result = caseNonAvailDaysHelper.isCaseNonAvailDay(caseId, date);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}
    
	/**
	 * Returns true if non available days exist for caseId and dates
	 * 
	 * @param caseId
	 *            ID Integer of the case
	 * @param fromDate
	 *            from date to check for a non-available day 
	 * @param toDate
	 *            to date to check for a non-available day 
	 * @return true if non-available day for case
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Boolean isCaseNonAvailDay(Integer caseId, Date fromDate, Date toDate) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: isCaseNonAvailDay(caseId="+caseId+", fromDate="+fromDate+", toDate="+toDate+")");
       	}
		Boolean result = false;
		try {
			result = caseNonAvailDaysHelper.isCaseNonAvailDay(caseId, fromDate, toDate);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}
    
	/**
	 * Returns true if case diary fixtures exist for caseId and dates
	 * 
	 * @param caseId
	 *            ID Integer of the case
	 * @param fromDate
	 *            from date to check for a fixture 
	 * @param toDate
	 *            to date to check for a fixture 
	 * @return true if fixture for case
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Boolean isCaseDiaryFixture(Integer caseId, Date fromDate, Date toDate) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: isCaseDiaryFixture(caseId="+caseId+", fromDate="+fromDate+", toDate="+toDate+")");
       	}
		Boolean result = false;
		try {
			result = caseDiaryFixtureHelper.isCaseDiaryFixture(caseId, fromDate, toDate);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}
    
	/**
	 * Returns a collection of CaseDiaryFixtureComplexValue for caseId and dates
	 * with the defendantOnCaseId marked as attending on the fixtures
	 * 
	 * @param caseId
	 *            ID Integer of the case
	 * @param fromDate
	 *            from date to check for a fixture 
	 * @param toDate
	 *            to date to check for a fixture 
	 * @param defendantOnCaseId
	 *            defendant on case id to check for a fixture 
	 * @return collection of CaseDiaryFixtureComplexValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Collection<CaseDiaryFixtureComplexValue> findCaseDiaryFixturesByCaseIdAndListingDateAndDefendantOnCaseId(Integer caseId, Date fromDate, Date toDate, Integer defendantOnCaseId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseDiaryFixturesByCaseIdAndListingDateAndDefendantOnCaseId(caseId="+caseId+", fromDate="+fromDate+", toDate="+toDate+", defendantOnCaseId="+defendantOnCaseId+")");
       	}
		Collection<CaseDiaryFixtureComplexValue> results = null;
		try {
			results = caseDiaryFixtureHelper.findByCaseIdAndListingDateAndDefendantOnCaseId(caseId, fromDate, toDate, defendantOnCaseId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}
	
	/**
	 * Returns collection for diary fixture rows impacted for given case entry id, start date and end date
	 * @param caseEntryId
	 * @param startDate
	 * @param endDate
	 * @return Collection
	 * @ejb.interface-method view-type="both"
	 * @throws ListingsControllerException
	 */
	public Collection findCaseDiaryFixturesImpacted(Integer caseEntryId, Date startDate, Date endDate, Date currentDate) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseDiaryFixturesImpacted(caseEntryId="+caseEntryId+", startDate="+startDate+", endDate="+endDate+", currentDate="+currentDate+")");
       	}
		Collection results = null;
		try {
			results =  caseDiaryFixtureHelper.findCaseDiaryFixturesImpacted(caseEntryId, startDate, endDate, currentDate);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}
	
	/**
	 * Returns collection for diary fixture rows for given case entry id
	 * @param caseListingEntryId
	 * @return Collection
	 * @ejb.interface-method view-type="both"
	 * @throws ListingsControllerException
	 */
	public Collection findPendingCaseDiaryFixturesByCaseEntryId(Integer caseListingEntryId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findPendingCaseDiaryFixturesByCaseEntryId(caseListingEntryId="+caseListingEntryId+")");
       	}
		Collection results = null;
		try {
			results = caseDiaryFixtureHelper.getPendingCaseDiaryFixturesByCaseListingIdAndStatus(caseListingEntryId, "A");
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}
        
    /**
     * Method to save case listing entry
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param CaseListingEntryValue
     *            a Case Listing Entry Value object
     * @throws ListingsControllerException
     */
    public void saveCaseListingEntry(CaseListingEntryValue value, String userDisplayName) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: saveCaseListingEntry(value="+value+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	caseListingEntryHelper.saveCaseListingEntry(value, userDisplayName);
        } catch (CreateException e) {
        	handleWrapAndRethrowException(SAVE_EXCEPTION, e);
        } catch (FinderException e) {
        	handleWrapAndRethrowException(SAVE_EXCEPTION, e);
        } catch (OptimisticLockException e) {
        	handleWrapAndRethrowException(SAVE_EXCEPTION, e);
        }
    }
    
    /**
     * Method to save a diary note entry
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param DiaryNoteEntryBasicValue
     *            a Diary Note Entry Value object
     * @param String
     *            user name
     * @throws ListingsControllerException
     */
    public Integer saveDiaryNoteEntry(DiaryNoteEntryBasicValue value, String userDisplayName) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: saveDiaryNoteEntry(value="+value+", userDisplayName="+userDisplayName+")");
       	}
        Integer result = null;
        try {
        	//If we are saving a new case note then validate it isn't transferred out
        	if (diaryNoteEntryHelper.isTransferredOutCase(value.getCaseId())) {
        		throw new ListingsControllerException(CASE_TRANSFERRED_OUT_EXCEPTION, "TransferredOut - CaseId:" + value.getCaseId());
        	}
        	result = diaryNoteEntryHelper.saveDiaryNoteEntry(value, userDisplayName);
        } catch (CreateException e) {
        	handleWrapAndRethrowException(SAVE_EXCEPTION, e);
        } catch (FinderException e) {
        	handleWrapAndRethrowException(SAVE_EXCEPTION, e);
        } catch (OptimisticLockException e) {
        	handleWrapAndRethrowException(SAVE_EXCEPTION, e);
        }
        return result;
    }

    /**
     * Method to delete a diary note entry
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param DiaryNoteEntryBasicValue
     *            a Diary Note Entry Value object
     * @param String
     *            user name
     * @throws ListingsControllerException
     */
    public void deleteDiaryNoteEntry(DiaryNoteEntryBasicValue value, String userDisplayName) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: deleteDiaryNoteEntry(value="+value+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	diaryNoteEntryHelper.deleteDiaryNoteEntry(value, userDisplayName);
        } catch (FinderException e) {
        	handleWrapAndRethrowException(DELETE_EXCEPTION, e);
        } catch (OptimisticLockException e) {
        	handleWrapAndRethrowException(DELETE_EXCEPTION, e);
        }        
    }
    
    /**
     * Method to save case listing non additional days
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param CaseNonAvailDaysBasicValue
     *            a Case Listing Non Additional Day object
     * 			  reason for additional days 
     * throws ListingsControllerException
     */
    public Integer saveCaseListingNonAdditionalDays(CaseNonAvailDaysBasicValue value, String userDisplayName) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: saveCaseListingNonAdditionalDays(value="+value+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	return caseNonAvailDaysHelper.saveCaseNonAvailDaysBasicValue(value, userDisplayName);
        } catch (CreateException e) {
            handleWrapAndRethrowException(SAVE_EXCEPTION, e);
        } catch (FinderException e) {
        	handleWrapAndRethrowException(SAVE_EXCEPTION, e);
        }
		return null;
    }

    /**
     * Method to find case diary fixture
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param Integer
     *            a Case diary fixture object id
     * @throws ListingsControllerException
     */
    public CaseDiaryFixtureComplexValue findCaseDiaryFixture(Integer id) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseDiaryFixture(id="+id+")");
       	}
        CaseDiaryFixtureComplexValue result = null;
        try {
        	result = caseDiaryFixtureHelper.findByPrimaryKey(id);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
        return result;
    }
    
    /**
     * Method to save case diary fixture
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param CaseDiaryFixtureValue
     *            a Case diary fixture object
     * @throws ListingsControllerException
     */
    public Integer saveCaseDiaryFixture(CaseDiaryFixtureValue value, String userDisplayName) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: saveCaseDiaryFixture(value="+value+", userDisplayName="+userDisplayName+")");
       	}
        Integer result = null;
        try {
        	result = caseDiaryFixtureHelper.saveCaseDiaryFixture(value, userDisplayName);
        } catch (CreateException e) {
            handleWrapAndRethrowException(SAVE_EXCEPTION, e);
        } catch (FinderException e) {
            handleWrapAndRethrowException(SAVE_EXCEPTION, e);
        }
        return result;
    }

    /**
     * Method to soft delete case listing non available days
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param CaseNonAvailDaysBasicValue
     *            a Case Listing Non Available Days Object
     * @param String
     * @throws ListingsControllerException
     */
    public void deleteCaseNonAvailDaysBasicValue(CaseNonAvailDaysBasicValue value, String userDisplayName) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: deleteCaseNonAvailDaysBasicValue(value="+value+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	caseNonAvailDaysHelper.deleteCaseNonAvailDaysBasicValue(value,userDisplayName);
        } catch (FinderException e) {
            handleWrapAndRethrowException(DELETE_EXCEPTION, e);
        }
    }
    
    /**
     * Method to delete a case diary fixture
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param CaseDiaryFixtureComplexValue
     *            a Case diary fixture object
     * @throws ListingsControllerException
     */
    public void deleteCaseDiaryFixture(CaseDiaryFixtureComplexValue value, String userDisplayName) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: deleteCaseDiaryFixture(value="+value+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	caseDiaryFixtureHelper.deleteCaseDiaryFixture(value, userDisplayName);
        } catch (FinderException e) {
            handleWrapAndRethrowException(DELETE_EXCEPTION, e);
        }
    }
  
    /**
     * Method to delete a case on list
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param CaseOnListComplexValue
     *            a Case on list object
     * @throws ListingsControllerException
     */
    public void deleteCaseOnList(CaseOnListComplexValue value, String userDisplayName) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: deleteCaseOnList(value="+value+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	caseOnListHelper.deleteCaseOnList(value, userDisplayName);
        } catch (FinderException e) {
            handleWrapAndRethrowException(DELETE_EXCEPTION, e);
        }
    }
    
    /**
     * Method to delete a case on list and def on case on list
     * 
     * @ejb.interface-method view-type="both"
     * 
     * @param CaseOnListComplexValue
     *            a Case on list object
     * @throws ListingsControllerException
     */
    public void deleteCaseOnListAndDefOnCaseOnList(CaseOnListComplexValue value, String userDisplayName) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: deleteCaseOnList(value="+value+", userDisplayName="+userDisplayName+")");
       	}
        try {
        	//delete the case on list
        	caseOnListHelper.deleteCaseOnList(value, userDisplayName);
        	
        	//delete the defoncaseonlist
        	List<DefOnCaseOnListBasicValue> defOnCaseOnLists = (List<DefOnCaseOnListBasicValue>) defOnCaseOnListMaintainer.getBasicValues(defOnCaseOnListMaintainer.findByCaseOnListId(value.getCaseOnListId()));
    		for (DefOnCaseOnListBasicValue defOnCaseOnList : defOnCaseOnLists) {
    			defOnCaseOnList.setObsInd("Y");
    			defOnCaseOnListMaintainer.delete(defOnCaseOnList, userDisplayName);
    		}
        	
        } catch (FinderException e) {
            handleWrapAndRethrowException(DELETE_EXCEPTION, e);
        }
    }
	/**
	 * Returns a sitting on list basic value object for the given id
	 * 
	 * @param sittingOnListId
	 *            ID Integer of the sitting on list 
	 * @return SittingOnListBasicValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public SittingOnListBasicValue findSittingOnList(Integer sittingOnListId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findSittingOnList(sittingOnListId="+sittingOnListId+")");
       	}
		SittingOnListBasicValue result = null;
		try {
			result = sittingOnListHelper.findByPrimaryKey(sittingOnListId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}

	/**
	 * Returns a collection of sitting on list basic value objects for the given listId
	 * 
	 * @param sittingOnListId
	 *            ID Integer of the list id 
	 * @return Collection
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Collection findSittingOnListByListId(Integer listId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findSittingOnListByListId(listId="+listId+")");
       	}
		Collection results = null;
		try {
			results = sittingOnListHelper.findByListId(listId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}
	
	/**
	 * Returns a list basic value object for the given id
	 * 
	 * @param listId
	 *            ID Integer of the list 
	 * @return ListBasicValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public ListBasicValue findList(Integer listId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findList(listId="+listId+")");
       	}
		ListBasicValue result = null;
		try {
			result = listHelper.findByPrimaryKey(listId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}
	
	/**
	 * Returns a list complex value object for the given id
	 * 
	 * @param listId
	 *            ID Integer of the list 
	 * @return ListComplexValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public ListComplexValue findListDetail(Integer listId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findListDetail(listId="+listId+")");
       	}
		ListComplexValue result = null;
		try {
			result = listHelper.findDetailByPrimaryKey(listId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}
	
	/**
	 * Returns a list complex value object for the given list value
	 * 
	 * @param listValue
	 *            List, Sittings On List and Cases On List to return 
	 * @return ListComplexValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public ListComplexValue findListDetailByListValue(ListValue listValue) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findListDetailByListValue(listValue="+listValue+")");
       	}
		ListComplexValue result = null;
		try {
			result = listHelper.findDetailByListValue(listValue);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}

	/**
	 * Returns a collection of list basic value objects for the given courtId
	 * 
	 * @param courtId
	 *            ID Integer of the court id 
	 * @return Collection
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Collection findListByCourtId(Integer courtId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findListByCourtId(courtId="+courtId+")");
       	}
		Collection results = null;
		try {
			results = listHelper.findByCourtId(courtId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}
    
	/**
	 * Method to save list
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param ListValue
	 *            a List Value object
	 * @throws ListingsControllerException
	 */
	public ListSaveResult saveList(ListValue value, String userDisplayName) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: saveList(value="+value+", userDisplayName="+userDisplayName+")");
       	}
	    ListSaveResult result = null;
	    try {
	    	result = listHelper.saveList(value, userDisplayName);
	    } catch (CreateException e) {
	    	handleWrapAndRethrowException(SAVE_EXCEPTION, e);
	    } catch (FinderException e) {
	    	handleWrapAndRethrowException(SAVE_EXCEPTION, e);
	    } catch (OptimisticLockException e) {
	    	handleWrapAndRethrowException(SAVE_EXCEPTION, e);
	    }
        return result;
	}
	
	/**
	 * Returns a case on list basic value object for the given id
	 * 
	 * @param caseOnListId
	 *            ID Integer of the case on list 
	 * @return ListBasicValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public CaseOnListBasicValue findCaseOnList(Integer caseOnListId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseOnList(caseOnListId="+caseOnListId+")");
       	}
		CaseOnListBasicValue result = null;
		try {
			result = caseOnListHelper.findByPrimaryKey(caseOnListId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}

	/**
	 * Returns a case on list complex value object for the given id
	 * 
	 * @param caseOnListId
	 *            ID Integer of the case on list 
	 * @return CaseOnListComplexValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public CaseOnListComplexValue findCaseOnListComplexValue(Integer caseOnListId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseOnListComplexValue(caseOnListId="+caseOnListId+")");
       	}
		CaseOnListComplexValue result = null;
		try {
			CaseOnListBasicValue basicValue = caseOnListHelper.findByPrimaryKey(caseOnListId);
			result = new CaseOnListComplexValue();
			caseOnListHelper.copyVO(basicValue, result);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}
	
	/**
	 * Description: Find Collection of Case On List by caseId
	 * 
	 * @param caseId
	 * @return Collection
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	@SuppressWarnings("unchecked")
	public Collection<CaseOnListBasicValue>  findCaseOnListByCaseId(Integer caseId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseOnListByCaseId(caseId="+caseId+")");
       	}
		Collection result = null;
		try {
			result = (Collection) caseOnListHelper.findByCaseId(caseId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}

	/**
	 * Returns a collection of list basic value objects for the given courtId
	 * 
	 * @param courtSiteId
	 *            ID Integer of the court site id 
	 * @param courtRoomId
	 *            ID Integer of the court room id 
	 * @return Collection
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Collection findCaseOnListByCourtSiteIdAndCourtRoomId(Integer courtSiteId, Integer courtRoomId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseOnListByCourtSiteIdAndCourtRoomId(courtSiteId="+courtSiteId+", courtRoomId="+courtRoomId+")");
       	}
		Collection results = null;
		try {
			results = caseOnListHelper.findByCourtSiteIdAndCourtRoomId(courtSiteId, courtRoomId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}

	/**
	 * Returns a collection of nonfixture Case On List complex value objects for the listId
	 * 
	 * @param listId
	 *            ID Integer of the list id 
	 * @param reserved
	 *            Filter by reserved flag
	 * @return Collection
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Collection<CaseOnListComplexValue> getNonFixtureCasesOnList(final Integer listId, final String reserved) 
			throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getNonFixtureCasesOnList(listId="+listId+", reserved="+reserved+")");
       	}
		Collection<CaseOnListComplexValue> results = null;
		try {
			results = listDatabaseManager.findUnlistedCasesOnList(listId, reserved);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}
	
	/** 
	 * Returns a fixtureDeftAttending basic value object for the given id
	 * 
	 * @param fixtureDeftAttendingId
	 *            ID Integer of the fixtureDeftAttending 
	 * @return FixtureDeftAttendingBasicValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public FixtureDeftAttendingBasicValue findFixtureDeftAttending(Integer fixtureDeftAttendingId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findFixtureDeftAttending(fixtureDeftAttendingId="+fixtureDeftAttendingId+")");
       	}
		FixtureDeftAttendingBasicValue result = null;
		try {
			result = fixtureDeftAttendingHelper.findByPrimaryKey(fixtureDeftAttendingId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return result;
	}

	/**
	 * Returns a collection of CaseDiaryFixtureComplexValue for a given date.
	 * @param entryDate
	 * @param courtId.
	 * @return CaseDiaryFixtureComplexValue
	 * @ejb.interface-method view-type="both"
	 * @throws ListingsControllerException
	 */
	public Collection<CaseDiaryFixtureComplexValue> findCaseDiaryFixturesByDateAndCourtId(final Date entryDate, final Integer courtId)
			throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseDiaryFixturesByDateAndCourtId(entryDate="+entryDate+", courtId="+courtId+")");
       	}
		Collection<CaseDiaryFixtureComplexValue> results = null;

		try {
			results = caseDiaryFixtureHelper.findByListingDateAndCourtId(entryDate, courtId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;

	}

	/**
	 * Returns a collection of diary note basic value objects for the given courtId and date
	 * 
	 * @param courtId
	 *            ID of the court
	 * @param diaryDate
	 *            Diary date 
	 * @return Collection
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Collection<DiaryNoteEntryComplexValue> findGeneralDiaryNotesByCourtIdAndDate(Integer courtId, Date diaryDate) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findGeneralDiaryNotesByCourtIdAndDate(courtId="+courtId+", diaryDate="+diaryDate+")");
       	}
		Collection<DiaryNoteEntryComplexValue> results = null;
		try {
			results = diaryNoteEntryHelper.findGeneralDiaryNotesByCourtIdAndDate(courtId, diaryDate);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}	
	
	/**
	 * Returns a collection of diary note basic value objects for the given courtId and date
	 * 
	 * @param courtId
	 *            ID of the court
	 * @param diaryStartDate
	 *            Diary  Start date 
	* @param diaryEndDate
	 *            Diary End date             
	 * @return Collection
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Collection<DiaryNoteEntryComplexValue> findGeneralDiaryNotesByCourtIdAndDates(final Integer courtId,
			final Date diaryStartDate, final Date diaryEndDate) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findGeneralDiaryNotesByCourtIdAndDates(courtId="+courtId+", diaryStartDate="+diaryStartDate+", diaryEndDate="+diaryEndDate+")");
       	}
		Collection<DiaryNoteEntryComplexValue> results = null;
		try {
			results = diaryNoteEntryHelper.findGeneralDiaryNotesByCourtIdAndDates(courtId, diaryStartDate, diaryEndDate);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}	
	
	
	/**
	 * Returns a collection of fixtureDeftAttending basic value objects for the given caseDiaryFixtureId
	 * 
	 * @param caseDiaryFixtureId
	 *            ID Integer of the case diary fixture id 
	 * @return Collection
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Collection findFixtureDeftAttendingByCaseDiaryFixtureId(Integer caseDiaryFixtureId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findFixtureDeftAttendingByCaseDiaryFixtureId(caseDiaryFixtureId="+caseDiaryFixtureId+")");
       	}
		Collection results = null;
		try {
			results = fixtureDeftAttendingHelper.findByCaseDiaryFixtureId(caseDiaryFixtureId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}	

	/**
	 * Returns a collection of diary note basic value objects for the given case Id
	 * 
	 * @param caseId
	 *            ID of the court
	 * @return Collection
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public Collection findCaseNotesByCaseId(Integer caseId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseNotesByCaseId(caseId="+caseId+")");
       	}
		Collection results = null;
		try {
			results = diaryNoteEntryHelper.findCaseNotesByCaseId(caseId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return results;
	}	
	
	/**
     * Create new 'U' case.
     * 
     * @param AddCaseValue -
     *            Details of the case to be created
     * @return Case - Details of the case that has been created
     * @throws ListingsControllerException
     * 
     * @ejb.interface-method view-type="both"
     */
    public CaseBasicValue createNewUorBCase(final AddCaseValue addCaseValue, final String userDisplayName) throws ListingsControllerException {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: createNewUorBCase(addCaseValue="+addCaseValue+", userDisplayName="+userDisplayName+")");
       	}
        CaseBasicValue result = null;
        try {
            result = caseListingEntryHelper.createNewUorBCase(addCaseValue, userDisplayName);
        } catch (final CreateException e) {
        	handleWrapAndRethrowException(CREATE_EXCEPTION,e);
        }
        return result;
    }
    
    /**
     * Find Listings
     * 
     * @param courtId The court Id 
     * @param listType 
     * @param rowNumberLimit The row number limit for the data fetch
     * @return Lists details
     * @throws ListingsControllerException
     * 
     * @ejb.interface-method view-type="both"
     */
    public Collection<ListBasicValue> findListsByRowNumber(Integer courtId, String listType, Integer rowNumberLimit) {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: findListsByRowNumber(courtId="+courtId+", listType="+listType+", rowNumberLimit="+rowNumberLimit+")");
       	}
    	return listDatabaseManager.findListsByRowNumber(courtId, listType, rowNumberLimit);
    }
    
    
    /**
     * Find Case On List
     * 
     * @param caseId The id of the case
     * @param rowNumberLimit The row number limit for the data fetch
     * @return Case on Lists details
     * @throws ListingsControllerException
     * 
     * @ejb.interface-method view-type="both"
     */
    public Collection<CaseListingHistoryInformation> findCaseListHistory(Integer caseId, Integer rowNumberLimit) {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: findCaseListHistory(caseId="+caseId+", rowNumberLimit="+rowNumberLimit+")");
       	}
    	return listDatabaseManager.findCaseListHistory(caseId, rowNumberLimit);
    }
    
    /**
     * Find Defendants On List
     * 
     * @param case on list id of the list
     * @return List of defendants on the case and on that list
     * @throws ListingsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public Collection<DefendantValue> findDefendantsOnList(Integer caseOnListId) {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: findDefendantsOnList(caseOnListId="+caseOnListId+")");
       	}
    	return listDatabaseManager.getDefendantsOnList(caseOnListId);
    }

    /**
     * Find Defendants On Fixture
     * 
     * @param case diary fixture id of the fixture
     * @return List of defendants on the case and on that fixture
     * @throws ListingsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public Collection<DefendantValue> findDefendantsOnFixture(Integer caseDiaryFixtureId) {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: findDefendantsOnFixture(caseDiaryFixtureId="+caseDiaryFixtureId+")");
       	}
    	return listDatabaseManager.getDefendantsOnFixture(caseDiaryFixtureId);
    }

    /**
     * Get Next Case On List Id
     * 
     * @return Case on List id
     * 
     * @ejb.interface-method view-type="both"
     */
    public Integer getNextCaseOnListId() {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: getNextCaseOnListId()");
       	}
    	return listDatabaseManager.getNextCaseOnListId();
    }

    /**
     * Get Next Sitting On List Id
     * 
     * @return Sitting on List id
     * 
     * @ejb.interface-method view-type="both"
     */
    public Integer getNextSittingOnListId() {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: getNextSittingOnListId()");
       	}
    	return listDatabaseManager.getNextSittingOnListId();
    }

    /**
     * Find Case on list using List Date, Court Site and Court Room
     * @param listDate List Date
     * @param courtSiteId Court Site Id
     * @param courtRoomId Court Room Id
     * @param courtId	Court Id (required if isAllCourtSites is true)
     * @param isAllCourtSites true if to return records for all sites, else false
     * @param isAllCourtRooms true if to return records for all court rooms, else false
     * @param isFloaterCourtRooms true if to return floater cases, else false
     * @return Cases on List
     * @throws ListingsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public Collection<ListingResultsInformation> findCasesOnListByDateSiteAndRoom(
    		Date listDate, 
    		Integer courtSiteId, 
    		Integer courtRoomId, 
    		Integer courtId,
    		Boolean isAllCourtSites,
    		Boolean isAllCourtRooms, 
    		Boolean isFloaterCourtRooms) {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: findCasesOnListByDateSiteAndRoom(listDate="+listDate+", courtSiteId="+courtSiteId+", courtRoomId="+courtRoomId+", courtId="+courtId+", isAllCourtSites="+isAllCourtSites+
       				", isAllCourtRooms="+isAllCourtRooms+", isFloaterCourtRooms="+isFloaterCourtRooms+")");
       	}
    	return listDatabaseManager.findCasesOnListByDateSiteAndRoom(listDate, courtSiteId, courtRoomId, courtId, isAllCourtSites, isAllCourtRooms, isFloaterCourtRooms);
    }

    /**
     * Get the lists for the create list options screen
     * 
     * @param courtId court identifier
     * @param listType list type
     * @param startDate start date
     * @param endDate end date
     * @return CreateListOptionsValue
     * 
     * @ejb.interface-method view-type="both"
     */
    public CreateListOptionsValue findCreateListOptions(Integer courtId, String listType, Date startDate, Date endDate) {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: findCreateListOptions(courtId="+courtId+", listType="+listType+", startDate="+startDate+", endDate="+endDate+")");
       	}
    	CreateListOptionsValue results = new CreateListOptionsValue(listType);
    	if (results.isDaily()) {
    		// Option 2 - Applicable Daily List
    		Collection<ListBasicValue> dailyLists = listDatabaseManager.findLatestListByDate(courtId, listType, startDate);
    		results.setPreviousDailyList(!dailyLists.isEmpty() ? ((List<ListBasicValue>) dailyLists).get(0) : null);
    		//Option 3 - Applicable Firm List
    		Collection<ListBasicValue> firmLists = listDatabaseManager.findLatestListByDate(courtId, CreateListOptionsValue.FIRM, startDate);
    		results.setPreviousFirmList(!firmLists.isEmpty() ? ((List<ListBasicValue>) firmLists).get(0) : null);
    	} else if (results.isFirm()) {    		
    		// Firm List in the date range
    		Collection<ListBasicValue> firmLists = listDatabaseManager.findListsInDateRange(courtId, listType, startDate, endDate);
    		results.setPreviousFirmList(!firmLists.isEmpty() ? ((List<ListBasicValue>) firmLists).get(0) : null);
    	} else if (results.isWarned()) {
    		// Warned List in the date range
    		Collection<ListBasicValue> warnedLists = listDatabaseManager.findListsInDateRange(courtId, listType, startDate, endDate);
    		results.setPreviousWarnList(!warnedLists.isEmpty() ? ((List<ListBasicValue>) warnedLists).get(0) : null);
    	}

    	return results;
    }

    /**
     * Finds a Daily List for the specified Date.
     * 
     * @param courtId court identifier
     * @param startDate start date
     * @return The daily list in a ListBasicValue
     * 
     * @ejb.interface-method view-type="both"
     */
	public ListBasicValue findDailyListByDateAndCourtId(final Integer courtId, final Date startDate) {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findDailyListByDateAndCourtId(courtId="+courtId+", startDate="+startDate+")");
       	}
		final List<ListBasicValue> dailyList = (List<ListBasicValue>) listDatabaseManager.findLatestListByDate(courtId,
				CreateListOptionsValue.DAILY, startDate);
		
		return !dailyList.isEmpty() && startDate.equals(dailyList.get(0).getListStartDate()) ? dailyList.get(0) : null;
	}

    
    /**
     * Finds a Warned List for the specified Date.
     * 
     * @param courtId court identifier
     * @param startDate start date
     * @return The warned list in a ListBasicValue
     * 
     * @ejb.interface-method view-type="both"
     */
	public ListBasicValue findWarnedListByDateAndCourtId(final Integer courtId, final Date startDate) {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findWarnedListByDateAndCourtId(courtId="+courtId+", startDate="+startDate+")");
       	}
		final List<ListBasicValue> warnedList = (List<ListBasicValue>) listDatabaseManager.findLatestListByDate(courtId,
				CreateListOptionsValue.WARNED, startDate);
		
		return warnedList.isEmpty() ? null : warnedList.get(0);
	}
	
	
    /**
     * Finds a Firm List for the specified Date.
     * 
     * @param courtId court identifier
     * @param startDate start date
     * @return The warned list in a ListBasicValue
     * 
     * @ejb.interface-method view-type="both"
     */
	public ListBasicValue findFirmListByDateAndCourtId(final Integer courtId, final Date startDate) {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findFirmListByDateAndCourtId(courtId="+courtId+", startDate="+startDate+")");
       	}
		final List<ListBasicValue> firmList = (List<ListBasicValue>) listDatabaseManager.findLatestListByDate(courtId,
				CreateListOptionsValue.FIRM, startDate);
		
		return firmList.isEmpty() ? null : firmList.get(0);
	}
    
    /**
     * Logically deletes a list from the database (marks as obsolete)
     * @param listId
     * @ejb.interface-method view-type="both"
     */
    public void deleteList(Integer listId)
    {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: deleteList(listId="+listId+")");
       	}
    	listDatabaseManager.deleteList(listId);
    }
    
    /**
     * Perfoms validation checks that the case does not exist on a warned list on the fixture listing
     * date or a reserved firm list on the fixture listing date.
     * @param caseId Case Id to search for
     * @param fixtureDate fixture listing date to search for
     * @return 'WARNED' if case exists on a warned list, 'FIRM' if it exists on a reserved firm list
     * 		   else 'NONE'
     * @ejb.interface-method view-type="both"
     */
    public String validateListsForFixture(Integer caseId, Date fixtureDate)
    {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: validateListsForFixture(caseId="+caseId+", fixtureDate="+fixtureDate+")");
       	}
    	return listDatabaseManager.validateListsForFixture(caseId, fixtureDate);
    }
    
    /**
     * Publishes the specified list to internal and external interfaces
     * @param listId
     * @throws  ListingsControllerException
     * @ejb.interface-method view-type="both"
     */
    public void publishList(Integer listId) throws ListingsControllerException
    {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: publishList(listId="+listId+")");
       	}
    	try{
    		// Assume success is inevitable (and update the list number)
    		listDatabaseManager.setListPublishStatus(listId, new Date(), ListBasicValue.PublishStatus.SUCCESS, null);
    		// Publish the list 
    		listDatabaseManager.publishList(listId);
    	} catch (DataAccessException ex) {
    		handleWrapAndRethrowException(PUBLISH_EXCEPTION, ex);
    	}
    }
    
    /**
     * Flag the list with a publish failure and stamp the reason onto the list 
     * @param listId
     * @param publishErrorReason
     * @throws  ListingsControllerException
     * @ejb.interface-method view-type="both"
     */
    public void setListPublishFailureStatus(Integer listId, String publishErrorReason) throws ListingsControllerException
    {
    	if ( log.isDebugEnabled() ) {
       		log.debug("START: setListPublishFailureStatus(listId="+listId+", publishErrorReason="+publishErrorReason+")");
       	}
    	try{
    		listDatabaseManager.setListPublishStatus(listId, null, ListBasicValue.PublishStatus.FAILURE, publishErrorReason);
    	} catch (DataAccessException ex) {
    		handleWrapAndRethrowException(PUBLISH_EXCEPTION, ex);
    	}
    }
    
    /**
     * Get a Judge by Id.
     * @param judgeId
     * @return
     * @throws  ListingsControllerException
     * 
     * @ejb.interface-method view-type="both"
     */
	public RefJudgeBasicValue getRefJudgeById(final Integer judgeId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getRefJudgeById(judgeId="+judgeId+")");
       	}
		try {
			return caseListingEntryHelper.getRefJudge(judgeId);
		} catch (FinderException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return null;

	}
	
	/**
	 * Get Defendants on Case By Case Id.
	 * @param caseId
	 * @return Collection<DefendantValue> of defendants
	 * @throws ListingsControllerException
	 * 
	 *  @ejb.interface-method view-type="both"
	 */
	public Collection<DefendantValue> getDefendantsByCaseId(final Integer caseId) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getDefendantsByCaseId(caseId="+caseId+")");
       	}
		try {
			return caseListingEntryHelper.getDefendants(caseId);
		} catch (CSBusinessException e) {
			handleWrapAndRethrowException(FIND_EXCEPTION, e);
		}
		return null;

	}

	/**
	 * Get the daily prison list xml.
	 * 
	 * @param listId
	 * @return string
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public java.lang.String getDailyPrisonList(Integer listId) {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getDailyPrisonList(listId="+listId+")");
       	}
		String xml = listDatabaseManager.getDailyPrisonList(listId);
		return xml;
	}

	/**
	 * Get the fixture count for court on or between from and to dates.
	 * 
	 * @param courtId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Integer getFixtureCount(Integer courtId, Date fromDate, Date toDate)
    {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getFixtureCount(courtId="+courtId+", fromDate="+fromDate+", toDate="+toDate+")");
       	}
		return listDatabaseManager.getFixtureCount(courtId, fromDate, toDate);
    }
	
	
	/**
	 * Get the list of case based on the provided filter criteria
	 * 
	 * @return
	 * @throws ListingsControllerException 
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public List<CaseFilterResultComplexValue> getCasesByFilter(final CaseListingFilterCriteria criteria) throws ListingsControllerException
    {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getCasesByFilter(criteria="+criteria+")");
       	}
		ListingsDatabaseManager dbMan = new ListingsDatabaseManager();
		List<CaseFilterResultComplexValue> results = new ArrayList<CaseFilterResultComplexValue>();
		List<CaseIdValue> caseIds;
		if (criteria.getCaseId() != null) {
			// This is a simple case Id search
			caseIds = new ArrayList<CaseIdValue>();
			caseIds.add(new CaseIdValue(criteria.getCaseId()));
		} else {			
			// This is a complex criteria search
			caseIds = (List<CaseIdValue>) dbMan.getCasesByFilter(
				criteria.getCourtId(), criteria.getCaseType(), criteria.getCaseClass(), criteria.getBcStatus(), 
				criteria.getHearingTypeCode(), criteria.getTimeEstFrom(), criteria.getTimeEstTo(),
				criteria.getUnits(), criteria.getRefJudgeTypeId(), criteria.getUnitsWeeks(),
				criteria.getSecureCourtRoom(),criteria.getJuvenileOnly());
		}
		for (CaseIdValue caseId : caseIds) {
			CaseFilterResultComplexValue result = new CaseFilterResultComplexValue();
			try {
				// Populate the caseComplexValue
				CaseComplexValue caseComplexValue = caseHelper.getCaseComplexValueByPrimaryKey(caseId.getCaseId());
				result.setCaseComplexValue(caseComplexValue);
				if (caseComplexValue != null) {
					// Populate the caseListingEntryComplexValue
					CaseListingEntryComplexValue caseListingEntryComplexValue = getCaseListingEntryByCaseIdAndCourtId(
							caseId.getCaseId(), result.getCaseComplexValue().getCourtID());
					result.setCaseListingEntryComplexValue(caseListingEntryComplexValue);
				}
				results.add(result);
			} catch (CSBusinessException e) {
				handleWrapAndRethrowException(FIND_EXCEPTION, e);
	    	} catch (Exception e) {
	    		handleWrapAndRethrowException(FIND_EXCEPTION, e);
			}
		}
		return results;
    }
	
	/*
	 * Take the passed in exception and wrap it as a ListingControllerException 
	 */
	private void handleWrapAndRethrowException(String errorKey, Exception ex) throws ListingsControllerException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: handleWrapAndRethrowException(errorKey="+errorKey+", ex="+ex+")");
       	}
		if (!errorKey.equals(FIND_EXCEPTION)) {
			ctx.setRollbackOnly();
		}
		CSServices.getDefaultErrorHandler().handleError(ex, getClass());
		throw new ListingsControllerException(errorKey, ex.getMessage(), ex);
	}
}
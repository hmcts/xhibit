package uk.gov.courtservice.xhibit.business.services.listing;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCase;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseDiaryFixture;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseDiaryFixtureMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntry;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.FixtureDeftAttending;
import uk.gov.courtservice.xhibit.business.entities.listing.FixtureDeftAttendingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingType;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingTypeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingData;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingDataMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.AbstractHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseDiaryFixtureValue;

/**
 * <p>
 * Title: CaseDiaryFixtureHelper
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
 
public class CaseDiaryFixtureHelper extends AbstractHelper {
	private static final Logger LOG = CSServices.getLogger(CaseDiaryFixtureHelper.class);

	private CaseDiaryFixtureMaintainer caseDiaryFixtureMaintainer;
	private RefHearingTypeMaintainer refHearingTypeMaintainer;
	private RefListingDataMaintainer refListingDataMaintainer;
	private FixtureDeftAttendingMaintainer fixtureDeftAttendingMaintainer;
	private CaseListingEntryMaintainer caseListingEntryMaintainer ;
	private CaseMaintainer caseMaintainer; 
	private DirectionsForCaseMaintainer directionsForCaseMaintainer;

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public CaseDiaryFixtureHelper() {
		caseDiaryFixtureMaintainer = new CaseDiaryFixtureMaintainer();
		refHearingTypeMaintainer = new RefHearingTypeMaintainer();
		refListingDataMaintainer = new RefListingDataMaintainer();
		fixtureDeftAttendingMaintainer = new FixtureDeftAttendingMaintainer();
		caseListingEntryMaintainer = new CaseListingEntryMaintainer();
		caseMaintainer = new CaseMaintainer();
		directionsForCaseMaintainer = new DirectionsForCaseMaintainer();
	}

	/**
	 * Description: get Case Diary Fixture by case listing id and status
	 * 
	 * @param Integer
	 *            caseListingEntryId
	 * @return Collection
	 * @throws FinderException
	 */
	public Collection<CaseDiaryFixtureComplexValue> getCaseDiaryFixturesByCaseListingIdAndStatus(Integer caseListingEntryId, String status)
			throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: getCaseDiaryFixturesByCaseListingIdAndStatus(caseListingEntryId="+caseListingEntryId+", status="+status+")");
       	}
		try {
			Collection fixtures = caseDiaryFixtureMaintainer.findByCaseListingEntryIdAndStatus(caseListingEntryId,
					status);
			Collection<CaseDiaryFixtureComplexValue> results = caseDiaryFixtureMaintainer.getComplexValues(fixtures);
			for (CaseDiaryFixtureComplexValue fixture : results) {
				populateComplexValue(fixture);
			}
			return results;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}

	/**
	 * Description: get pending Case Diary Fixture by case listing id and status
	 * 
	 * @param Integer
	 *            caseListingEntryId
	 * @param String
	 *            status
	 * @return Collection
	 * @throws FinderException
	 */
	public Collection<CaseDiaryFixtureComplexValue> getPendingCaseDiaryFixturesByCaseListingIdAndStatus(Integer caseListingEntryId, String status)
			throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: getPendingCaseDiaryFixturesByCaseListingIdAndStatus(caseListingEntryId="+caseListingEntryId+", status="+status+")");
       	}
		try {
			Collection fixtures = caseDiaryFixtureMaintainer.findByCaseListingEntryIdAndStatusAndListingDate(caseListingEntryId,
					status, DateTimeUtilities.stripTimeToUtilDate(new Date()));
			Collection<CaseDiaryFixtureComplexValue> results = caseDiaryFixtureMaintainer.getComplexValues(fixtures);
			for (CaseDiaryFixtureComplexValue fixture : results) {
				populateComplexValue(fixture);
			}
			return results;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}
	
    /**
	 * Description: get Case Diary Fixture by Date
	 * @param dateOfFixture The date you want fixtures for.
	 * @param courtId The court Id of the fixture.
	 * @return A collection of CaseDiaryFixtureComplexValue
	 * @throws FinderException
	 */
	public Collection<CaseDiaryFixtureComplexValue> findByListingDateAndCourtId(final Date dateOfFixture, final Integer courtId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: findByListingDateAndCourtId(dateOfFixture="+dateOfFixture+", courtId="+courtId+")");
       	}
		try {
			Collection fixtures = caseDiaryFixtureMaintainer.findByListingDateAndCourtId(dateOfFixture, courtId);
			Collection<CaseDiaryFixtureComplexValue> results = caseDiaryFixtureMaintainer.getComplexValues(fixtures);
			for (CaseDiaryFixtureComplexValue fixture : results) {
				populateComplexValue(fixture);
			}
			return results;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}
	
	/**
	 * Description: Collection of case diary fixtures for caseId and dates
	 * with the defendantOnCaseId marked as attending on the fixtures
	 * 
	 * @param caseId
	 * @param fromDate
	 * @param toDate
	 * @param defendantOnCaseId
	 * @return collection of CaseDiaryFixtureComplexValue
	 * @throws FinderException 
	 */
	public Collection<CaseDiaryFixtureComplexValue> findByCaseIdAndListingDateAndDefendantOnCaseId(Integer caseId, Date fromDate, Date toDate, Integer defendantOnCaseId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: findByCaseIdAndListingDateAndDefendantOnCaseId(caseId="+caseId+", fromDate="+fromDate+", toDate="+toDate+", defendantOnCaseId="+defendantOnCaseId+")");
       	}
		try {
			Collection fixtures = caseDiaryFixtureMaintainer.findByCaseIdAndListingDateAndDefendantOnCaseId(caseId, fromDate, toDate, defendantOnCaseId);
			Collection<CaseDiaryFixtureComplexValue> results = caseDiaryFixtureMaintainer.getComplexValues(fixtures);
			for (CaseDiaryFixtureComplexValue fixture : results) {
				populateComplexValue(fixture);
			}
			return results;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}
	
	/**
	 * Description: Return true if case diary fixtures exist for caseId and dates
	 * 
	 * @param caseId
	 * @param fromDate
	 * @param toDate
	 * @return true if case diary fixtures exist
	 * @throws FinderException 
	 */
	public Boolean isCaseDiaryFixture(Integer caseId, Date fromDate, Date toDate) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: isCaseDiaryFixture(caseId="+caseId+", fromDate="+fromDate+", toDate="+toDate+")");
       	}
		try {
			Collection locals = caseDiaryFixtureMaintainer.findByCaseIdAndListingDate(caseId, fromDate, toDate);
			return !locals.isEmpty();
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}

	/**
	 * Populate the case diary fixture complex value.
	 * 
	 * @param fixture
	 * @throws FinderException
	 */
	private void populateComplexValue (CaseDiaryFixtureComplexValue fixture)
			throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: populateComplexValue(fixture="+fixture+")");
       	}
		// Get the hearing type
		if (fixture.getHearingTypeId() != null) {
			RefHearingTypeBasicValue refHearingType = getHearingTypeByPrimaryKey(fixture.getHearingTypeId());
			fixture.setRefHearingType(refHearingType);
		}
		
		// Get the predefined list note
		RefListingDataBasicValue preDefinedlistNote = null;
		if (fixture.getListNotePreDefinedId() != null) {
			preDefinedlistNote = getPredefinedNoteByPrimaryKey(fixture.getListNotePreDefinedId());
		}
		fixture.setPreDefinedlistNote(preDefinedlistNote);
		
		// Get the defendants attending the fixture
		fixture.setFixtureDeftAttending( getFixtureDeftAttending(fixture.getCaseDiaryFixtureId()) );

		// Get the case
		final CaseListingEntry caseListingEntry = caseListingEntryMaintainer
				.findByPrimaryKey(fixture.getCaseListingEntryId());
		fixture.setCase(caseMaintainer.getCaseBasicValue(caseMaintainer.findByPrimaryKey(caseListingEntry.getCaseId())));

		// Get the directions for case which may not exist
		DirectionsForCaseBasicValue directionsForCase;
		try {
			DirectionsForCase dfc = directionsForCaseMaintainer.findByCaseId(caseListingEntry.getCaseId());
			directionsForCase = directionsForCaseMaintainer.getBasicValue(dfc);
		} catch (ObjectNotFoundException e) {
			directionsForCase = null;
		}
		fixture.setDirectionsForCase(directionsForCase);
	}

    /**
	 * Description: Find fixtures by caseEntryId, startDate and endDate
	 * 
	 * @param caseEntryId
	 * @param startDate
	 * @param endDate
	 * @throws FinderException
	 */
	@SuppressWarnings("unchecked")
	public Collection findCaseDiaryFixturesImpacted(Integer caseEntryId, Date startDate, Date endDate, Date currentDate) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: findCaseDiaryFixturesImpacted(caseEntryId="+caseEntryId+", startDate="+startDate+", endDate="+endDate+", currentDate="+currentDate+")");
       	}
		try {
			Collection results = newCollection();
			Collection locals = caseDiaryFixtureMaintainer.findCaseDiaryFixturesImpacted(caseEntryId, startDate, endDate, currentDate);
			for (CaseDiaryFixture local : (Collection<CaseDiaryFixture>) locals) {
				CaseDiaryFixtureBasicValue value = caseDiaryFixtureMaintainer.getBasicValue(local);
				results.add(value);
			}
			return results;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}
    
    /**
	 * Description: Find fixture by id
	 * 
	 * @param id
	 * @throws FinderException
	 */
	public CaseDiaryFixtureComplexValue findByPrimaryKey(Integer id) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: findByPrimaryKey(id="+id+")");
       	}
		CaseDiaryFixtureComplexValue result = null;
		try {
			CaseDiaryFixture local = caseDiaryFixtureMaintainer.findByPrimaryKey(id);
			result = caseDiaryFixtureMaintainer.getComplexValue(local);
			populateComplexValue(result);
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
		return result;
	}
	
	/**
	 * Description: Save Case Diary Fixture
	 * 
	 * @param CaseDiaryFixtureValue
	 * @throws OptimisticLockException,
	 *             CreateException, FinderException
	 */
	public Integer saveCaseDiaryFixture(CaseDiaryFixtureValue caseDiaryFixtureValue, String userDisplayName)
			throws OptimisticLockException, CreateException, FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: saveCaseDiaryFixture(caseDiaryFixtureValue="+caseDiaryFixtureValue+", userDisplayName="+userDisplayName+")");
       	}
		Integer result = null;
		try {
			CaseDiaryFixtureComplexValue fixtureValue = caseDiaryFixtureValue.getFixture();
			
			// Create / Update fixture
			if (fixtureValue.getId() == null) {
				// Create the case listing (if it does not exist)
				if (fixtureValue.getCaseListingEntryId() == null) {
					Integer caseListingEntryId = saveCaseListingEntryBasicValue(caseDiaryFixtureValue.getCourtId(), caseDiaryFixtureValue.getCaseId(), userDisplayName);
					fixtureValue.setCaseListingEntryId(caseListingEntryId);
				}
				CaseDiaryFixture local = (CaseDiaryFixture) caseDiaryFixtureMaintainer.create(fixtureValue, userDisplayName);
				result = local.getCaseDiaryFixtureId();
			} else {
				caseDiaryFixtureMaintainer.update(fixtureValue, userDisplayName);
				result = fixtureValue.getCaseDiaryFixtureId();
			}
		    
			// Update FixtureDeftAttendingBasicValue records
			if (!fixtureValue.getFixtureDeftAttending().isEmpty()) {
				for (FixtureDeftAttendingBasicValue fda : fixtureValue.getFixtureDeftAttending() ) {
					if ( fda.getCaseDiaryFixtureId() == null ) {
						// Set the Fixture Id where not present
						fda.setCaseDiaryFixtureId(result);
					}
					
					if ( fda.getFixtureDeftAttendingId() != null ) {
						// Existing FixtureDeftAttendingBasicValue - update record
						fixtureDeftAttendingMaintainer.update(fda, userDisplayName);
					}
					else {
						// New FixtureDeftAttendingBasicValue - create record
						fixtureDeftAttendingMaintainer.create(fda, userDisplayName);
					}
				}
			}
			return result;
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
	
	private Integer saveCaseListingEntryBasicValue(Integer courtId, Integer caseId, String userDisplayName ) {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: saveCaseListingEntryBasicValue(courtId="+courtId+", caseId="+caseId+", userDisplayName="+userDisplayName+")");
       	}
		Integer result = null;
		try{
			CaseListingEntryBasicValue caseListingEntryBasicValue = new CaseListingEntryBasicValue();
			caseListingEntryBasicValue.setCaseId(caseId);
			caseListingEntryBasicValue.setCourtId(courtId);
			
			CaseListingEntry local = (CaseListingEntry) caseListingEntryMaintainer.create(caseListingEntryBasicValue, userDisplayName);
			result = local.getCaseListingEntryId();
		} catch (EJBException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
		return result;	
	}

	/**
	 * Description: Delete Case Diary Fixture and any child records (mark as obsolete)
	 * 
	 * @param CaseDiaryFixtureBasicValue
	 * @throws FinderException
	 */
	public void deleteCaseDiaryFixture(CaseDiaryFixtureComplexValue complexValue, String userDisplayName) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: deleteCaseDiaryFixture(complexValue="+complexValue+", userDisplayName="+userDisplayName+")");
       	}
		try {
			// Mark all FixtureDeftAttendingBasicValue records as obsolete
			if (!complexValue.getFixtureDeftAttending().isEmpty()) {
				for (FixtureDeftAttendingBasicValue fda : complexValue.getFixtureDeftAttending() ) {
					fixtureDeftAttendingMaintainer.delete(fda, userDisplayName);
				}
			}
			
			// Mark the fixture as obsolete
			caseDiaryFixtureMaintainer.delete(complexValue, userDisplayName);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}

	private RefHearingTypeBasicValue getHearingTypeByPrimaryKey(Integer refHearingTypeId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: getHearingTypeByPrimaryKey(refHearingTypeId="+refHearingTypeId+")");
       	}
		try {
			RefHearingType rht = refHearingTypeMaintainer.findByPrimaryKey(refHearingTypeId);
			RefHearingTypeBasicValue result = refHearingTypeMaintainer.getBasicValue(rht);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
	}

	private RefListingDataBasicValue getPredefinedNoteByPrimaryKey(Integer id) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: getPredefinedNoteByPrimaryKey(id="+id+")");
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

	private Collection<FixtureDeftAttendingBasicValue> getFixtureDeftAttending(Integer caseDiaryFixtureId) throws FinderException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("START: getFixtureDeftAttending(caseDiaryFixtureId="+caseDiaryFixtureId+")");
       	}
		try {
			@SuppressWarnings("unchecked")
			Collection<FixtureDeftAttending> locals = fixtureDeftAttendingMaintainer
					.findByCaseDiaryFixtureId(caseDiaryFixtureId);
			Collection<FixtureDeftAttendingBasicValue> result = fixtureDeftAttendingMaintainer.getBasicValues(locals);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, ListingsControllerBean.class);
			throw ex;
		}
	}
}
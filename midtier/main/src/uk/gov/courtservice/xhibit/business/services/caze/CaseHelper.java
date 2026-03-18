package uk.gov.courtservice.xhibit.business.services.caze;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseHome;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCase;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntry;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingType;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingTypeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerLocal;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: CaseHelper
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
 * 
 * @author Mark Groen
 * @version 1.0
 * 
 * @history 25/05/2018 John Uphill updates to getCaseComplexValueByPrimaryKey
 */
public class CaseHelper {
	private static final Logger LOG = CSServices.getLogger(CaseHelper.class);
	
	private CaseMaintainer caseMaintainer;
	private HearingMaintainer hearingMaintainer;
	private RefHearingTypeMaintainer refHearingTypeMaintainer;
	private DirectionsForCaseMaintainer directionsForCaseMaintainer;
	private DefendantOnCaseMaintainer defendantOnCaseMaintainer;
	private CaseListingEntryMaintainer caseListingEntryMaintainer;
	
	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public CaseHelper() {
		caseListingEntryMaintainer = new CaseListingEntryMaintainer();
		defendantOnCaseMaintainer = new DefendantOnCaseMaintainer();
		directionsForCaseMaintainer = new DirectionsForCaseMaintainer();
		refHearingTypeMaintainer = new RefHearingTypeMaintainer();
		hearingMaintainer = new HearingMaintainer();
		caseMaintainer = new CaseMaintainer();		
	}
	
	/**
	 * Gets defendant value objects for a given case
	 * 
	 * @param caseID
	 *            The case id
	 * @return collection of defendant value objects
	 * @throws CaseControllerException
	 * @throws DefendantControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection getDefendants(Integer caseID) throws CaseControllerException, DefendantControllerException {
		
		ArrayList defendantValues = new ArrayList();

		// Find the defendants by caseId and populate them into collection -
		// defendantValues
		try {
			Context ctx = new InitialContext();
			DefendantOnCaseHome home = (DefendantOnCaseHome) ctx.lookup("DefendantOnCaseHome");
			Collection defendantBeans = home.findByCaseId(caseID);
			Iterator defendantIterator = null;
			defendantIterator = defendantBeans.iterator();

			DefendantControllerLocal defControllerBean = (DefendantControllerLocal) CSServices.getEJBServices()
					.createLocalSession(DefendantControllerLocalHome.class);

			while (defendantIterator.hasNext()) {
				DefendantOnCase defendantOnCase = (DefendantOnCase) defendantIterator.next();
				DefendantValue defendantValue = defControllerBean.getDefendantDetails(
						defendantOnCase.getDefendant().getDefendantId(), defendantOnCase.getCaseId());
				defendantValues.add(defendantValue);
			}

		} catch (NamingException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CaseControllerBean.class);
			throw new CSUnrecoverableException("Could not find DefendantOnCaseHome", e);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CaseControllerBean.class);
			throw new CSUnrecoverableException(e);
		}

		return defendantValues;
	}
	
	public CaseComplexValue getCaseComplexValueByPrimaryKey(Integer caseId) throws CaseControllerException {
		final String methodName = "getCaseComplexValueByPrimaryKey(" + caseId + ")";
		LOG.debug(methodName + " entered");

		try {
			Case local = caseMaintainer.findByPrimaryKey(caseId);
			CaseComplexValue result = caseMaintainer.getCaseComplexValue(local);
			if (result.getDefaultHearingType() != null) {
				RefHearingTypeBasicValue refHearingType = getHearingTypeByPrimaryKey(result.getDefaultHearingType());
				result.setDefaultHearingTypeBasicValue(refHearingType);
			}
			
			// Get the directions for case which may not exist
			DirectionsForCaseBasicValue directionsForCase;
			try {
				DirectionsForCase dfc = directionsForCaseMaintainer.findByCaseId(caseId);
				directionsForCase = directionsForCaseMaintainer.getBasicValue(dfc);
			} catch (ObjectNotFoundException e) {
				directionsForCase = null;
			}
			result.setDirectionsForCase(directionsForCase);

			// Get the defendants on case
            Collection defendantOnCases = defendantOnCaseMaintainer.findByCaseId(caseId);
            @SuppressWarnings("unchecked")
			Collection<DefendantOnCaseBasicValue> defendantOnCaseValues =
						defendantOnCaseMaintainer.getDefendantOnCaseBasicValues(defendantOnCases);
			result.setDefendantOnCases(defendantOnCaseValues);

			// Get the case listing entry which may not exist
			CaseListingEntryBasicValue caseListingEntry;
			try {
	        	CaseListingEntry cle = caseListingEntryMaintainer.findByCaseId(caseId);
	        	caseListingEntry = caseListingEntryMaintainer.getBasicValue(cle);
			} catch (ObjectNotFoundException ex) {
				caseListingEntry = null;
			}
			result.setCaseListingEntry(caseListingEntry);
			
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, CaseControllerBean.class);
			throw new EJBException(ex);
		}
	}
	
	public Collection<ScheduledHearingValue> findScheduledHearingsByCaseId(Integer caseId) throws ObjectNotFoundException, NamingException {
		final String methodName = "findScheduledHearingsByCaseId(" + caseId + ")";
		LOG.debug(methodName + " entered");
		
		Collection<ScheduledHearingValue> scheduledHearingValues = new ArrayList<ScheduledHearingValue>();
		Collection<ScheduledHearing> scheduledHearings = hearingMaintainer.findScheduledByCaseId(caseId);
		for (ScheduledHearing scheduledHearing : scheduledHearings) {
			ScheduledHearingValue scheduledHearingValue = new ScheduledHearingValue();
			scheduledHearingValue.setScheduledHearingID(scheduledHearing.getScheduledHearingId());
			scheduledHearingValue.setCaseActive(scheduledHearing.getIsCaseActive());
			// Convert Timestamp to Calendar
			// bug fix for null timestamp value
			Calendar schedHearingDate = null;
			Timestamp ts = scheduledHearing.getSitting().getHearingList().getStartDate();

			if (ts != null) {
				schedHearingDate = Calendar.getInstance();
				schedHearingDate.setTime(ts);
			}

			scheduledHearingValue.setScheduledHearingDate(schedHearingDate);
			if (LOG.isDebugEnabled()) {
				LOG.debug("adding scheduled hearing value=" + scheduledHearingValue);
			}
			scheduledHearingValues.add(scheduledHearingValue);
		}
		return scheduledHearingValues;
	}

	private RefHearingTypeBasicValue getHearingTypeByPrimaryKey(Integer refHearingTypeId)
			throws CaseControllerException {
		final String methodName = "getHearingTypeByPrimaryKey(" + refHearingTypeId.toString() + ")";
		LOG.debug(methodName + " entered");

		try {
			RefHearingType rht = refHearingTypeMaintainer.findByPrimaryKey(refHearingTypeId);
			RefHearingTypeBasicValue result = refHearingTypeMaintainer.getBasicValue(rht);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, CaseControllerBean.class);
			throw new EJBException(ex);
		}
	}
	
}

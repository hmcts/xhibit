package uk.gov.courtservice.xhibit.business.services.defendant;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailService;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.address.AddressHome;
import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantHome;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReference;
import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReferenceHome;
import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReferenceMaintainer;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeCourtRoomUnknownException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caze.SchedHearingLocationValue;
import uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseStatusValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.UpdateCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: DefendantHelper
 * </p>
 * <p>
 * Description: Provides and abstract layer between the session facade and the
 * maintainer class. It is used to construct the necessary value objects and
 * contains any business logic.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version 1.0
 */
public class DefendantHelper {
	private static final Logger LOG = CSServices.getLogger(DefendantHelper.class);

	private static final String DEF_NOT_FOUND = "defendantcontroller.defnotfound";

	private static final String DEF_ON_CASE_NOT_FOUND = "defendantcontroller.defoncasenotfound";

	private static final Integer COURT_LOG_EVENT_UPDATE_DEF = new Integer(40300);

	private DefendantOnCaseMaintainer defOnCaseMaintainer;

	private DefendantReferenceMaintainer defRefMaintainer;

	private AddressMaintainer addressMaintainer;

	private String methodName;

	private CaseControllerLocal caseController;

	private CourtSiteMaintainer courtSiteMaintainer;
	
	private DefendantMaintainer defMaintainer;

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public DefendantHelper() {
		defOnCaseMaintainer = new DefendantOnCaseMaintainer();
		courtSiteMaintainer = new CourtSiteMaintainer();
		defMaintainer = new DefendantMaintainer();
		defRefMaintainer = new DefendantReferenceMaintainer();
		addressMaintainer = new AddressMaintainer();
	}

	/**
	 * Description: Added for CCN400 Used to retreive defendnat on case details
	 * by defendantOnCaseId
	 * 
	 * @param defendantOnCaseId
	 * @return DefendantOnCase
	 */
	public DefendantOnCase getDefendantOnCaseDetails(Integer defendantOnCaseId) {
		DefendantOnCase defOnCase = null;

		try {
			defOnCase = defOnCaseMaintainer.findByPrimaryKey(defendantOnCaseId);

		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
		}

		return defOnCase;
	}

	/**
	 * This method constructs the client VO by finding the defendant details and
	 * finding the defendantOnCase details.
	 * 
	 * @param defId
	 *            the current defendant ID
	 * @param caseId
	 *            the current case ID
	 * @return DefendantValue Full DefendantValue Object
	 * @throws DefendantControllerException
	 *             custom exception
	 */
	public DefendantValue getDefendantDetailsWithAddress(Integer defId, Integer caseId)
			throws DefendantControllerException {
		methodName = "getDefendantDetailsWithAddress(" + defId + ", " + caseId + ")";
		LOG.debug(methodName + " called");

		// Populate the DefendantValue object
		DefendantValue defendantValue = getDefendantDetails(defId, caseId);

		// Find the address record for this defendant
		Integer addressID = defendantValue.getAddressId();
		if (addressID != null) {
			try {
				Address addressBean = (Address) CSServices.getEJBServices()
						.findLocalEntityByPrimaryKey(AddressHome.class, addressID);
				AddressValue addressValue = new AddressValue(addressBean.getAddressId(), addressBean.getAddress1(),
						addressBean.getAddress2(), addressBean.getAddress3(), addressBean.getAddress4(),
						addressBean.getTown(), addressBean.getCounty(), addressBean.getPostcode(),
						addressBean.getCountry());
				addressValue.setUpdateCount(addressBean.getVersion().intValue());
				defendantValue.setAddressValue(addressValue);
			} catch (ObjectNotFoundException e) {
				// this is an unexpected exception, the address id was
				// retrieved as part of the defendant details so there should
				// be an entity for this id
				CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
				throw new CSUnrecoverableException("Could not find an address entity "
						+ "from an id retrieved as part of " + "a defendant entity ", e);
			}
		}

		// defendantOnCaseDetails
		try {
			DefendantOnCaseBasicValue defOnCaseBasicValue = defOnCaseMaintainer
					.getDefendantOnCaseBasicValue(defOnCaseMaintainer.findByDefendantAndCase(defId, caseId));
			defendantValue.setDefOnCaseBasicValue(defOnCaseBasicValue);

		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
			throw new DefendantControllerException(DEF_ON_CASE_NOT_FOUND,
					"Defendant " + defId + ", on case " + caseId + " not found", e);
		}

		LOG.debug(methodName + "returning");
		return defendantValue;
	}

	/**
	 * This method constructs the client VO by finding the defendant details and
	 * finding the defendantOnCase details.
	 * 
	 * @param defId
	 *            the current defendant ID
	 * @param caseId
	 *            the current case ID
	 * @return DefendantValue Full DefendantValue Object
	 * @throws DefendantControllerException
	 *             custom exception
	 */
	public DefendantValue getDefendantDetails(Integer defId, Integer caseId) throws DefendantControllerException {
		return getDefendantDetails(defId, caseId, false);
	}

	/**
	 * This method constructs the client VO by finding the defendant details and
	 * finding the defendantOnCase details.
	 * 
	 * @param defId
	 *            the current defendant ID
	 * @param caseId
	 *            the current case ID
	 * @param includeObsolete
	 *            whether to find defendants that have been marked obsolete
	 * @return DefendantValue Full DefendantValue Object
	 * @throws DefendantControllerException
	 *             custom exception
	 */
	public DefendantValue getDefendantDetails(Integer defId, Integer caseId, boolean includeObsolete)
			throws DefendantControllerException {
		methodName = "getDefendantDetails(" + defId + ", " + caseId + ")";
		LOG.debug(methodName + " called");

		// defendantDetails
		Defendant defBean = getDefendant(defId);

		// Convert Timestamp dob and last conv. date to Calendar
		Calendar dateOfBirth = Calendar.getInstance();
		if (defBean.getDateOfBirth() != null) {
			dateOfBirth.setTime(defBean.getDateOfBirth());
		} else {
			dateOfBirth = null;
		}

		Calendar lastConvictionDate = Calendar.getInstance();
		if (defBean.getLastConvictionDate() != null) {
			lastConvictionDate.setTime(defBean.getLastConvictionDate());
		} else {
			lastConvictionDate = null;
		}

		// Populate the DefendantValue object
		DefendantValue defendantValue = new DefendantValue(defBean.getDefendantId(), defBean.getCrestDefendantId(),
				defBean.getFirstName(), defBean.getMiddleName(), defBean.getSurname(), defBean.getInitials(),
				dateOfBirth, defBean.getGender(), lastConvictionDate, defBean.getCourtId(),
				defBean.getCurrentPrisonStatus(), defBean.getPrisonId());
		defendantValue.setUpdateCount(defBean.getVersion().intValue());
		defendantValue.setAddressId(defBean.getAddressId());
		if (defBean.getPublicDisplayHide() != null) {
			defendantValue.setHideDefendantInCallCases(defBean.getPublicDisplayHide().equals("Y"));
		} else {
			defendantValue.setHideDefendantInCallCases(false);
		}

		// defendantOnCaseDetails
		try {
			if (includeObsolete) {
				DefendantOnCaseBasicValue defOnCaseBasicValue = getDefendantOnCaseDetailsIncludeObsolete(defId, caseId);
				defendantValue.setDefOnCaseBasicValue(defOnCaseBasicValue);
			} else {
				DefendantOnCaseBasicValue defOnCaseBasicValue = defOnCaseMaintainer
						.getDefendantOnCaseBasicValue(defOnCaseMaintainer.findByDefendantAndCase(defId, caseId));
				defendantValue.setDefOnCaseBasicValue(defOnCaseBasicValue);
			}
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
			throw new DefendantControllerException(DEF_ON_CASE_NOT_FOUND,
					"Defendant " + defId + ", on case " + caseId + " not found", e);
		}

		LOG.debug(methodName + "returning");
		return defendantValue;
	}

	public Collection findDefendantByCourtIdSurnameGenderFirstNamePrisonerNumber(Integer courtId, String firstName,
			String surname, Integer gender, String prisonerNumber) throws ObjectNotFoundException {
		Collection<DefendantValue> defComplex = new ArrayList<DefendantValue>();
		Collection defs = defMaintainer.findDefendantByCourtIdSurnameGenderFirstName(courtId, firstName, surname,
				gender);
		HashMap<Integer, Defendant> defIds = new HashMap<Integer, Defendant>();
		Iterator iter = defs.iterator();
		while (iter.hasNext()) {
			Defendant def = (Defendant) iter.next();
			defIds.put(def.getDefendantId(), def);
			if (prisonerNumber == null || prisonerNumber.equals("")) {
				DefendantValue val = getDefValue(def);
				defComplex.add(val);
			}
		}
		// if all of the other search criterias are null
		if ((firstName == null || firstName.equals("")) && ((surname == null || surname.equals("")) && gender > 0)) {
			Collection<DefendantValue> defComplexWithPris = new ArrayList<DefendantValue>();
		
			Collection<DefendantReference> refColl = new ArrayList<DefendantReference>();
			refColl = defRefMaintainer.findByRefNameValue("Prisoner Number", prisonerNumber);
			Iterator iter2 = refColl.iterator();
			while (iter2.hasNext()) {
				DefendantReference ref = (DefendantReference) iter2.next();
				Defendant def = defMaintainer.findByPrimaryKey(ref.getDefendantId());
				defComplexWithPris.add(getDefValue(def));
			}
			defComplex = defComplexWithPris;
		}
		// means we have entries in first array
		else if (defIds.size() > 0) {
			// filter by prisoner Number
			if (prisonerNumber != null && !prisonerNumber.equals("")) {
				Collection<DefendantValue> defComplexWithPris = new ArrayList<DefendantValue>();

				Collection<DefendantReference> refColl = new ArrayList<DefendantReference>();
				refColl = defRefMaintainer.findByRefNameValue("Prisoner Number", prisonerNumber);
				Iterator iter2 = refColl.iterator();
				while (iter2.hasNext()) {
					DefendantReference ref = (DefendantReference) iter2.next();
					Integer def = ref.getDefendantId();
					if (defIds.containsKey(def)) {
						defComplexWithPris.add(getDefValue(defIds.get(def)));
					}
				}
				defComplex = defComplexWithPris;
			}
		}
		return defComplex;
	}

	public Collection findDefendantByCourtIdSurnameFirstNamePrisonerNumber(Integer courtId, String firstName,
			String surname, String prisonerNumber) throws ObjectNotFoundException {
		Collection<DefendantValue> defComplex = new ArrayList<DefendantValue>();
		HashMap<Integer, Defendant> defIds = new HashMap<Integer, Defendant>();
		Collection defs = defMaintainer.findDefendantByCourtIdFirstNameSurname(courtId, firstName, surname);
		Iterator iter = defs.iterator();
		while (iter.hasNext()) {
			Defendant def = (Defendant) iter.next();
			defIds.put(def.getDefendantId(), def);
			if (prisonerNumber == null || prisonerNumber.equals("")) {
				DefendantValue val = getDefValue(def);
				defComplex.add(val);
			}

		}
		// if all of the other search criterias are null
		if ((firstName == null || firstName.equals("")) && ((surname == null || surname.equals("")))) {
			Collection<DefendantValue> defComplexWithPris = new ArrayList<DefendantValue>();
			
			Collection<DefendantReference> refColl = new ArrayList<DefendantReference>();
			refColl = defRefMaintainer.findByRefNameValue("Prisoner Number", prisonerNumber);
			Iterator iter2 = refColl.iterator();
			while (iter2.hasNext()) {
				DefendantReference ref = (DefendantReference) iter2.next();
				Defendant def = defMaintainer.findByPrimaryKey(ref.getDefendantId());
				defComplexWithPris.add(getDefValue(def));
			}
			
			defComplex = defComplexWithPris;
		}
		// means we have entries in first array
		else if (defIds.size() > 0) {
			// filter by prisoner Number
			if (prisonerNumber != null && !prisonerNumber.equals("")) {
				Collection<DefendantValue> defComplexWithPris = new ArrayList<DefendantValue>();
				
				Collection<DefendantReference> refColl = new ArrayList<DefendantReference>();
				refColl = defRefMaintainer.findByRefNameValue("Prisoner Number", prisonerNumber);
				Iterator iter2 = refColl.iterator();
				while (iter2.hasNext()) {
					DefendantReference ref = (DefendantReference) iter2.next();
					Integer def = ref.getDefendantId();
					if (defIds.containsKey(def)) {
						defComplexWithPris.add(getDefValue(defIds.get(def)));
					}
				}
				defComplex = defComplexWithPris;
			}
		}
		return defComplex;
	}

	public Collection findDefendantByCourtIdSurname(Integer courtId, String surname)  {
		Collection<DefendantValue> defComplex = new ArrayList<DefendantValue>();
		Collection defs = defMaintainer.findDefendantByCourtIdSurname(courtId, surname);
		Iterator iter = defs.iterator();
		while (iter.hasNext()) {
			Defendant def = (Defendant) iter.next();
			DefendantValue val = getDefValue(def);
			defComplex.add(val);
		}
		return defComplex;
	}
	
	public Collection findDefendantByCourtIdFirstNameSurname(Integer courtId, String firstName, String surname)  {
		Collection<DefendantValue> defComplex = new ArrayList<DefendantValue>();
		Collection defs = defMaintainer.findDefendantByCourtIdFirstNameSurname(courtId, firstName, surname);
		Iterator iter = defs.iterator();
		while (iter.hasNext()) {
			Defendant def = (Defendant) iter.next();
			DefendantValue val = getDefValue(def);
			defComplex.add(val);
		}
		return defComplex;
	}

	/*
	 * getDefendantOnCaseDetailsIncludeObsolete
	 */
	private DefendantOnCaseBasicValue getDefendantOnCaseDetailsIncludeObsolete(Integer defId, Integer caseId)
			throws ObjectNotFoundException {

		Collection defs = defOnCaseMaintainer.findByDefendantAndCaseIncludeObsolete(defId, caseId);

		// Validate
		{
			int nonObsoleteCount = 0;
			Iterator iter = defs.iterator();
			while (iter.hasNext()) {
				DefendantOnCase doc = (DefendantOnCase) iter.next();
				if (!isObsolete(doc)) {
					nonObsoleteCount++;
					if (nonObsoleteCount > 1) {
						throw new ObjectNotFoundException("Defendant " + defId + ", on case " + caseId
								+ " matches multiple defendant_on_case entries");
					}
				}
			}
		}

		// Choose the first non obsolete DOC
		{
			Iterator iter = defs.iterator();
			while (iter.hasNext()) {
				DefendantOnCase doc = (DefendantOnCase) iter.next();
				if (!isObsolete(doc)) {
					DefendantOnCaseBasicValue defOnCaseBasicValue = defOnCaseMaintainer
							.getDefendantOnCaseBasicValue(doc);
					return defOnCaseBasicValue;
				}
			}
		}

		// Else choose the first obsolete DOC
		{
			Iterator iter = defs.iterator();
			while (iter.hasNext()) {
				DefendantOnCase doc = (DefendantOnCase) iter.next();

				DefendantOnCaseBasicValue defOnCaseBasicValue = defOnCaseMaintainer.getDefendantOnCaseBasicValue(doc);
				return defOnCaseBasicValue;
			}
		}

		// Else the collection is empty
		throw new ObjectNotFoundException("Empty collection returned from findByDefendantAndCaseIncludeObsolete");
	}

	private boolean isObsolete(DefendantOnCase doc) {
		return (doc.getObsInd() != null && doc.getObsInd().equals("Y"));
	}

	/**
	 * Get the Defendant for the given defendant id
	 * 
	 * @param defId
	 *            The defendant id
	 * @return Defendant
	 * @throws DefendantControllerException
	 */
	private Defendant getDefendant(Integer defId) throws DefendantControllerException {

		// Find the defendant record for this defendant ID
		try {
			return (Defendant) CSServices.getEJBServices().findLocalEntityByPrimaryKey(DefendantHome.class, defId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
			throw new DefendantControllerException(DEF_NOT_FOUND, "Defendant " + defId + " not found", e);
		}
	}

	/**
	 * Get the defendant on case for the given defendant id and case id
	 * 
	 * @param defendantId
	 * @param caseId
	 * @return
	 * @throws DefendantControllerException
	 */
	private DefendantOnCase getDefendantOnCase(Integer defendantId, Integer caseId)
			throws DefendantControllerException {
		try {

			return defOnCaseMaintainer.findByDefendantAndCase(defendantId, caseId);

		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, DefendantControllerBean.class);
			throw new DefendantControllerException(DEF_ON_CASE_NOT_FOUND,
					"Defendant on case not found, defid=" + defendantId + " caseid=" + caseId, ex);
		}
	}

	public void createCourtLogEntry(DefendantValue defendant, CaseStatusValue caseStatusValue)
			throws CourtLogBusinessException {
		CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

		// set the value which notifies if the case is active in court
		logEntry.setInCourt(caseStatusValue.isInCourt());
		logEntry.setCaseId(caseStatusValue.getCaseID());

		logEntry.setEventType(COURT_LOG_EVENT_UPDATE_DEF);

		// Use the client time instead of the server time.
		// logEntry.setEntryDate(Calendar.getInstance().getTime());
		logEntry.setEntryDate(defendant.getUpdateTime().getTime());

		logEntry.setEntryFreeText(buildDefendantName(defendant));

		/**
		 * @todo need to add defendantOnCaseId for the CJSE event, not for
		 *       October 03 release so not implementing now
		 */

		// find the court log controller and create the entry
		CourtLogWorkFlow.newEntry(logEntry);
	}

	private void createAuditEvent(DefendantValue defendant) {
		Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
		hashtable.put(DefendantValue.class.getName(), defendant);
		AuditTrailService auditService = CSServices.getAuditTrailService();
		AuditTrailEvent event = auditService.getAuditTrailEvent(hashtable);
		event.setSuccess(true);
		auditService.createAuditRecord(event);
	}
	
	/**
	 * This method constructs the client VO by finding the defendant, Address, Defendant Reference and
	 * the defendantOnCase details.
	 * 
	 * @param defId
	 *            the current defendant ID
	 * @param caseId
	 *            the current case ID
	 * @return DefendantValue Full DefendantValue Object
	 * @throws DefendantControllerException
	 *             custom exception
	 */
	public DefendantValue getFullDefendantDetails(Integer defId, Integer caseId)
			throws DefendantControllerException {
		methodName = "getDefendantDetails(" + defId + ", " + caseId + ")";
		LOG.debug(methodName + " called");

		// defendantDetails
		Defendant defBean = getDefendant(defId);

		// Populate the DefendantValue object
		DefendantValue defendantValue = this.getDefValue(defBean);

		// defendantOnCaseDetails
		try{
			DefendantOnCaseBasicValue defOnCaseBasicValue = defOnCaseMaintainer
					.getDefendantOnCaseBasicValue(defOnCaseMaintainer.findByDefendantAndCase(defId, caseId));
			defendantValue.setDefOnCaseBasicValue(defOnCaseBasicValue);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
			throw new DefendantControllerException(DEF_ON_CASE_NOT_FOUND,
					"Defendant " + defId + ", on case " + caseId + " not found", e);
		}

		LOG.debug(methodName + "returning");
		return defendantValue;
	}
 	 

	/**
	 * The public display hide fields are local to Xhibit and changes to them do
	 * not have to be propogated to CREST via the Mercator integration facade.
	 * 
	 * @param defendant
	 */
	public void updatePublicDisplayHideSettings(DefendantValue defendantValue, CaseStatusValue caseStatusValue,
			String userDisplayName)
			throws DefendantControllerException {

		createAuditEvent(defendantValue);
		updatePublicDisplayHideSettingsAux(defendantValue, caseStatusValue);
		// Tell the Public Displays that the defendant has changed
		notifyNewPublicDisplays(caseStatusValue, userDisplayName);
	}

	/**
	 * updatePublicDisplayHideSettings auxiliary method.
	 * 
	 * @param defendantValue
	 * @param caseStatusValue
	 * @throws DefendantControllerException
	 */
	public void updatePublicDisplayHideSettingsAux(DefendantValue defendantValue, CaseStatusValue caseStatusValue)
			throws DefendantControllerException {

		DefendantOnCase defendantOnCase = getDefendantOnCase(defendantValue.getDefendantID(),
				caseStatusValue.getCaseID());
		defendantOnCase.setPublicDisplayHide(defendantValue.getDefOnCaseBasicValue().isHideDefendantInThisCase()
				? CaseBasicValue.HIDE_IN_PUBLIC_DISPLAY_FLAG : null);

		Defendant defendant = getDefendant(defendantValue.getDefendantID());
		defendant.setPublicDisplayHide(
				defendantValue.getHideDefendantInAllCases() ? CaseBasicValue.HIDE_IN_PUBLIC_DISPLAY_FLAG : null);
	}

	/**
	 * Method to notify the public displays.
	 * 
	 * @param caseId
	 *            Integer
	 * @throws DefendantControllerException
	 */
	/*
	 * private void notifyPublicDisplays(Integer caseId) throws
	 * DefendantControllerException { log.debug("notifyPublicDisplays() with: \n
	 * " + "\ncaseId: " + caseId); PDNotificationValue notificationValue = new
	 * PDNotificationValue();
	 * notificationValue.setNotificationId(notificationValue.DEFENDANT_AMENDED);
	 * // get the court urn for this scheduled hearing String urn; try { // need
	 * courtRoomId and courtSiteId to build urn caseController =
	 * (CaseController) CSServices.getEJBServices()
	 * .createLocalSession(CaseControllerHome.class); SchedHearingLocationValue
	 * locationVal = caseController.getTodaysSchedHearingLocation(caseId); //
	 * only notify if a location is found for today, otherwise must be editing
	 * // a case from another day's log and therfore no public display to notify
	 * if (locationVal != null) { CourtLogMessagingHelper clmHelper = new
	 * CourtLogMessagingHelper(); urn =
	 * clmHelper.buildCourtURN(locationVal.getCourtRoomID(),
	 * locationVal.getCourtSiteID());
	 * 
	 * notificationValue.addParameter(notificationValue.URN, urn);
	 * subscriptionHelper.publishEvent("publicDisplayListener",
	 * notificationValue); } } catch (CourtLogException e) {
	 * CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
	 * throw new
	 * DefendantControllerException(e.getUserMessageAsMessage().getKey(),
	 * e.getMessage(), e); } catch (SubscriptionException e) {
	 * CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
	 * throw new
	 * DefendantControllerException(e.getUserMessageAsMessage().getKey(),
	 * e.getMessage(), e); } catch (CaseControllerException e) {
	 * CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
	 * throw new
	 * DefendantControllerException(e.getUserMessageAsMessage().getKey(),
	 * e.getMessage(), e); } }
	 */

	/**
	 * Method to build the defendant name. The defendant name will be in format:
	 * surname, firsname initials
	 * 
	 * @param defendant
	 *            DefendantValue
	 * @return String - the full built up name
	 */
	private String buildDefendantName(DefendantValue defendant) {
		LOG.debug("buildDefendantName called");
		StringBuffer name = new StringBuffer();

		// Add the inital text....
		name.append("Details of defendant ");

		if (defendant != null) {
			LOG.debug("Defendant id : " + defendant.getDefendantID());

			// add the surname
			if (defendant.getSurName() != null) {
				name.append(defendant.getSurName());
			}
			// add firstname and any formatting
			if (defendant.getFirstName() != null) {
				if (name.length() > 0) {
					name.append(", ");
				}
				name.append(defendant.getFirstName());
			}
			// add intitials and any formatting
			if (defendant.getInitials() != null) {
				if (name.length() > 0) {
					name.append(" ");
				}
				name.append(defendant.getInitials());
			}
		} else {
			LOG.debug("Defendant is null");
			name.append("");
		}
		name.append(" amended");

		LOG.debug("buildDefendantName finished - return name : " + name.toString());
		return name.toString();
	}

	public void notifyNewPublicDisplays(CaseStatusValue caseStatusValue, String userDisplayName) throws DefendantControllerException {
		try {
			Integer caseId = caseStatusValue.getCaseID();
			caseController = (CaseControllerLocal) CSServices.getEJBServices()
					.createLocalSession(CaseControllerLocalHome.class);
			// See if there are any scheduled hearings for today...
			ScheduledHearingValue[] scheduledHearings = caseController.getScheduledHearingsForCaseOnDay(caseId,
					Calendar.getInstance());
			if (scheduledHearings.length > 0) {
				// Notify...
				SchedHearingLocationValue locationVal = caseController.getTodaysSchedHearingLocation(caseId);
				Integer courtId = courtSiteMaintainer.findByPrimaryKey(locationVal.getCourtSiteID()).getCourtId();
				String courtName = getCourtName(courtId);
				Integer courtRoomNo = getCourtRoomNumber(locationVal);
				DisplayablePublicNoticeValue[] publicNotices = PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(locationVal.getCourtRoomID());
				CourtRoomIdentifier cri = new CourtRoomIdentifier(courtId, locationVal.getCourtRoomID(), courtName, courtRoomNo, publicNotices);
				CaseChangeInformation cci = new CaseChangeInformation(isCaseActive(scheduledHearings));
				UpdateCaseEvent uce = new UpdateCaseEvent(cri, cci);
				PddaHelper notifier = new PddaHelper();
				notifier.sendMessage(uce, userDisplayName);
			}
		} catch (CaseControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new DefendantControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
		} catch (ObjectNotFoundException ex) {
			// the defendant passed in could not be found
			CSServices.getDefaultErrorHandler().handleError(ex, DefendantControllerBean.class);
			throw new CSUnrecoverableException("Court site not found.", ex);
		} catch (PublicNoticeCourtRoomUnknownException e) {
			CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
			throw new CSUnrecoverableException("No public notices found.", e);
		}
	}

	private boolean isCaseActive(ScheduledHearingValue[] scheduledHearings) {
		boolean returnValue = false;
		for (int i = 0; i < scheduledHearings.length && !returnValue; i++) {
			returnValue = scheduledHearings[i].getCaseActive().equals("Y");
		}
		return returnValue;
	}

	private DefendantValue getDefValue(Defendant def) {
		LOG.debug("Found" + def.getDefendantId());
		// Convert Timestamp dob and last conv. date to Calendar
		Calendar dateOfBirth = Calendar.getInstance();
		if (def.getDateOfBirth() != null) {
			dateOfBirth.setTime(def.getDateOfBirth());
		} else {
			dateOfBirth = null;
		}

		Calendar lastConvictionDate = Calendar.getInstance();
		if (def.getLastConvictionDate() != null) {
			lastConvictionDate.setTime(def.getLastConvictionDate());
		} else {
			lastConvictionDate = null;
		}

		// Populate the DefendantValue object
		DefendantValue defendantValue = new DefendantValue(def.getDefendantId(), def.getCrestDefendantId(),
				def.getFirstName(), def.getMiddleName(), def.getSurname(), def.getInitials(), dateOfBirth,
				def.getGender(), lastConvictionDate, def.getCourtId(), def.getCurrentPrisonStatus(), def.getPrisonId());
		defendantValue.setEthnicAppearanceCode(def.getEthnicAppearanceCode());
		defendantValue.setEthnicitySelfDefined(def.getEthnicitySelfDefined());
		defendantValue.setParentGuardianName(def.getParentGuardianName());
		defendantValue.setPrisonId(def.getPrisonId());
		defendantValue.setUpdateCount(def.getVersion().intValue());
		defendantValue.setIsCompany(def.getIsCompany());
		defendantValue.setAddressId(def.getAddressId());
		defendantValue.setCurrentPrisonStatus(def.getCurrentPrisonStatus());
		if (def.getPublicDisplayHide() != null) {
			defendantValue.setHideDefendantInCallCases(def.getPublicDisplayHide().equals("Y"));
		} else {
			defendantValue.setHideDefendantInCallCases(false);
		}
		// Find the address record for this defendant
		Integer addressID = defendantValue.getAddressId();
		if (addressID != null) {
			try {
				Address addressBean = (Address) CSServices.getEJBServices()
						.findLocalEntityByPrimaryKey(AddressHome.class, addressID);
				AddressValue addressValue = new AddressValue(addressBean.getAddressId(), addressBean.getAddress1(),
						addressBean.getAddress2(), addressBean.getAddress3(), addressBean.getAddress4(),
						addressBean.getTown(), addressBean.getCounty(), addressBean.getPostcode(),
						addressBean.getCountry());
				addressValue.setUpdateCount(addressBean.getVersion().intValue());
				defendantValue.setAddressValue(addressValue);
			} catch (ObjectNotFoundException e) {
				// this is an unexpected exception, the address id was
				// retrieved as part of the defendant details so there should
				// be an entity for this id
				CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
				throw new CSUnrecoverableException("Could not find an address entity "
						+ "from an id retrieved as part of " + "a defendant entity ", e);
			}
		}

		try {
			// add reference data
			DefendantReference driverNo = defRefMaintainer.findByDefendantIdAndReferenceName(
					defendantValue.getDefendantID(), DefendantReferenceProperties.DRIVER_NUMBER);
			defendantValue.setDriverNo(defRefMaintainer.getDefendantReferenceBasicValue(driverNo));
		} catch (ObjectNotFoundException e) {
			// Note: Defendant Reference does not contain driver info
		}

		try {
			// add reference data
			DefendantReference croNo = defRefMaintainer.findByDefendantIdAndReferenceName(
					defendantValue.getDefendantID(), DefendantReferenceProperties.CRO_NUMBER);
			defendantValue.setCroNo(defRefMaintainer.getDefendantReferenceBasicValue(croNo));
		} catch (ObjectNotFoundException e) {
			//Note: Defendant Reference does not contain cro no info
		}

		try {
			// add reference data
			DefendantReference prisonerNo = defRefMaintainer
					.findByDefendantIdAndReferenceName(defendantValue.getDefendantID(), "Prisoner Number");
			defendantValue.setPrisonerNo(defRefMaintainer.getDefendantReferenceBasicValue(prisonerNo));
		} catch (ObjectNotFoundException e) {
			//Note: Defendant Reference does not contain prisoner no info 
		}
		
		try {
			// add reference data
			DefendantReference licenceType = defRefMaintainer
					.findByDefendantIdAndReferenceName(defendantValue.getDefendantID(), DefendantReferenceProperties.LICENCE_TYPE);
			defendantValue.setLicenceType(defRefMaintainer.getDefendantReferenceBasicValue(licenceType));
		} catch (ObjectNotFoundException e) {
			//Note: Defendant Reference does not contain prisoner no info 
		}
		
		try {
			// add reference data
			DefendantReference licenceIssueNumber = defRefMaintainer
					.findByDefendantIdAndReferenceName(defendantValue.getDefendantID(), DefendantReferenceProperties.LICENCE_ISSUE_NUMBER);
			defendantValue.setLicenceIssueNumber(defRefMaintainer.getDefendantReferenceBasicValue(licenceIssueNumber));
		} catch (ObjectNotFoundException e) {
			//Note: Defendant Reference does not contain prisoner no info 
		}
		return defendantValue;
	}

	public DefendantBasicValue createDefendant(DefendantValue dv, Integer addressId, String userDisplayName) throws FinderException, CreateException {
			String firstName = null;
			String middleName = null;
			String surname = null;
			String initials = null;
			String ethnicAppearanceCode = null;
			String ethnicitySelfDefined = null;
			Integer gender = null;
			Calendar dob = null;
			Timestamp DOB = null;
			String parentGuardian = null;
			String isCompany = null;
			Integer courtId = null;
			String currentPrisonStatus = "N";
			String prisonId = null;

			if (dv.getFirstName() != null) {
				firstName = dv.getFirstName();
			}
			if (dv.getMiddleName() != null) {
				middleName = dv.getMiddleName();
			}
			if (dv.getSurName() != null) {
				surname = dv.getSurName();
			}
			if (dv.getInitials() != null) {
				initials = dv.getInitials();
			}
			if (dv.getEthnicAppearanceCode() != null) {
				ethnicAppearanceCode = dv.getEthnicAppearanceCode();
			}

			if (dv.getEthnicitySelfDefined() != null) {
				ethnicitySelfDefined = dv.getEthnicitySelfDefined();
			}

			if (dv.getGender() != null) {
				gender = dv.getGender();
			}
			if (dv.getDateOfBirth() != null) {
				dob = dv.getDateOfBirth();
				DOB = new Timestamp(dob.getTimeInMillis());
			}
			if (dv.getParentGuardianName() != null) {
				parentGuardian = dv.getParentGuardianName();
			}
			if (dv.getIsCompany() != null) {
				isCompany = dv.getIsCompany();
			}
			if (dv.getCourtID() != null) {
				courtId = dv.getCourtID();
			}
			if (dv.getCurrentPrisonStatus() != null) {
				currentPrisonStatus = dv.getCurrentPrisonStatus();
			}
			if (dv.getPrisonId() != null) {
				prisonId = dv.getPrisonId();
			}

			DefendantHome xdHome = (DefendantHome) CSServices.getServiceLocator().getLocalHome(DefendantHome.class);

			LOG.debug("About to create defendant");
			Defendant defendant = xdHome.create(null, firstName, middleName, surname, initials, DOB,
					parentGuardian, gender, null, addressId, isCompany, courtId, userDisplayName, ethnicAppearanceCode,
					ethnicitySelfDefined, currentPrisonStatus, prisonId);

			if (LOG.isDebugEnabled()) {
				LOG.debug("***************************************");
				LOG.debug("Creating defendant with values: First Name: " + defendant.getFirstName() + ", Middle Name: "
						+ defendant.getMiddleName() + ", Surname: " + defendant.getSurname() + ", Initials: "
						+ defendant.getInitials() + ", Date of Birth: " + defendant.getDateOfBirth() + ", gender: "
						+ defendant.getGender() + ", Last Conviction Date: " + defendant.getLastConvictionDate()
						+ ", CourtID: " + defendant.getCourtId() + ", All by the user: " + userDisplayName);
			}
			
			return defMaintainer.getDefendantBasicValue(defendant);
	}

	public Integer createDefendantAddress(AddressValue address, String userDisplayName) throws CreateException {
		AddressHome adHome = (AddressHome) CSServices.getServiceLocator().getLocalHome(AddressHome.class);

		String address1 = null;
		String address2 = null;
		String address3 = null;
		String address4 = null;
		String town = null;
		String county = null;
		String country = null;
		String postcode = null;
		if (address.getAddress1() != null) {
			address1 = address.getAddress1();
		}
		if (address.getAddress2() != null) {
			address2 = address.getAddress2();
		}
		if (address.getAddress3() != null) {
			address3 = address.getAddress3();
		}
		if (address.getAddress4() != null) {
			address4 = address.getAddress4();
		}
		if (address.getTown() != null) {
			town = address.getTown();
		}
		if (address.getCounty() != null) {
			county = address.getCounty();
		}
		if (address.getCountry() != null) {
			country = address.getCountry();
		}
		if (address.getPostcode() != null) {
			postcode = address.getPostcode();
		}

		Address newAddress = adHome.create(address1, address2, address3, address4, town, county, postcode, country, userDisplayName);
		Integer addressId = newAddress.getAddressId();
		return addressId;
	}

	public DefendantValue findByDefId(Integer DefendantId) throws FinderException {
		DefendantValue dv = new DefendantValue();
		DefendantHome dHome = (DefendantHome) CSServices.getServiceLocator().getLocalHome(DefendantHome.class);
		Defendant d = dHome.findByPrimaryKey(DefendantId);
		if (!(d.getAddressId() == null)) {
			dv.setAddressId(d.getAddressId());
		}
		if (!(d.getDateOfBirth() == null)) {
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(d.getDateOfBirth());
			dv.setDateOfBirth(cal);
		}
		if (!(d.getCourtId() == null)) {
			dv.setCourtID(d.getCourtId());
		}
		if (!(d.getDefendantId() == null)) {
			dv.setDefendantID(d.getDefendantId());
		}
		if (!(d.getEthnicAppearanceCode() == null)) {
			dv.setEthnicAppearanceCode(d.getEthnicAppearanceCode());
		}
		if (!(d.getEthnicitySelfDefined() == null)) {
			dv.setEthnicitySelfDefined(d.getEthnicitySelfDefined());
		}
		if (!(d.getFirstName() == null)) {
			dv.setFirstName(d.getFirstName());
		}
		if (!(d.getSurname() == null)) {
			dv.setSurName(d.getSurname());
		}
		if (!(d.getMiddleName() == null)) {
			dv.setMiddleName(d.getMiddleName());
		}
		if (!(d.getParentGuardianName() == null)) {
			dv.setParentGuardianName(d.getParentGuardianName());
		}
		if (!(d.getGender() == null)) {
			dv.setGender(d.getGender());
		}
		if (!(d.getInitials() == null)) {
			dv.setInitials(d.getInitials());
		}
		if (!(d.getIsCompany() == null)) {
			dv.setIsCompany(d.getIsCompany());
		}
		if (!(d.getPrisonId() == null)) {
			dv.setPrisonId(d.getPrisonId());
		}
		if (!(d.getVersion() == null)) {
			dv.setVersion(d.getVersion());
		}
		if (!(d.getCurrentPrisonStatus() == null)) {
			dv.setCurrentPrisonStatus(d.getCurrentPrisonStatus());
		}
		return dv;
	}

	public Integer createDefendantReference(String refValue, String refName, Integer defId, String userDisplayName)
			throws CreateException, FinderException {
		DefendantReferenceHome dfHome = (DefendantReferenceHome) CSServices.getServiceLocator()
				.getLocalHome(DefendantReferenceHome.class);
		DefendantHome dHome = (DefendantHome) CSServices.getServiceLocator().getLocalHome(DefendantHome.class);
		Defendant d = dHome.findByPrimaryKey(defId);
		DefendantReference da = dfHome.create(refValue, refName, "", defId, d, userDisplayName);
		return da.getDefRefId();
	}

	public void createDefendantReferenceWithDefendant(String refValue, String refName, Integer defId,
			DefendantValue def, String userDisplayName) throws CreateException, FinderException {
		DefendantReferenceHome dfHome = (DefendantReferenceHome) CSServices.getServiceLocator()
				.getLocalHome(DefendantReferenceHome.class);
		DefendantHome dHome = (DefendantHome) CSServices.getServiceLocator().getLocalHome(DefendantHome.class);
		Defendant d = dHome.findByPrimaryKey(defId);
		dfHome.create(refValue, refName, "", defId, d, userDisplayName);
	}

	/**
	 * This method updates the defendant in the xhb defendant, xhb address and
	 * xhb defendant Reference
	 * 
	 * @param defValue
	 *            a DefendantValue object
	 * @param addValue
	 *            AddressBasicValue
	 * @param userDisplayName
	 *            a String
	 * @throws DefendantControllerException
	 *             custom exception
	 */
	public void updateDefendant(DefendantValue defValue, AddressBasicValue addValue, String userDisplayName) {
		methodName = "updateDefendant(" + defValue + ", " + userDisplayName + ")";
		LOG.debug(methodName + " called");

		if (LOG.isDebugEnabled()) {
			LOG.debug("START: updateDefendant(" + defValue + ", " + userDisplayName + ")");
		}
		try {
			DefendantHome xdHome = (DefendantHome) CSServices.getServiceLocator().getLocalHome(DefendantHome.class);
			Defendant dv = xdHome.findByPrimaryKey(defValue.getDefendantID());
			if (!dv.getVersion().equals(defValue.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				if (!(defValue.getCrestDefendantID() == null)) {
					dv.setCrestDefendantId(defValue.getCrestDefendantID());
				}
				if (!(defValue.getCurrentPrisonStatus() == null)) {
					dv.setCurrentPrisonStatus(defValue.getCurrentPrisonStatus());
				}
				if (defValue.getDateOfBirth() != null) {
					Timestamp tim = new Timestamp(defValue.getDateOfBirth().getTimeInMillis());
					dv.setDateOfBirth(tim);
				} else {
					dv.setDateOfBirth(null);
				}
				if (!(defValue.getEthnicAppearanceCode() == null)) {
					dv.setEthnicAppearanceCode(defValue.getEthnicAppearanceCode());
				}
				if (!(defValue.getEthnicitySelfDefined() == null)) {
					dv.setEthnicitySelfDefined(defValue.getEthnicitySelfDefined());
				}
				if (!(defValue.getFirstName() == null)) {
					dv.setFirstName(defValue.getFirstName());
				}
				if (!(defValue.getGender() == null)) {
					dv.setGender(defValue.getGender());
				}
				if (!(defValue.getInitials() == null)) {
					dv.setInitials(defValue.getInitials());
				}
				if (!(defValue.getIsCompany() != null)) {
					dv.setIsCompany(defValue.getIsCompany());
				}
				if (!(defValue.getLastConvictionDate() == null)) {
					Timestamp timTab = new Timestamp(defValue.getLastConvictionDate().getTimeInMillis());
					dv.setLastConvictionDate(timTab);
				}
				if (!(defValue.getMiddleName() == null)) {
					dv.setMiddleName(defValue.getMiddleName());
				}
				if (!(defValue.getParentGuardianName() == null)) {
					dv.setParentGuardianName(defValue.getParentGuardianName());
				}
				if (!(defValue.getPrisonId() == null)) {
					dv.setPrisonId(defValue.getPrisonId());
				}
				if (!(defValue.getSurName() == null)) {
					dv.setSurname(defValue.getSurName());
				}
				if (!(userDisplayName == null)) {
					dv.setUpdated(userDisplayName);
				}
			}
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}

	public boolean updateAddress(AddressBasicValue addValue, String userDisplayName) {
		if (!(addValue.getAddressId() == null)) {
			addressMaintainer.update(addValue, userDisplayName);
			return true;
		}
		return false;
	}

	public boolean updateDefendantReference(Integer defId, DefendantReferenceBasicValue dr, String userDisplayName)
			throws ObjectNotFoundException {
		DefendantReference dfD = defRefMaintainer.findByDefendantIdAndReferenceName(defId, dr.getReferenceName());

		if (!(dfD.getDefRefId() == null || dfD.getDefRefId() == 0)) {

			dr.setId(dfD.getDefRefId());
			if (!(dfD.getCategory() == null)) {
				dr.setCategory(dfD.getCategory());
			}
			dr.setDefendantID(dfD.getDefendantId());
			dr.setVersion(dfD.getVersion());

			defRefMaintainer.update(dr, userDisplayName);
			return true;
		}
		return false;
	}
	
	public String getCourtName(Integer courtId) {
		String courtName = "Unknown";
		try {
			CourtMaintainer courtMaintainer = new CourtMaintainer();
			courtName = courtMaintainer.findByPrimaryKey(courtId).getCourtName();
		} catch (ObjectNotFoundException e) {
			LOG.error("Cannot find the court site name.");
			e.printStackTrace();
		}
		return courtName;
	}
	
	
	public Integer getCourtRoomNumber(SchedHearingLocationValue shlv) {
		Integer courtRoomNo = 0;
		if ((shlv != null) && (shlv.getCourtSiteID() != null)) {
			CourtRoomMaintainer courtRoomMaintainer = new CourtRoomMaintainer();
			try {
				courtRoomNo = courtRoomMaintainer.findByPrimaryKey(shlv.getCourtRoomID()).getCrestCourtRoomNo();
			} catch (ObjectNotFoundException e) {
				LOG.error("Cannot find the court room number.");
				e.printStackTrace();
			}
		}
		return courtRoomNo;
	}
}
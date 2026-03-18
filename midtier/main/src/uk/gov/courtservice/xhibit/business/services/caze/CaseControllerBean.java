package uk.gov.courtservice.xhibit.business.services.caze;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.casereference.CaseReference;
import uk.gov.courtservice.xhibit.business.entities.casereference.CaseReferenceMaintainer;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.chargeslog.ChargesLogMaintainer;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingHome;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntry;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refusedbroadcastcase.RefusedBroadcastCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_charges_log.XhbChargesLog;
import uk.gov.courtservice.xhibit.business.entities.xhb_charges_log.XhbChargesLogBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.createcase.GenerateCaseGroupControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.createcase.GenerateCaseNumberControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.darts.DartsHelper;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantOnCaseDatabaseManager;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.SystemCodeHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ChargesLogBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DarRetentionPolicyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefusedBroadcastCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caze.SchedHearingLocationValue;
import uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.courtlog.helpers.SubscriptionValueAssembler;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
import uk.gov.courtservice.xhibit.integration.vos.services.caseretrieval.CaseAccessValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @ejb.bean name="CaseController" description="Case Session Bean"
 *           type="Stateless" view-type="both" jndi-name="CaseControllerHome"
 *           local-jndi-name="CaseControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author unascribed
 * @author Ian Hannaford
 * @version 3.0
 * @history 20/04/2003 Ian Hannaford Added new method <code>checkCase</code>.
 * @history 13/03/2009 James Powell. marked method
 *          <code>getScheduledHearings(Integer)</code> as 'both' from 'remote'
 *          to allow it to be called via its remote interface
 * @history 23/04/2009 Kelvin Davies UpdateCRESTCase method added, thsi method
 *          add sends a call to mercator to update the Date Exported field for
 *          CREST (CCN1263).
 * @history 17/06/2017 Nia Walters create case method added.
 * @history 02/08/2017 Nia Walters adding method to add case prosecutor details.
 * @history 12/09/2018 Nia Walters switching so that all case creation is done
 *          in 1 method and if any fail then rollback.
 * @history 07/02/2018 Mark Groen added getCaseComplexValueByPrimaryKey() method
 */
public class CaseControllerBean extends CSSessionBean implements SessionBean {
	
	
	/**
	 * serial version uid.
	 */
	private static final long serialVersionUID = -1482124779093244736L;
	
	// error keys
	private static final String CASE_NOT_FOUND = "case.casenotfound";
	private static final String UNEXPECTED_ERROR = "xhibit.error.unexpected";

	private CaseHelper caseHelper = new CaseHelper();
	private CaseDatabaseManager caseDatabaseManager = new CaseDatabaseManager();
	private static final String PACKAGE_NAME = "uk.gov.courtservice.xhibit.business.services.caze";
	private static final String CLASS_NAME = ".CaseControllerBean";
	private static final String ERROR_IN = "Error in ";
	private static final String FOUND = "found ";
	private static final String CASE_NUMBER =  ", caseNumber=";
	private static final String COURT_ID = ", courtId=";
	private static final String EXCEPTION = "Exception";
	private static final String USER_DISPLAY_NAME = ", userDisplayName=";
	private static final String GROUP_NUMBER =  ", groupNumber=";
	private static final String DEF_ON_CASE_ID = ", defOnCaseId=";
	private static final String CURRENT_DATE = ", currentDate=";


	/**
	 * Returns all the hearings which have been scheduled so far for a given
	 * caseId
	 * 
	 * @param caseID
	 *            The case id
	 * @return A Collection of ScheduledHearing value objects containing the
	 *         scheduled hearing id and date.
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection getScheduledHearings(Integer caseID) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: getScheduledHearings(caseID="+caseID+")");
		}
		// Find the hearings by caseId
		try {
			Collection scheduledHearingValues = caseHelper.findScheduledHearingsByCaseId(caseID);
			return scheduledHearingValues;
		} catch (NamingException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CaseControllerBean.class);
			throw new CSUnrecoverableException(e);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CaseControllerBean.class);
			throw new CSUnrecoverableException(e);
		}
	}

	/**
	 * Returns all the hearings which have been scheduled so far for a given
	 * caseId, fromDate and toDate
	 * 
	 * @param caseID
	 *            The case id
	 * @param fromDate
	 * @param toDate
	 * @return A Collection of ScheduledHearing value objects containing the
	 *         scheduled hearing id and date.
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection getScheduledHearings(Integer caseID, Date fromDate, Date toDate) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: getScheduledHearings(caseID="+caseID+", fromDate="+fromDate+", toDate="+toDate+")");
		}
		ArrayList scheduledHearingValues = new ArrayList();

		// Find the hearings by caseId
		try {
			Context ctx = new InitialContext();
			HearingHome home = (HearingHome) ctx.lookup("HearingHome");
			Collection hearingBeans = home.findByCaseId(caseID);
			if (log.isDebugEnabled()) {
				log.debug(FOUND + hearingBeans.size() + " hearings for caseId=" + caseID);
			}
			Iterator hearingsIterator = null;
			hearingsIterator = hearingBeans.iterator();

			// get scheduled hearings for each hearing
			while (hearingsIterator.hasNext()) {
				Hearing hearing = (Hearing) hearingsIterator.next();
				Collection scheduledHearingBeans = hearing.getScheduledHearings();
				if (log.isDebugEnabled()) {
					log.debug(FOUND + scheduledHearingBeans.size() + " scheduled hearings for hearing=" + hearing);
				}
				Iterator scheduledHearingsIterator = scheduledHearingBeans.iterator();

				// get the ID and date for each scheduled hearing
				while (scheduledHearingsIterator.hasNext()) {
					ScheduledHearing scheduledHearing = (ScheduledHearing) scheduledHearingsIterator.next();
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
					if (log.isDebugEnabled()) {
						log.debug("adding scheduled hearing value=" + scheduledHearingValue);
					}

					// Ok so only add this scheduled hearing if:
					// Its after the start and before the end date (where it is
					// the end of the end date, i.e. 23:59 not 00:00)

					if ((ts.getTime() >= fromDate.getTime())
							&& (ts.getTime() < toDate.getTime() + (24 * 60 * 60 * 1000))) { // toDate+24hrs
						scheduledHearingValues.add(scheduledHearingValue);
					}
				}
			}
		} catch (NamingException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CaseControllerBean.class);
			throw new CSUnrecoverableException(e);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CaseControllerBean.class);
			throw new CSUnrecoverableException(e);
		}

		return scheduledHearingValues;
	}

	/**
	 * Returns all the hearings which have been scheduled so far for a given
	 * caseId.
	 * 
	 * @param caseType
	 *            The case type. S for Sentence, T for Trial e.t.c
	 * @param caseNumber
	 *            The case number
	 * @param courtId
	 *            The court Id
	 * @return A Collection of ScheduledHearingValue objects containing the
	 *         scheduled hearing id and date.
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection getScheduledHearings(String caseType, Integer caseNumber, Integer courtId)
			throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: getScheduledHearings(caseType="+caseType+CASE_NUMBER+caseNumber+COURT_ID+courtId+")");
		}
		return getScheduledHearings(findCaseId(caseType, caseNumber, courtId));
	}

	/**
	 * See uk.gov.courtservice.xhibit.integration.services.IntegrationFacade for
	 * details
	 * 
	 * @param caseId
	 *            The case id
	 * @return CaseAccessValue
	 * @throws CaseAccessException
	 * @throws CaseRetrievalIntControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseAccessValue refreshLeaseTime(Integer caseId)
			throws CaseAccessException, CaseRetrievalIntControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START:  refreshLeaseTime(caseId="+caseId+")");
		}
		return new CaseRetrievalIntController().refreshLeaseTime(caseId);
	}

	/**
	 * See uk.gov.courtservice.xhibit.integration.services.IntegrationFacade for
	 * details
	 * 
	 * @param caseId
	 *            The case id
	 * @return CaseAccessValue
	 * @throws CaseAccessException
	 * @throws CaseRetrievalIntControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseAccessValue checkCaseAccess(Integer caseId)
			throws CaseAccessException, CaseRetrievalIntControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: checkCaseAccess(caseId="+caseId+")");
		}
		return new CaseRetrievalIntController().refreshLeaseTime(caseId);
	}

	/**
	 * See uk.gov.courtservice.xhibit.integration.services.IntegrationFacade for
	 * details
	 * 
	 * @param caseId
	 *            The case id
	 * @return CaseAccessValue
	 * @throws CaseAccessException
	 * @throws CaseRetrievalIntControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseAccessValue openCase(Integer caseId) throws CaseAccessException, CaseRetrievalIntControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: openCase(caseId="+caseId+")");
		}
		return new CaseRetrievalIntController().openCase(caseId);
	}

	/**
	 * Gets the schedHearingLocation for a given case for today
	 * 
	 * @param caseID
	 *            The case id
	 * @return The shedHearingLocation or null if there is no hearing scheduled
	 *         for today
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public SchedHearingLocationValue getTodaysSchedHearingLocation(Integer caseID) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: getTodaysSchedHearingLocation(caseID="+caseID+")");
		}
		Collection schedHearings = getScheduledHearings(caseID);
		if (schedHearings != null) {
			if (log.isDebugEnabled()) {
				log.debug("schedHearings.size=" + schedHearings.size());
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("schedHearings== null");
			}
		}

		Iterator schedHearingsValues = schedHearings.iterator();
		while (schedHearingsValues.hasNext()) {
			ScheduledHearingValue schedHearingValue = (ScheduledHearingValue) schedHearingsValues.next();
			Calendar today = Calendar.getInstance();
			Calendar hearingDate = schedHearingValue.getScheduledHearingDate();
			if (log.isDebugEnabled()) {
				log.debug("hearingDate=" + hearingDate);
				log.debug("today=" + today);
			}
			if (hearingDate != null && today.get(Calendar.DAY_OF_MONTH) == hearingDate.get(Calendar.DAY_OF_MONTH)
					&& today.get(Calendar.MONTH) == hearingDate.get(Calendar.MONTH)
					&& today.get(Calendar.YEAR) == hearingDate.get(Calendar.YEAR)) {
				try {
					if (log.isDebugEnabled()) {
						log.debug("creating sheduledHearingValue");
					}
					Integer schedHearingID = schedHearingValue.getScheduledHearingID();
					ScheduledHearing sheduledHearing = (ScheduledHearing) CSServices.getEJBServices()
							.findLocalEntityByPrimaryKey(ScheduledHearingHome.class, schedHearingID);
					SchedHearingLocationValue schedHearingLocationValue = new SchedHearingLocationValue();
					schedHearingLocationValue.setCourtRoomID(sheduledHearing.getSitting().getCourtRoomId());
					schedHearingLocationValue.setCourtSiteID(sheduledHearing.getSitting().getCourtSiteId());
					schedHearingLocationValue.setShedHearingID(schedHearingID);
					if (log.isDebugEnabled()) {
						log.debug("created sheduledHearingValue=" + schedHearingLocationValue);
					}
					return schedHearingLocationValue;
				} catch (ObjectNotFoundException e) {
					// This is an unexpected exception, we have retrieved
					// the
					// schedHearingID so this entity should exist
					CSServices.getDefaultErrorHandler().handleError(e, CaseControllerBean.class);
					throw new CSUnrecoverableException("Could not find a scheduled " + "hearing which was retrieved by "
							+ "the getScheduledHearings(caseID) " + "method.");
				}
			}
		}
		// if no scheduled hearing today
		return null;
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
	public Collection getDefendants(Integer caseID) throws CaseControllerException, DefendantControllerException 
	{
		
		return caseHelper.getDefendants(caseID);
	}

	/**
	 * Finds a case Id from a case type, number and court id.
	 * 
	 * @param caseType
	 *            the case type
	 * @param caseNumber
	 *            the case number
	 * @param courtId
	 *            the court id
	 * @return the case Id
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Integer findCaseId(String caseType, Integer caseNumber, Integer courtId) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: findCaseId(caseType="+caseType+CASE_NUMBER+caseNumber+COURT_ID+courtId+")");
		}
		CaseMaintainer caseMaintainer = new CaseMaintainer();
		try {
			Case caze = caseMaintainer.findByNumberTypeAndCourt(caseNumber, caseType, courtId);
			return caze.getCaseId();
		} catch (Exception e) {
			throw new CaseControllerException(CASE_NOT_FOUND, new Object[] { caseNumber.toString(), caseType, courtId },
					"Could not find case for caseNumber = " + caseNumber.toString() + " caseType = " + caseType
							+ " courtId = " + courtId,
					e);
		}
	}
	
	/**
	 * Finds a case  from a case type, number and court id.
	 * 
	 * @param caseType
	 *            the case type
	 * @param caseNumber
	 *            the case number
	 * @param courtId
	 *            the court id
	 * @return the case 
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public CaseBasicValue findCase(String caseType, Integer caseNumber, Integer courtId) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: findCase(caseType="+caseType+CASE_NUMBER+caseNumber+COURT_ID+courtId+")");
		}
		CaseMaintainer caseMaintainer = new CaseMaintainer();
		try {
			Case caze = caseMaintainer.findByNumberTypeAndCourt(caseNumber, caseType, courtId);
			return caseMaintainer.getCaseBasicValue(caze);
		} catch (Exception e) {
			throw new CaseControllerException(CASE_NOT_FOUND, new Object[] { caseNumber.toString(), caseType, courtId },
					"Could not find case for caseNumber = " + caseNumber.toString() + " caseType = " + caseType
							+ " courtId = " + courtId,
					e);
		}
	}


	/**
	 * Finds all cases for defendantId.
	 * 
	 * @param defendantId
	 *            the defendant id
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findAllCasesByDefendantId(Integer defendantId, Integer courtId) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: findAllCasesByDefendantId(defendantId="+defendantId+COURT_ID+courtId+")");
		}
		CaseMaintainer caseMaintainer = new CaseMaintainer();

		try {
			Collection cases = caseMaintainer.findAllCasesByDefendantId(defendantId, courtId);
			ArrayList<CaseBasicValue> caseValues = new ArrayList<CaseBasicValue>();
			if (cases != null && cases.size()>0) {
				Iterator caseIterator = null;
				caseIterator = cases.iterator();
				while (caseIterator.hasNext()) {
					Case thisCase = (Case)caseIterator.next();					
					CaseBasicValue caseValue = caseMaintainer.getCaseBasicValue(thisCase);
					caseValues.add(caseValue);
				}
			} else {
				log.warn("Unable to find case for defendant "+defendantId);
			}
			return caseValues;
		} catch (Exception ex) {
			throw new CaseControllerException(CASE_NOT_FOUND, new Object[] { defendantId }, EXCEPTION, ex);
		}
	}

	/**
	 * Finds a case Id from a defendant surname and partial case number.
	 * 
	 * @param defendantName
	 *            defendant surname
	 * @param caseNumber
	 *            the case number
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findByDefendantNameAndCaseNumber(String defendantFirstName, String defendantSurname, String caseType, String caseNumber, Integer courtId)
			throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: findByDefendantNameAndCaseNumber(defendantFirstName="+defendantFirstName+", defendantSurname="+defendantSurname+", caseType="+caseType+CASE_NUMBER+caseNumber+COURT_ID+courtId+")");
		}
		Collection cases = null;
		CaseMaintainer caseMaintainer = new CaseMaintainer();
		try {
			if(defendantFirstName != null && !defendantFirstName.equals("")) {
			cases = caseMaintainer.findByDefendantNamesAndCaseNumber(defendantFirstName, defendantSurname, caseType, caseNumber, courtId);
			} else {
				cases = caseMaintainer.findByDefendantSurnameAndCaseNumber(defendantSurname, caseType, caseNumber, courtId);
			}
			
			ArrayList<CaseBasicValue> caseValues = new ArrayList<CaseBasicValue>();
			if (cases != null && cases.size()>0) {
				Iterator caseIterator = null;
				caseIterator = cases.iterator();

				while (caseIterator.hasNext()) {
					Case thisCase = (Case) caseIterator.next();
					CaseBasicValue caseValue = caseMaintainer.getCaseBasicValue(thisCase);
					caseValues.add(caseValue);
				}
			} else {
				log.warn("Unable to find case "+caseType+""+caseNumber +" for "+defendantFirstName+" "+defendantSurname);
			}
			return caseValues;
		} catch (Exception ex) {
			throw new CaseControllerException(CASE_NOT_FOUND, new Object[] { defendantFirstName+" "+defendantSurname + " " + caseNumber },
					EXCEPTION, ex);
		}
	}

	/**
	 * Updates the case using the given CaseBasicValue.
	 * 
	 * @param cs
	 *            CaseBasicValue
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void amendCase(CaseBasicValue cs, List<ChargesLogBasicValue> chargesLogList, String userDisplayName, List<RefusedBroadcastCaseBasicValue> broadcastVals) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: amendCase(cs="+cs+USER_DISPLAY_NAME+userDisplayName+", chargesLogList="+chargesLogList+")");
		}
		final String METHOD_NAME = ".amendCase - ";
		CaseMaintainer caseMaintainer = new CaseMaintainer();
		try {
			if ("T".equals(cs.getCaseType())) {
				// For Trial cases only, determine if the case listing entry needs to be amended
				Case local = caseMaintainer.findByPrimaryKey(cs.getCaseId());
				CaseBasicValue oldCaseBasicValue = caseMaintainer.getCaseBasicValue(local);
				updateCaseListingEntryForAmendCase(oldCaseBasicValue, cs, userDisplayName);
				
			}
			// --- Create newCase and get case using cs caseID, copy values from
			// cs into newCase ---
			Case updatedCase = caseMaintainer.amendCase(cs, userDisplayName);
			
			//Update the refused broadcast case for T cases
			if("T".equals(cs.getCaseType()) && broadcastVals!=null && broadcastVals.size()>0) {
				RefusedBroadcastCaseMaintainer refusedMaintainer = new RefusedBroadcastCaseMaintainer();
				//get the newly update case 
				refusedMaintainer.updateRefusedBroadcast(broadcastVals, userDisplayName, updatedCase);
			}
			
			// --- Update Charges ---
            ChargesLogMaintainer main = new ChargesLogMaintainer();
			ArrayList<XhbChargesLog> oldChargesLog = (ArrayList<XhbChargesLog>)XhbChargesLogBeanHelper2.findNonObsoleteByCaseId(cs.getCaseId());
			Comparator<XhbChargesLog> chargeSeq = new Comparator<XhbChargesLog>() {
				@Override
				public int compare(XhbChargesLog o1, XhbChargesLog o2) {
					return o1.getSequenceNo().compareTo(o2.getSequenceNo());
				}
			};
			Collections.sort(oldChargesLog,chargeSeq);
			int numOldRows = oldChargesLog.size();
			Collection<ChargesLogBasicValue> newChargesLog = chargesLogList;
			int numNewRows = newChargesLog.size();

			Iterator iterOld = oldChargesLog.iterator();
			Iterator<ChargesLogBasicValue> iterNew = newChargesLog.iterator();
			if (numOldRows == numNewRows) {
				// Update each existing row
				for (int i = 1; i <= numNewRows; i++) {
					XhbChargesLog cL = (XhbChargesLog) iterOld.next();
					ChargesLogBasicValue cLV = iterNew.next();
					cL.setChargesInfo(cLV.getChargesInfo());
					cL.setObsInd("N");
					cL.setLastUpdatedBy(userDisplayName);
				}
			} else if (numOldRows > numNewRows) {
				// update existing rows where data still exists
				for (int i = 1; i <= numNewRows; i++) {
					XhbChargesLog cL = (XhbChargesLog) iterOld.next();
					ChargesLogBasicValue cLV = iterNew.next();
					cL.setChargesInfo(cLV.getChargesInfo());
					cL.setObsInd("N");
					cL.setLastUpdatedBy(userDisplayName);
				}
				// now delete no longer used rows
				while (iterOld.hasNext()) {
					XhbChargesLog cL = (XhbChargesLog) iterOld.next();
					cL.setObsInd("Y");
					cL.setLastUpdatedBy(userDisplayName);
				}
			} else if (numOldRows < numNewRows) {
				// update existing rows
				for (int i = 1; i <= numOldRows; i++) {
					XhbChargesLog cL = (XhbChargesLog) iterOld.next();
					ChargesLogBasicValue cLV = iterNew.next();
					cL.setChargesInfo(cLV.getChargesInfo());
					cL.setObsInd("N");
					cL.setLastUpdatedBy(userDisplayName);
				}
				// add new rows
				for (int i = numOldRows; i < numNewRows; i++) {
					ChargesLogBasicValue cLV = iterNew.next();
					ChargesLogBasicValue chargesLogBasicValue = new ChargesLogBasicValue();
					chargesLogBasicValue.setCaseId(cs.getCaseId());
					chargesLogBasicValue.setSequenceNo(cLV.getSequenceNo());
					chargesLogBasicValue.setChargesInfo(cLV.getChargesInfo());
					main.create(chargesLogBasicValue, userDisplayName);
				}
			}

		} catch (ObjectNotFoundException e) {
			// This update must be on a CaseBasicValue retrieved from the
			// midtier
			// therefore if this case is not found to update it is an unexpected
			// exception
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+" : "+e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new CaseControllerException(UNEXPECTED_ERROR, EXCEPTION, e);
		}
	}
	
	/**
	 * Updates the case basic value and defendant related information in one transaction
	 * 
	 * @param caseBasicValue
	 *  	      cbv to save to db
	 * @param defToAddToCase
	 *            Defendant on cbv to save
	 * @param docRefSolFirmValue
	 *            def on case ref sol firm value to save
	 * @param userDisplayName
	 *            logged in user name
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void amendCaseAndDefendant(CaseBasicValue caseBasicValue, List<DefendantOnCaseBasicValue> defs,
			String userDisplayName, boolean create) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: amendCaseAndDefendant(caseBasicValue="+caseBasicValue+USER_DISPLAY_NAME+userDisplayName+", defs="+defs+", create="+create+")");
		}
		
		CaseMaintainer caseMaintainer = new CaseMaintainer();
		final String METHOD_NAME=".amendCaseAndDefendant - ";
		try {
			// --- Update the Defendants ---
			DefendantOnCaseMaintainer defOnCaseMaintainer = new DefendantOnCaseMaintainer();
			if (defs != null) {			
				defOnCaseMaintainer.amendDefendantsOnCase(defs, userDisplayName, caseBasicValue.getCaseId(), caseBasicValue);
			}
			
			// --- amend case, copy values from
			// cs into newCase ---
			caseMaintainer.amendCase(caseBasicValue, userDisplayName);	
			
		} catch (ObjectNotFoundException e) {
			// This update must be on a CaseBasicValue retrieved from the
			// midtier
			// therefore if this case is not found to update it is an unexpected
			// exception
			ctx.setRollbackOnly();
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+" : "+e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new CaseControllerException(UNEXPECTED_ERROR, EXCEPTION, e);
		}
	}
	
	/**
	 * Updates the case basic value only
	 * 
	 * @param caseBasicValue
	 *  	      cbv to save to db
	 * @param userDisplayName
	 *            logged in user name
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */	
	public void amendCaseOnly(CaseBasicValue caseBasicValue, String userDisplayName) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: amendCaseOnly(caseBasicValue="+caseBasicValue+USER_DISPLAY_NAME+userDisplayName+")");
		}
		CaseMaintainer caseMaintainer = new CaseMaintainer();
		final String METHOD_NAME=".amendCaseOnly - ";
		try {			
			// --- amend case, copy values from
			// cs into newCase ---
			caseMaintainer.amendCase(caseBasicValue, userDisplayName);		
		} catch (ObjectNotFoundException e) {
			// This update must be on a CaseBasicValue retrieved from the
			// midtier
			// therefore if this case is not found to update it is an unexpected
			// exception
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+" : "+e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new CaseControllerException(UNEXPECTED_ERROR, EXCEPTION, e);
		}
	}
	
	/**
	 * Updates the case basic value only and removes group number
	 * 
	 * @param caseId
	 *  	      id of case to remove group number of
	 * @param userDisplayName
	 *            logged in user name
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */	
	public void removeGroupNumber(Integer caseId, String userDisplayName) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: removeGroupNumber(caseId="+caseId+USER_DISPLAY_NAME+userDisplayName+")");
		}
		
		CaseMaintainer caseMaintainer = new CaseMaintainer();
		final String METHOD_NAME=".removeGroupNumber - ";
		try {			
			CaseBasicValue caseBasicValue = getCase(caseId);
			caseBasicValue.setCaseGroupNumber(null);
			caseMaintainer.amendCase(caseBasicValue, userDisplayName);		
		} catch (ObjectNotFoundException e) {
			// This update must be on a CaseBasicValue retrieved from the
			// midtier
			// therefore if this case is not found to update it is an unexpected
			// exception
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+" : "+e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new CaseControllerException(UNEXPECTED_ERROR, EXCEPTION, e);
		}
	}

	/**
	 * Updates the case using the given CaseBasicValue.
	 * 
	 * @param cs
	 *            CaseBasicValue
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateCase(CaseBasicValue cs, String userDisplayName) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: updateCase(cs="+cs+USER_DISPLAY_NAME+userDisplayName+")");
		}
		final String METHOD_NAME = "updateCase(CaseBasicValue) - ";

		CaseMaintainer caseMaintainer = new CaseMaintainer();
		try {
			caseMaintainer.update(cs, userDisplayName);
		} catch (ObjectNotFoundException e) {
			// This update must be on a CaseBasicValue retrieved from the
			// midtier
			// therefore if this case is not found to update it is an
			// unexpected
			// exception
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+" : "+e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);		
       }
	}

	/**
	 * Creates the case using the given CaseBasicValue.
	 * 
	 * @param cs
	 *            CaseBasicValue
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */

	public String createCase(CaseBasicValue cs, String userDisplayName, List<ChargesLogBasicValue> chargesLogList, List<RefusedBroadcastCaseBasicValue> broadcastVals)
			throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: createCase(cs="+cs+USER_DISPLAY_NAME+userDisplayName+", chargesLogList="+chargesLogList+")");
		}
		final String METHOD_NAME = ".createCase - ";
		String caseNumber = "";
		String caseType = "";
		if (cs.getCaseType().equals("T")) {
			caseType = "TRIAL";
		} else if (cs.getCaseType().equals("S")) {
			caseType = "SENTENCE";
		} else {
			caseType = "APPEAL";
		}

		try {
			if (cs.getCaseType().equals("T") && cs.getReceiptType().equals("IO")) {
				log.debug("about to generate case number for case type : TRIAL_INDICTMENT, court :"+cs.getCourtID());
				caseNumber = GenerateCaseNumberControllerBeanBusinessDelegate.DelegateFactory.getInstance()
						.generateCaseNumber(cs.getCourtID(), "TRIAL_INDICTMENT");
			} else {
				log.debug("about to generate case number for case type : "+caseType+", court :"+cs.getCourtID());
				caseNumber = GenerateCaseNumberControllerBeanBusinessDelegate.DelegateFactory.getInstance()
						.generateCaseNumber(cs.getCourtID(), caseType);
			}
			log.debug("Case Number "+caseNumber+" generated");
			CaseMaintainer caseMaintainer = new CaseMaintainer();
		
			cs.setCaseNumber(Integer.parseInt(caseNumber.substring(1)));
			Case caseCreated = caseMaintainer.createCase(cs, userDisplayName);
			log.debug(METHOD_NAME + " created case :"+Integer.parseInt(caseNumber.substring(1)));

			log.debug("about to add to charges log");
			ChargesLogMaintainer main = new ChargesLogMaintainer();
			for (int i = 0; i < chargesLogList.size(); i++) {
				chargesLogList.get(i).setCaseId(caseCreated.getCaseId());
				main.create(chargesLogList.get(i), userDisplayName);
			}
			
			log.debug("about to add a case listing entry");
			createCaseListingEntry(caseMaintainer.getCaseBasicValue(caseCreated), userDisplayName);
			
			//if it's a T case we need to also create the refusedbroadcastobjects
			if(caseType.equals("TRIAL") && broadcastVals.size()>0 ) {
				RefusedBroadcastCaseMaintainer refusedMaintainer = new RefusedBroadcastCaseMaintainer();
				for(int i=0;i<broadcastVals.size();i++) {
					refusedMaintainer.create(broadcastVals.get(i), caseCreated, userDisplayName);
				}
			}
			
			log.debug(METHOD_NAME + " exited");

			return caseCreated.getCaseType() + "" + caseCreated.getCaseNumber();
		} catch (Exception e) {
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+" : "+e);
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new CaseControllerException(UNEXPECTED_ERROR, EXCEPTION, e);
		}
	}
	
	/**
	 * Handles the creation of a case listing entry record
	 * @param caseBasicValue	Case record the case listing entry record is a child of
	 * @param userDisplayName	Username to set against the new record
	 * @throws CaseControllerException 
	 */
	private void createCaseListingEntry(CaseBasicValue caseBasicValue, String userDisplayName) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: createCaseListingEntry(caseBasicValue="+caseBasicValue+USER_DISPLAY_NAME+userDisplayName+")");
		}
		CaseListingEntryBasicValue listingsEntry = new CaseListingEntryBasicValue();
		listingsEntry.setCaseId(caseBasicValue.getCaseId());
		listingsEntry.setCourtId(caseBasicValue.getCourtID());
		
		// Determine what the default judge type should be set to
		String defaultJudgeTypeCode = caseBasicValue.getDefaultJudgeTypeCode();
		if ( defaultJudgeTypeCode != null ) {
			// Find the XHB_REF_SYSTEM_CODE id for the judge type code
			Integer judgeTypeId = getDefaultJudgeTypeId(defaultJudgeTypeCode, caseBasicValue.getCourtID());
			if ( judgeTypeId != null ) { 
				listingsEntry.setRefJudgeTypeId(judgeTypeId);
			}
		}
		
		CaseListingEntryMaintainer listingsMaintainer = new CaseListingEntryMaintainer();
		listingsMaintainer.create(listingsEntry, userDisplayName);
	}
 
	/**
	 * Updates the cases using the given CaseBasicValues.
	 * 
	 * @param cbvs
	 *            array of CaseBasicValue which have been changed.
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateCase(CaseBasicValue[] cbvs, String userDisplayName) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: updateCase(cbvs="+cbvs+USER_DISPLAY_NAME+userDisplayName+")");
		}
		int size = cbvs.length;

		for (int i = 0; i < size; i++) {
			CaseBasicValue cbv = cbvs[i];
			updateCase(cbv, userDisplayName);
		}
	}

	/**
	 * returns a CaseComplexValue object for the given case id
	 * 
	 * @param caseId
	 * @return CaseComplexValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ListingsControllerException
	 */
	public CaseComplexValue getCaseComplexValueByPrimaryKey(Integer caseId) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: getCaseComplexValueByPrimaryKey(caseId="+caseId+")");
		}
		return caseHelper.getCaseComplexValueByPrimaryKey(caseId);
	}

	/**
	 * Get the case for the given id.
	 * 
	 * @param caseId
	 *            the id of the case to retrieve.
	 * @return CaseBasicValue
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public CaseBasicValue getCase(Integer caseId) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: getCase(caseId="+caseId+")");
		}
		final String METHOD_NAME = ".getCase - ";
		try {
			CaseMaintainer caseMaintainer = new CaseMaintainer();
			Case cs = caseMaintainer.findByPrimaryKey(caseId);
			CaseBasicValue caseBasic = caseMaintainer.getCaseBasicValue(cs);
			log.debug(METHOD_NAME + " exited");
			return caseBasic;
		} catch (ObjectNotFoundException ex) {
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+" : "+ex);
			CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
			throw new CaseControllerException();
		}
	}

	/**
	 * This method checks the case reference table to see if reporting
	 * restrictions are set. If they have then it will update public notice to
	 * indicate they are set by sending a court log message.
	 * 
	 * @param caseId
	 *            the current case ID
	 * @param courtRoomId
	 *            the current court room ID
	 * @throws CaseControllerException
	 * @todo Sort out the Object Not found Exception
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void checkCaseHistory(Integer caseId, Integer courtRoomId, String userDisplayName) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: checkCaseHistory(caseId="+caseId+", courtRoomId="+courtRoomId+")");
		}
		
		Integer caseNumber = new Integer(0);
		Integer courtId = new Integer(0);
		String caseType = "";
		String courtName = "";
		// try and get the case and court details for PDDA - needs its own tr/catch block
		try {
			CaseMaintainer caseMaintainer = new CaseMaintainer();
			Case caze = caseMaintainer.findByPrimaryKey(caseId);
			
			caseNumber = caze.getCaseNumber();
			caseType = caze.getCaseType();
			courtId = caze.getCourtId();
			
			CourtMaintainer courtMaintainer = new CourtMaintainer();
			Court court = courtMaintainer.findByPrimaryKey(courtId);
			
			courtName = court.getCourtName();
			
		} catch (ObjectNotFoundException onfe) {
			log.debug("No Case details found for this case. caseId= " + caseId
					+ "This is not a valid scenario, so something fundamental is wrong", onfe);
		}
		

		try {
			// find caseReference by caseId
			CaseReferenceMaintainer caseRefMaintainer = new CaseReferenceMaintainer();
			CaseReference caseReferences = caseRefMaintainer.findByCaseId(caseId);
			Integer reportingRes = caseReferences.getReportingRestrictions();

			// If they are,
			if (reportingRes.equals(CaseReferenceMaintainer.REPORTING_RESTRICTIONS_SET)) {
				// will update the PN to indicate that they are set.
				// (Send a CourtLog Message to the PN)

				// create the log entry for this event
				CourtLogViewValue logEntry = new CourtLogViewValue();

				// set the value which notifies if the case is active in court
				logEntry.setCaseId(caseId);
				logEntry.setEntryDate(new Date());
				// this is a case level event so no further parameters required
				// for CJSE
				logEntry.setEventType(CaseReferenceMaintainer.REPORTING_RESTRICTIONS_EVENT_TYPE_SET);
				
				// Add some PDDA values
				if (caseNumber != null) {
					logEntry.setCaseNumber(caseNumber);
				}
				if ((caseType != null) && (caseType.length() > 0)) { 
					logEntry.setCaseType(caseType);
				}
				if ((courtName != null) && (courtName.length() > 0)) {
					logEntry.setCourtSiteName(courtName);
				}
				
				CourtLogSubscriptionValue subValue = SubscriptionValueAssembler.getSubsciptionValue(logEntry);

				// If the case is not currently in a court room, then there is
				// no need to update the Public Display
				if (subValue.getCourtRoomId() != null) {
					PublicNoticeWorkFlow.setPublicNoticeforCourtRoom(subValue, userDisplayName);
				}
			}
		} catch (PublicNoticeException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
			throw new CaseControllerException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
		} catch (ObjectNotFoundException ex) {
			log.debug("No CaseReferences found for this case. "
					+ "This is a valid scenario where no Reporting Restrictions are set", ex);
		}
	}

	/**
	 * This method is called to get all the hearings scheduled for a case on a
	 * particular day.
	 * 
	 * @param caseId
	 *            The case for which to retrieve the scheduled hearings.
	 * @param day
	 *            The Calendar object representing the day to look for scheduled
	 *            hearings on.
	 * @return an array of ScheduledHearingValues representing the scheduled
	 *         hearings occuring for the case on the day in question.
	 * @throws CaseControllerException
	 *             if there is a problem retrieving the scheduled hearings.
	 * @author Bob Boothby
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public ScheduledHearingValue[] getScheduledHearingsForCaseOnDay(Integer caseId, Calendar day)
			throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: getScheduledHearingsForCaseOnDay(caseId="+caseId+", day="+day+")");
		}
		ArrayList returnList = new ArrayList();
		// find all the scheduled hearings for this case
		Collection schedHearings = getScheduledHearings(caseId);

		Iterator schedHearingsValues = schedHearings.iterator();
		while (schedHearingsValues.hasNext()) {
			ScheduledHearingValue schedHearingValue = (ScheduledHearingValue) schedHearingsValues.next();
			Calendar hearingDate = schedHearingValue.getScheduledHearingDate();
			// if the sched hearing is on this date then add VO to list.
			if (hearingDate != null && day.get(Calendar.DAY_OF_YEAR) == hearingDate.get(Calendar.DAY_OF_YEAR)
					&& day.get(Calendar.YEAR) == hearingDate.get(Calendar.YEAR)) {
				returnList.add(schedHearingValue);
			}
		}
		ScheduledHearingValue[] returnArray = new ScheduledHearingValue[returnList.size()];
		returnList.toArray(returnArray);
		return returnArray;
	}

	/**
	 * @description forces a case resynch. This method must always require a new
	 *              transaction to ensure that it is not rolled back as part of
	 *              a containing transaction
	 * 
	 * @ejb.interface-method view-type="local"
	 * @ejb.transaction type="RequiresNew"
	 */
	public void forceCaseResynch(Integer[] caseIds) throws CaseAccessException, CaseRetrievalIntControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: forceCaseResynch(caseIds="+caseIds+")");
		}
		try {
			new CaseRetrievalIntController().forceCaseResynch(caseIds);
		} catch (CaseRetrievalIntControllerException ex) {
			ctx.setRollbackOnly();
			throw ex;
		} catch (CaseAccessException ex) {
			ctx.setRollbackOnly();
			throw ex;
		}
	}

	/**
	 * Get the charges log for the given case id
	 * 
	 * @param caseId
	 *            the id of the case to retrieve.
	 * @return String
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public String getChargesLog(Integer caseId) {
		if (log.isDebugEnabled()) {
			log.debug("START: getChargesLog(caseId="+caseId+")");
		}
		final String METHOD_NAME = "getChargesLog(caseId)";
		try {
			ArrayList<XhbChargesLog> val = (ArrayList<XhbChargesLog>)XhbChargesLogBeanHelper2.findNonObsoleteByCaseId(caseId);
			Comparator<XhbChargesLog> chargeSeq = new Comparator<XhbChargesLog>() {
				@Override
				public int compare(XhbChargesLog o1, XhbChargesLog o2) {
					return o1.getSequenceNo().compareTo(o2.getSequenceNo());
				}
			};
			Collections.sort(val,chargeSeq);
			String logVal = "";
			Iterator iter = val.iterator();
			while (iter.hasNext()) {
				logVal += ((XhbChargesLog) iter.next()).getChargesInfo();
			}
			return logVal;

		} catch (Exception e) {
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+" : "+e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	
	/**
	 * Returns a collection of cases that are active and have the given group number
	 * 
	 * @param courtId
	 * 		currently logged in court id
	 * @param groupNumber
	 *            the group number linking the cases
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findActiveCasesWithGroupNumber(Integer courtId, Integer groupNumber) {
		if (log.isDebugEnabled()) {
			log.debug("START: findActiveCasesWithGroupNumber(courtId="+courtId+GROUP_NUMBER+groupNumber+")");
		}
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findActiveCasesWithGroupNumber(courtId, groupNumber);
	}

	/**
	 * Returns a collection of all cases that have the given group number
	 * 
	 * @param courtId
	 * 		currently logged in court id
	 * @param groupNumber
	 *            the group number linking the cases
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findCasesByGroupNumber(Integer courtId, Integer groupNumber) {
		if (log.isDebugEnabled()) {
			log.debug("START: findCasesByGroupNumber(courtId="+courtId+GROUP_NUMBER+groupNumber+")");
		}
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findCasesByGroupNumber(courtId, groupNumber);
	}
	
	/**
	 * Returns a single case (the input case id will match) if any defendants are common between
	 * the group number grouped cases. Otherwise will return nothing.
	 * @param caseId
	 * 		input case Id to match
	 * @param courtId
	 * 		currently logged in court id
	 * @param groupNumber
	 *            the group number linking the cases
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findCommonDefendantsWithGroupNumber(Integer caseId, Integer courtId, Integer groupNumber) {
		if (log.isDebugEnabled()) {
			log.debug("START: findCommonDefendantsWithGroupNumber(caseId="+caseId+COURT_ID+courtId+GROUP_NUMBER+groupNumber+")");
		}
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findCommonDefendantsWithGroupNumber(caseId, courtId, groupNumber);
	}
			
	/**
     * Invokes a stored function to determine the status of a given case
     * @param caseId The identifier of the case to be checked
     * @return status of the case
     * @ejb.interface-method view-type="both"
     */
    public String determineCaseStatus(Integer caseId)
    {
    	if (log.isDebugEnabled()) {
			log.debug("START: determineCaseStatus(caseId="+caseId+")");
    	}
    	return caseDatabaseManager.determineCaseStatus(caseId);
    }
    
    /**
     * Invokes a stored function to return the offences of a given case
     * @param caseId The identifier of the case to be checked
     * @return collection of offence details
     * @ejb.interface-method view-type="both"
     */
    public Collection getCaseOffences(Integer caseId)
    {
    	if (log.isDebugEnabled()) {
			log.debug("START: getCaseOffences(caseId="+caseId+")");
    	}
    	return caseDatabaseManager.getCaseOffences(caseId);
    }
    
    
	/**
	 * return group number for courtid
	 * 
	 * @param courtId
	 *            the id of the court.
	 * @return Integer
	 * 
	 * @ejb.interface-method view-type="both"
	 */
    public Integer generateCaseGroupNumber(Integer courtId) {
    	if (log.isDebugEnabled()) {
			log.debug("START: generateCaseGroupNumber(courtId="+courtId+")");
    	}
		Integer num = GenerateCaseGroupControllerBeanBusinessDelegate.DelegateFactory.getInstance().generateCaseGroupNumber(courtId);
		if (log.isDebugEnabled()) {
			log.debug("Returned a group number of : "+num);
		}
		return num;
	}

    /**
     * If a Trial Case is amended and the class has changed from 1 or 2 to 3 or the class has changed
     * from 3 to 1 or 2 then the default judge type on the case listing entry record needs to be updated.
     * @param caseMaintainer
     * @param newCaseBasicValue
     * @param userDisplayName
     * @throws CaseControllerException 
     */
	private void updateCaseListingEntryForAmendCase(CaseBasicValue oldCaseBasicValue, CaseBasicValue newCaseBasicValue, String userDisplayName) throws CaseControllerException {
		if (log.isDebugEnabled()) {
			log.debug("START: updateCaseListingEntryForAmendCase(oldCaseBasicValue="+oldCaseBasicValue+", newCaseBasicValue="+newCaseBasicValue+")");
		}
		// Only for Trial Cases - this is checked when the method is invoked, but it won't hurt to check again
    	if ("T".equals(newCaseBasicValue.getCaseType())) {
    		// Retrieve the old and new class values
    		Integer oldClassCode = oldCaseBasicValue.getClassCode();
    		Integer newClassCode = newCaseBasicValue.getClassCode();
    		
    		if (!newClassCode.equals(oldClassCode) && 
    				(Integer.valueOf(3).equals(oldClassCode) || Integer.valueOf(3).equals(newClassCode))) {  
    			// The old and new class values are different and has changed from a value of 3 or to a value of 3 meaning
    			// that the default judge type must be updated on the case listing entry record
    			CaseListingEntryMaintainer caseListingEntryMaintainer = new CaseListingEntryMaintainer();
    			
    			try {
    				// Determine what the new default judge type should be set to
    				String defaultJudgeTypeCode = newCaseBasicValue.getDefaultJudgeTypeCode();
    				
    				// Find the XHB_REF_SYSTEM_CODE id for the judge type code
    				Integer judgeTypeId = getDefaultJudgeTypeId(defaultJudgeTypeCode, newCaseBasicValue.getCourtID());
    				if ( judgeTypeId != null ) { 
	    				// Update the Case Listing Entry record
	    				CaseListingEntry local = caseListingEntryMaintainer.findByCaseId(newCaseBasicValue.getCaseId());
	    				CaseListingEntryBasicValue caseListingEntryBasicValue = caseListingEntryMaintainer.getBasicValue(local);
	    				caseListingEntryBasicValue.setRefJudgeTypeId(judgeTypeId); 
	    				caseListingEntryMaintainer.updateForAmendCase(caseListingEntryBasicValue, userDisplayName);
    				}
    			} catch (ObjectNotFoundException ex) {
    				// A Case Listing Entry record does not exist for this case so create one
    				createCaseListingEntry(newCaseBasicValue, userDisplayName);
    			}
    		}
    	}
    }
    
    /**
     * Searches the XHB_REF_SYSTEM_CODE table with a type of JUDGE_TYPE for a code supplied and returns the
     * primary key identifier for the matching row
     * @param defaultJudgeTypeCode The default judge type code e.g.'CJ' or 'HJ'
     * @param courtId Court Id
     * @return The primary key identifier for the first matching row
     */
    @SuppressWarnings("unchecked")
	private Integer getDefaultJudgeTypeId(String defaultJudgeTypeCode, Integer courtId) throws CaseControllerException {
    	if (log.isDebugEnabled()) {
			log.debug("START: getDefaultJudgeTypeId(defaultJudgeTypeCode="+defaultJudgeTypeCode+COURT_ID+courtId+")");
    	}
    	Integer judgeTypeId = null;
    	ArrayList<RefSystemCodeBasicValue> refCodeList = null;
    	
    	// Set the search criteria
    	RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
		criteria.setCode(defaultJudgeTypeCode);
		criteria.setCourtId(courtId.toString());
		criteria.setCodeType(RefSystemCodeCriteria.CodeType.JUDGE_TYPE);
		
		SystemCodeHelper systemCodeHelper = new SystemCodeHelper();
		try {
			refCodeList = (ArrayList<RefSystemCodeBasicValue>)systemCodeHelper.findSystemCodes(criteria);
			if ( !refCodeList.isEmpty() ) { 
				// Use the first value (should only be 1 returned) and retrieve the id
				RefSystemCodeBasicValue refSystemCode = refCodeList.iterator().next();
				judgeTypeId = refSystemCode.getId();
			}
			
		} catch (BisRefControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, CaseControllerBean.class);
			throw new CaseControllerException(e.getUserMessageAsMessage().getKey(), "Failed to retrieve ref system data judge type", e);
		}
		
		return judgeTypeId;
    }

	/**
	 * Used in REDEL to delete defendants off Sentence and Trial Cases
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defOnCaseId
	 */
	public void REDELSentenceTrialDelete(Integer defOnCaseId) {
		if (log.isDebugEnabled()) {
			log.debug("START: REDELSentenceTrialDelete(defOnCaseId="+defOnCaseId+")");
		}
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		docDBManager.REDELSentenceTrialDelete(defOnCaseId);

	}

	/**
	 * Used in REDEL to delete defendants off Appeal Cases
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @param defOnCaseId
	 */
	public void REDELAppealDelete(Integer caseId, Integer defOnCaseId) {
		if (log.isDebugEnabled()) {
			log.debug("START: REDELAppealDelete(caseId="+caseId+DEF_ON_CASE_ID+defOnCaseId+")");
		}
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		docDBManager.REDELAppealDelete(caseId, defOnCaseId);

	}

	/**
	 * Used in REDEL to find future Fixtures
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @param currentDate
	 * @return collection
	 */
	public Collection findREDELFutureFixtures(Integer caseId, Date currentDate) {
		if (log.isDebugEnabled()) {
			log.debug("START: findREDELFutureFixtures(caseId="+caseId+CURRENT_DATE+currentDate+")");
		}
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findREDELFutureFixtures(caseId, currentDate);

	}
	
	/**
	 * Used in REDEL to find future Fixtures for def
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @param defOnCaseId
	 * @param currentDate
	 * @return collection
	 */	
	public Collection findREDELFutureFixturesDef(Integer caseId, Integer defOnCaseId, Date currentDate) {
		if (log.isDebugEnabled()) {
			log.debug("START: findREDELFutureFixturesDef(caseId="+caseId+DEF_ON_CASE_ID+defOnCaseId+CURRENT_DATE+currentDate+")");
		}
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findREDELFutureFixturesDef(caseId, defOnCaseId, currentDate);
	}
	
	/**
	 * Used in REDEL to find future Listings for def
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @param defOnCaseId
	 * @param currentDate
	 * @return collection
	 */	
	public Collection findREDELFutureListingsDef(Integer caseId, Integer defOnCaseId, Date currentDate) {
		if (log.isDebugEnabled()) {
			log.debug("START: findREDELFutureListingsDef(caseId="+caseId+DEF_ON_CASE_ID+defOnCaseId+CURRENT_DATE+currentDate+")");
		}
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findREDELFutureListingsDef(caseId, defOnCaseId, currentDate);
	}

	/**
	 * Used in REDEL to find future Listings
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @param currentDate
	 * @return collection
	 */
	public Collection findREDELFutureListings(Integer caseId, Date currentDate) {
		if (log.isDebugEnabled()) {
			log.debug("START: findREDELFutureListings(caseId="+caseId+CURRENT_DATE+currentDate+")");
		}
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findREDELFutureListings(caseId, currentDate);

	}
	
	/**
	 * Used in General trial to retreive broadcast case 
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @return collection
	 */
	public ArrayList<RefusedBroadcastCaseBasicValue> getRefusedBroadcastCaseArray(Integer caseId) {
		if (log.isDebugEnabled()) {
			log.debug("START: getRefusedBroadcastCaseArray(caseId="+caseId+")");
		}
		RefusedBroadcastCaseMaintainer refusedMaintainer = new RefusedBroadcastCaseMaintainer(); 
		return refusedMaintainer.findByCaseId(caseId);

	}
	
	/**
	 * Used to updaate last update when a case is opened,
	 * fix for unauth case report
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId, updatedBy
	 */
	public void updateLastUpdateOnCaseOpen(Integer caseId, String lastUpdatedBy) {
		if (log.isDebugEnabled()) {
			log.debug("START: updateLastUpdateOnCaseOpen(caseId="+caseId+", lastUpdatedBy="+lastUpdatedBy);
		}
		try {
			CaseMaintainer caseMaintainer = new CaseMaintainer();
			caseMaintainer.updateLastUpdate(caseId, lastUpdatedBy);
		
		} catch (ObjectNotFoundException e) {
			// This update must be on a CaseBasicValue retrieved from the midtier
			// therefore if this case is not found to update it is an unexpected exception
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+"updateLastUpdateDateOnCaseOpen : "+e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);		
       }
	}
	
	/**
	 * Used to fetch the DARTS retention policy
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @param darRetentionPolicyId
	 * @return DarRetentionPolicyComplexValue
	 */
	public DarRetentionPolicyComplexValue findDartsRetentionPolicy(final Integer caseId, final Integer darRetentionPolicyId) {
		DarRetentionPolicyComplexValue result = null;
		
		if (darRetentionPolicyId != null) {
			try {
				DartsHelper helper = new DartsHelper();
				result = helper.findDartsRetentionPolicy(caseId, darRetentionPolicyId);
			} catch (FinderException e) {
				CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
	            throw new EJBException(e);
			}
		}
		return result;	
	}

	/**
	 * Used to fetch the DARTS variable retention release date
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @return Date
	 */
	public Date findDvrReleaseDate() {
		DartsHelper helper = new DartsHelper();
		return helper.getDvrReleaseDate();
	}
}
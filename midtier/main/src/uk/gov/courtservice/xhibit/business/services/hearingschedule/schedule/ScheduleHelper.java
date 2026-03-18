package uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoom;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoomMaintainer;
import uk.gov.courtservice.xhibit.business.entities.courtsite.CourtSiteMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearinglist.HearingList;
import uk.gov.courtservice.xhibit.business.entities.hearinglist.HearingListMaintainer;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shstaff.ShStaff;
import uk.gov.courtservice.xhibit.business.entities.shstaff.ShStaffMaintainer;
import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
import uk.gov.courtservice.xhibit.business.entities.sitting.SittingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeCourtRoomUnknownException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingAttendeeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.MoveCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtRoomCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtReporterCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefHearingTypeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJudgeCriteria;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.HearingStatusEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.pdda.PddaHearingProgressEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;

/**
 * <p>
 * 
 * Title:
 * </p>
 * <p>
 * 
 * Description:
 * </p>
 * <p>
 * 
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * 
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * @created 31 March 2003
 * @version $Id: ScheduleHelper.java,v 1.63 2014/06/20 17:22:26 atwells Exp $
 *          <Change History/>
 *          <P>
 * 
 *          10/02/03 - JB - First Issue
 *          </P>
 *          <P>
 * 
 *          11/02/03 - JB - Methods to: - check sittings and - get scheduled
 *          hearings ordered by sequence No.
 *          </P>
 *          <P>
 * 
 *          13/02/03 - JB - Methods to: - order sched hearings and - adjust
 *          sched hearings sequence number when inserting a new one into the
 *          daily list.
 *          </P>
 *          <P>
 * 
 *          17/02/03 - JB - Adjusted the algorithm that determines the new
 *          sequence numbers the sitting's sched hearings.
 *          </P>
 *          <P>
 * 
 *          19/02/03 - JB - Fuller judge name.
 *          </P>
 *          <P>
 * 
 *          20/02/03 - JB - Added getCourtRoom method
 *          </P>
 *          <P>
 * 
 *          25/02/03 - JB - Added createSHDefendants.
 *          </P>
 *          <P>
 * 
 *          26/02/03 - JB - Added method to strip time from a java Date.
 *          </P>
 *          <P>
 * 
 *          03/03/03 - JB - Added updateHearingProgress
 *          </P>
 *          <P>
 * 
 *          28/02/03 - AW Daley - Obs No 64, Test case Id 52033.
 *          </P>
 *          <P>
 * 
 *          04/03/03 - JB - Fix to handle VOs from bisref controller
 *          </P>
 *          <P>
 * 
 *          08/05/03 - MH - Fix to get the right Bench Warrant code from
 *          RefHearingType. Amended getBWHearingInfo and getBWHearingTypeId.
 *          </P>
 *          <P>
 * 
 *          08/05/03 - MH - Fixed to set the right defendantOnCaseId in
 *          DefendanScheduledHearing on create. Added method
 *          getDefendantOnCaseId(caseId, defendantId) and amended
 *          createSHDefendants()
 *          </P>
 *          <P>
 */
/*
 * Ref Date Author Description
 * 
 * 64,52033 28-02-2003 AW Daley getJudgeName modified to make a call to a new
 * method buildPersonsFullNames. This method does not add first, middle or
 * surnames to the full name if null.
 * 
 */
public class ScheduleHelper {
	private static final Logger log = CSServices.getLogger(ScheduleHelper.class);

	private static final String EBW_HEARING_CODE = "EBW";

	private static final String NOT_FLOATING = "0";

	private final BisRefControllerLocal bisRefControllerLocal;

	private final CaseMaintainer caseMaintainer;

	private final SittingMaintainer sittingMaintainer;

	private final ScheduledHearingMaintainer scheduledHearingMaintainer;

	private final SchedHearingDefendantMaintainer schedHearingDefendantMaintainer;

	private final HearingListMaintainer hearingListMaintainer;

	private final ShStaffMaintainer staffMaintainer;

	private final SchedHearingAttendeeMaintainer attendeeMaintainer;

	private final CourtSiteMaintainer courtSiteMaintainer;

	private PddaHelper pddaHelper;

	/**
	 * Constructor for the ScheduleHelper object
	 */
	public ScheduleHelper() {
		log.debug("ScheduleHelper() - constructor");

		bisRefControllerLocal = (BisRefControllerLocal) CSServices.getEJBServices()
				.createLocalSession(BisRefControllerLocalHome.class);

		caseMaintainer = new CaseMaintainer();
		sittingMaintainer = new SittingMaintainer();
		scheduledHearingMaintainer = new ScheduledHearingMaintainer();
		schedHearingDefendantMaintainer = new SchedHearingDefendantMaintainer();
		hearingListMaintainer = new HearingListMaintainer();
		staffMaintainer = new ShStaffMaintainer();
		attendeeMaintainer = new SchedHearingAttendeeMaintainer();
		courtSiteMaintainer = new CourtSiteMaintainer();
	}

	/**
	 * Get the Ref Hearing information for a BW hearing. MH - Will get the first EBW
	 * since there might more than one in the database.
	 * 
	 * @return RefHearingTypeBasicValue
	 * @throws CSConfigurationException
	 */
	public RefHearingTypeBasicValue getBWHearingInfo(Integer courtId) throws CSConfigurationException {
		try {
			log.debug("getBWHearingInfo() - called");
			RefHearingTypeCriteria htc = new RefHearingTypeCriteria();

			// MH - should be EBW and use of a static final String
			// htc.setHearingCode("BWE");
			htc.setHearingCode(EBW_HEARING_CODE);
			htc.setCourtId(courtId.toString());
			Collection li = bisRefControllerLocal.findHearingTypes(htc);

			// MH - There can be more than one EBW but we are only
			// interessted in
			// the first one.
			if (li == null || li.size() == 0) {
				log.debug("getBWHearingInfo() - Did not retrieve the expected hearing type");
				throw new CSConfigurationException("Found unexpected hearingtypes");
			} else {
				log.debug("Will take out the first hearing type");
				Iterator it = li.iterator();

				// there can be more than one EBW
				RefHearingTypeBasicValue rht = (RefHearingTypeBasicValue) it.next();

				if (log.isDebugEnabled()) {
					log.debug("getBWHearingInfo() - Ref Hearing type id: " + rht.getId());
				}

				return rht;
			}
		} catch (BisRefControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new CSConfigurationException(e.toString(), e);
		}
	}

	/**
	 * Get Reference Hearing information for a given hearing type...
	 * 
	 * @param id Ref hearing type Id
	 * @return RefHearingTypeBasicValue VO.
	 * @throws CSConfigurationException
	 */
	public RefHearingTypeBasicValue getRefHearingInfo(Integer id) throws CSConfigurationException {
		try {
			log.debug("getRefHearingInfo() - called :: Hearing id: " + id);
			RefHearingTypeCriteria htc = new RefHearingTypeCriteria();
			htc.setPrimaryKey(id);
			Collection li = bisRefControllerLocal.findHearingTypes(htc);

			if (li.size() != 1) {
				log.debug("getRefHearingInfo() - Did not retrieve the expected single hearing type");
				throw new CSConfigurationException("Found unexpected hearingtypes");
			} else {
				Iterator it = li.iterator();
				RefHearingTypeBasicValue rht = (RefHearingTypeBasicValue) it.next();
				if (log.isDebugEnabled()) {
					log.debug("getRefHearingInfo() - Exited :: Ref Hearing type id: " + rht.getId());
				}
				return rht;
			}
		} catch (BisRefControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new CSConfigurationException(e.toString(), e);
		}
	}

	/**
	 * Short cut to the Hearing type Id for a bench Warrant Hearing.
	 * 
	 * @return
	 */
	/** @todo BAL remove if midtier builds ok */
	public Integer getBWHearingTypeId(Integer courtId) {
		log.debug("getBWHearingTypeId() - called");
		RefHearingTypeBasicValue refHearingType = getBWHearingInfo(courtId);
		log.debug("getBWHearingTypeId() - return : " + refHearingType.getId());

		return refHearingType.getId();
	}

	/**
	 * Gets the judgeDetsFromSchedHearing attribute of the ScheduleHelper object
	 * 
	 * @param shId Description of the Parameter
	 * @return The judgeDetsFromSchedHearing value
	 * @exception HearingScheduleException Description of the Exception
	 */
	public RefJudgeBasicValue getJudgeDetsFromSchedHearing(Integer shId) throws HearingScheduleException {
		log.debug("getJudgeDetsFromSchedHearing() - called :: shId: " + shId);

		Integer judgeId = getSHJudgeId(shId);

		RefJudgeBasicValue rjbv = this.getJudgeDetails(judgeId);

		log.debug("getJudgeDetsFromSchedHearing() - Exited OK :: rjbv: " + rjbv);
		return rjbv;
	}

	/**
	 * Get the Judge Id from the Scheduled Hearing.
	 * 
	 * @param scheduledHearingId
	 * @return Sched Hearing judgeId
	 * @exception HearingScheduleException Description of the Exception
	 */
	public Integer getSHJudgeId(Integer scheduledHearingId) throws HearingScheduleException {
		log.debug("getSHJudgeId() - called :: Scheduled Hearing Id: " + scheduledHearingId);

		try {
			ScheduledHearing scheduledHearing = scheduledHearingMaintainer.findByPK(scheduledHearingId);

			Collection shAttendees = scheduledHearing.getScheduledHearingAttendee();
			Integer judgeId = null;
			Iterator it = shAttendees.iterator();
			while (it.hasNext()) {
				SchedHearingAttendee sha = (SchedHearingAttendee) it.next();
				if (sha.getAttendeeType().equals("J")) {
					judgeId = sha.getRefJudgeId();
					break;
				}
			}

			log.debug("getSHJudgeId() - Exited OK :: judgeId: " + judgeId);
			return judgeId;
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new HearingScheduleException("HearingSchdule.Missing_Data",
					"Unable to find Scheduled Hearing for id: " + scheduledHearingId + " " + e.toString(), e);
		}
	}

	/**
	 * Get judge's name based on judge Id (uses bus ref controller)
	 * 
	 * @param judgeId
	 * @return
	 * @exception HearingScheduleException Description of the Exception
	 */
	public RefJudgeBasicValue getJudgeDetails(Integer judgeId) throws HearingScheduleException {
		try {
			log.debug("getJudgeDetails() - called - judgeId: " + judgeId);
			RefJudgeCriteria rjc = new RefJudgeCriteria();
			rjc.setPrimaryKey(judgeId);
			Collection coll = bisRefControllerLocal.findJudges(rjc);
			if (coll.size() <= 0) {
				HearingScheduleException hse = new HearingScheduleException("HearingSchedule.Unexpected",
						"Did not find the expected number of hearing judges");
				CSServices.getDefaultErrorHandler().handleError(hse, getClass(),
						hse.toString() + "Found: " + coll.size() + " entries");
				throw hse;
			} else {
				Iterator it = coll.iterator();
				RefJudgeBasicValue rj = (RefJudgeBasicValue) it.next();

				if (log.isDebugEnabled()) {
					log.debug("getJudgeDetails() - Exited OK :: RefJudge: " + rj);
				}

				return rj;
			}
		} catch (BisRefControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new HearingScheduleException("HearingSchdule.Missing_Data",
					"Unable to find Judge date for id: " + judgeId + " " + e.toString(), e);
		}
	}

	/**
	 * Get judge's name based on judge Id (uses bus ref controller)
	 * 
	 * @param judgeId
	 * @return
	 */
	public String getJudgeName(Integer judgeId) {
		try {
			log.debug("getJudgeName() - called - judgeId: " + judgeId);
			RefJudgeCriteria rjc = new RefJudgeCriteria();
			rjc.setPrimaryKey(judgeId);
			Collection coll = bisRefControllerLocal.findJudges(rjc);
			if (coll.size() <= 0) {
				log.debug("getJudgeName() - Did not retrieve the expected single hearing judge - " + coll.size()
						+ "entries");
				throw new CSConfigurationException("Found unexpected hearing judges");
			} else {
				Iterator it = coll.iterator();
				RefJudgeBasicValue rj = (RefJudgeBasicValue) it.next();

				// Builds Judges full name ignoring nulls
				String refJudgeFullName = this.buildPersonsFullName(rj.getTitle(), rj.getFirstName(),
						rj.getMiddleName(), rj.getSurname());

				if (log.isDebugEnabled()) {
					log.debug("getJudgeName() - Returning - Ref Judge name: " + refJudgeFullName);
				}

				return refJudgeFullName;
			}
		} catch (BisRefControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new CSConfigurationException(e.toString(), e);
		}
	}

	/**
	 * Get courtsite Id based on court room Id
	 * 
	 * @param courtRoomId
	 * @return
	 * @throws HearingScheduleException
	 */
	public Integer getCourtSiteId(Integer courtRoomId) throws HearingScheduleException {
		try {
			log.debug("getCourtSiteId() - called :: courtRoomId: " + courtRoomId);
			Integer courtSiteId = null;
			CourtRoomCriteria crc = new CourtRoomCriteria();
			crc.setPrimaryKey(courtRoomId);
			Collection coll = bisRefControllerLocal.findCourtRooms(crc);
			log.debug("collection size " + coll.size());

			if (coll.size() <= 0) {
				log.debug("getCourtSiteId() - Did not retrieve the expected single court site");
				throw new CSConfigurationException("Did not find court sites");
			} else {
				Iterator it = coll.iterator();
				CourtRoomBasicValue cr = (CourtRoomBasicValue) it.next();
				courtSiteId = cr.getCourtSiteId();
				log.debug("getCourtSiteId() - CourtSite: " + courtSiteId);
				return courtSiteId;
			}
		} catch (SysRefControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new CSConfigurationException(e.toString(), e);
		}
		// catch (ObjectNotFoundException onfe)
		// {
		// CSServices.getDefaultErrorHandler().handleError(onfe, getClass(),
		// onfe.toString());
		// throw new HearingScheduleException("", onfe.toString(), onfe);
		// }

	}

	/**
	 * @param courtRoomId
	 * @return
	 * @throws HearingScheduleException
	 */
	public CourtRoomBasicValue getCourtRoom(Integer courtRoomId) throws HearingScheduleException {
		try {
			// SysRefController sysRefController = null;
			log.debug("getCourtRoom() - called - courtRoomId:" + courtRoomId);
			CourtRoomCriteria csc = new CourtRoomCriteria();
			csc.setPrimaryKey(courtRoomId);
			Collection coll = bisRefControllerLocal.findCourtRooms(csc);

			// commented out because ObjectNotFoundException should be
			// thrown for find methods if nothing there
			/*
			 * if ( coll == null ) { HearingScheduleException e = new
			 * HearingScheduleException("", "No CourtRooms found" );
			 * CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString()
			 * ); throw e; }
			 */
			if (coll.size() <= 0) {
				log.debug("getCourtRoom() - Did not retrieve the expected single court site");
				throw new CSConfigurationException("Did not find court sites");
			} else {
				Iterator it = coll.iterator();
				CourtRoomBasicValue crv = (CourtRoomBasicValue) it.next();
				if (log.isDebugEnabled()) {
					log.debug("getCourtRoom() - CourtRoom Name:" + crv.getCourtRoomName());
					log.debug("getCourtRoom() - Exited OK");
				}
				return crv;
			}
		} catch (SysRefControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new CSConfigurationException(e.toString(), e);
		}
		// catch (ObjectNotFoundException onfe)
		// {
		// CSServices.getDefaultErrorHandler().handleError(onfe, getClass(),
		// onfe.toString());
		// throw new HearingScheduleException("", onfe.toString(), onfe);
		// }
	}

	/**
	 * Check whether case exists in Xhibit.
	 * 
	 * @param caseNumber
	 * @param caseType
	 * @param courtId
	 * @return
	 */
	public boolean caseExists(Integer caseNumber, String caseType, Integer courtId) {
		boolean retVal = true;
		try {
			if (log.isDebugEnabled()) {
				log.debug("caseExists() - called - Case: " + caseNumber + caseType + " CourtID: " + courtId);
			}
			caseMaintainer.findByNumberTypeAndCourt(caseNumber, caseType, courtId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			retVal = false;
		}
		return retVal;
	}

	/**
	 * @param notBeforeTime
	 * @param courtRoomId
	 * @param courtSiteId
	 * @return
	 */
	public Sitting sittingExists(Date notBeforeTime, Integer courtRoomId, Integer courtSiteId) {
		log.debug("sittingExists() - start()");
		if (/** (notBeforeTime == null) || */
		(courtRoomId == null) || (courtSiteId == null)) {
			log.debug("invalid paramters");
			return null;
		}
		Timestamp tsNotBeforeTime = notBeforeTime == null ? null : new Timestamp(notBeforeTime.getTime());

		if (log.isDebugEnabled()) {
			log.debug("sittingExists() - called - " + "Date: " + notBeforeTime + "CourtRmId: " + courtRoomId
					+ " CourtSiteID: " + courtSiteId);
		}

		Timestamp startOfDay = getStartOfDay();
		Collection sittings = sittingMaintainer.findByDateAndCourt(startOfDay, courtRoomId, courtSiteId);

		return sittingExists(sittings, tsNotBeforeTime);
	}

	public Sitting sittingExists(Collection sittings, Timestamp tsNotBeforeTime) {
		Sitting bestSitting = null;

		// if no sittings found for this courtroom today return null
		if (sittings.isEmpty())
			return null;

		Iterator itr = sittings.iterator();
		Map map = new HashMap();
		while (itr.hasNext()) {
			Sitting sitting = (Sitting) itr.next();

			// Ignore sittings associated with floating cases
			if (sitting.getIsFloating() == null || sitting.getIsFloating().equalsIgnoreCase(NOT_FLOATING)) {
				map.put(sitting.getSittingTime(), sitting);
			}
		}

		Timestamp bestFit = null;
		Collection sittingTimes = map.keySet();

		// if not before time is not specified, take the last sitting in the
		// current day
		if (tsNotBeforeTime == null) {
			Timestamp lastSittingTime = (Timestamp) Collections.max(sittingTimes);
			return (Sitting) map.get(lastSittingTime);
		}

		// if not before time is specified, take the nearest sitting before
		// the not before time in the current day
		Iterator mapItr = sittingTimes.iterator();
		boolean firstItr = true;
		while (mapItr.hasNext()) {
			Timestamp ts = (Timestamp) mapItr.next();
			if ((firstItr) && (ts.before(tsNotBeforeTime))) {
				bestFit = ts;
				firstItr = false;
				continue;
			}
			log.debug("checking ts=" + ts);
			if (ts.before(tsNotBeforeTime) && ts.after(bestFit)) {
				bestFit = ts;
			}
		}

		log.debug("bestFit=" + bestFit);
		bestSitting = (Sitting) map.get(bestFit);

		return bestSitting;
	}

	/**
	 * Create a sitting. (Uses sysref to get all the othe information it needs).
	 * 
	 * @param courtRoomId
	 * @param sittingTime
	 * @return
	 * @throws HearingScheduleException
	 */
	public Sitting createSitting(Integer courtRoomId, Date sittingTime, String userDisplayName)
			throws HearingScheduleException {
		try {
			final Integer courtSiteId = getCourtSiteId(courtRoomId);
			final String courtSiteIdString = ((courtSiteId == null) ? null : courtSiteId.toString());

			CourtCriteria cc = new CourtCriteria();

			// ensure we use the string version
			cc.setCourtSiteId(courtSiteIdString);
			Collection courtsColl = bisRefControllerLocal.findCourts(cc);
			Iterator it = courtsColl.iterator();
			CourtBasicValue cbv = (CourtBasicValue) it.next();

			return createSitting(cbv.getId(), courtSiteId, courtRoomId, sittingTime, userDisplayName);
		} catch (SysRefControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new HearingScheduleException("", e.toString(), e);
		} catch (ObjectNotFoundException onfe) {
			CSServices.getDefaultErrorHandler().handleError(onfe, getClass(), onfe.toString());
			throw new HearingScheduleException("", onfe.toString(), onfe);
		}
	}

	/**
	 * @param courtId
	 * @param courtSiteId
	 * @param courtRoomId
	 * @param sittingTime
	 * @return
	 * @exception ObjectNotFoundException Description of the Exception
	 * @throws HearingScheduleException
	 */
	public Sitting createSitting(Integer courtId, Integer courtSiteId, Integer courtRoomId, Date sittingTime,
			String userDisplayName) throws ObjectNotFoundException {
		if (log.isDebugEnabled()) {
			log.debug("createSitting() - called :: " + "courtId: " + courtId + " courtSiteId: " + courtSiteId
					+ " courtRoomId: " + courtRoomId + " sittingTime: " + sittingTime);
		}

		// 1. Today's hearing list...
		Calendar cal = Calendar.getInstance();
		cal.setTime(sittingTime);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int date = cal.get(Calendar.DATE);
		GregorianCalendar gre = new GregorianCalendar(year, month, date);
		HearingList hearingList = hearingListMaintainer.findByCourtIdAndDate(courtId, gre.getTime());

		// 2. Basic VO...
		SittingBasicValue sbv = createSittingVO(courtRoomId, courtSiteId, sittingTime, hearingList.getListId());

		// this is a new sitting set the sequence number to 1
		sbv.setSittingSequenceNo(new Integer(1));

		// 3. Create sitting...
		Sitting sitting = (Sitting) sittingMaintainer.create(sbv, userDisplayName);
		log.debug("created sitting id=" + sitting.getPrimaryKey());

		// each sitting is part of a hearing list so associate the newly
		// created istting to the hearinglist
		log.debug("got hearing list id=" + hearingList.getPrimaryKey());
		sitting.setHearingList(hearingList);

		log.debug("createSitting() - Exited OK");
		return sitting;
	}

	/**
	 * Get a set of ordered Scheduled Hearing for a sitting
	 * 
	 * @param sitting  Sitting local entity.
	 * @param criteria
	 * @return
	 */
	public List getOrderedScheduledHearings(Sitting sitting, String[] criteria) {
		// Follow CMR...
		Collection coll = sitting.getScheduledHearings();

		// Get SchedHearing Basic VOs
		List vosColl = scheduledHearingMaintainer.getScheduledHearings(coll);

		// Sort the list
		Sorter.sort(vosColl, criteria);

		return vosColl;
	}

	/**
	 * Adjusts the sitting sequence numbers based on the scheduled Hearing's "not
	 * before time"
	 * 
	 * @param sitting
	 * @param shbv
	 * @return
	 * @exception ObjectNotFoundException Description of the Exception
	 */
	public Integer adjustSHSequenceNumbers(Sitting sitting, ScheduledHearingBasicValue shbv, String userDisplayName)
			throws ObjectNotFoundException {
		log.debug("adjustSHSequenceNumbers():start");
		Integer insertedSequenceNumber = null;
		int largestSequenceNumber = 0;
		final String[] orderCriteria = new String[] { "sequenceNo" };

		// Ordered list of sched hearings
		final Vector list = (Vector) getOrderedScheduledHearings(sitting, orderCriteria);
		final int listSize = list.size();
		log.debug("listSize: " + listSize);

		// only process if we have enough details and some results
		if ((shbv.getNotBeforeTime() != null) && (listSize > 0)) {
			// work backwards so we place after all other at the earlier
			// time
			for (int i = listSize - 1; i >= 0; i--) {
				final ScheduledHearingBasicValue f1 = (ScheduledHearingBasicValue) list.get(i);
				final int sequenceNo = f1.getSequenceNo().intValue();
				log.debug("processing ScheduledHearingBasicValue as f1 with ID:" + f1.getId());
				log.debug("****largestSequenceNumber:" + largestSequenceNumber);
				log.debug("****sequenceNo:" + sequenceNo);

				if (largestSequenceNumber < sequenceNo) {
					largestSequenceNumber = sequenceNo;
				}

				if ((f1.getNotBeforeTime() != null) && !shbv.getNotBeforeTime().before(f1.getNotBeforeTime())) {
					int nextSequenceNo = sequenceNo + 1;
					insertedSequenceNumber = new Integer(nextSequenceNo++);

					// now that we have a value, we need to sort out the
					// rest
					// of the sequence numbers
					log.debug("****now that we have a value, we need to sort out the rest of the sequence numbers");
					for (int j = i + 1; j < listSize; j++) {
						ScheduledHearingBasicValue f2 = (ScheduledHearingBasicValue) list.get(j);
						log.debug("********processing ScheduledHearingBasicValue as f2 with ID:" + f2.getId());
						f2.setSequenceNo(new Integer(nextSequenceNo++));
						scheduledHearingMaintainer.update(f2, userDisplayName);
					}

					// break out of the main loop, as we need to do no
					// more processing
					break;
				}
			}
		}

		// if we have no valid sequence, put it at the end
		if (insertedSequenceNumber == null) {
			insertedSequenceNumber = new Integer(largestSequenceNumber + 1);
		}
		log.debug("largestSequenceNumber:" + insertedSequenceNumber.intValue());

		log.debug("adjustSHSequenceNumbers():end");
		return insertedSequenceNumber;
	}

	/**
	 * Add defendants to the Scheduled Hearing.
	 * 
	 * @param sh         ScheduledHearing entity.
	 * @param defendants Collection of DefendantValue VOs.
	 */
	public void createSHDefendants(Integer caseId, ScheduledHearing sh, Collection defendants, String userDisplayName) {
		if (log.isDebugEnabled()) {
			log.debug("createSHDefendants() - called :: " + "caseId: " + caseId + " Sched Hearing Id: "
					+ sh.getScheduledHearingId() + " No. of Defendants :" + defendants.size());
		}

		Iterator it = defendants.iterator();
		while (it.hasNext()) {
			DefendantValue dv = (DefendantValue) it.next();
			SchedHearingDefendantBasicValue shdbv = new SchedHearingDefendantBasicValue();

			// MH - the defOnCaseBasicValue is populated with the
			// defendantid
			// rather than the defOnCaseid. The SchedHearingDef is expecting
			// the
			// defOnCaseId NOT the defendantId.
			// shdbv.setDefendantOnCaseID(dv.getDefOnCaseBasicValue().getDefendantID());

			// get the defendantOnCaseId
			Integer defOnCaseId = this.getDefendantOnCaseId(caseId, dv.getDefOnCaseBasicValue().getDefendantID());

			// Set the real id.
			shdbv.setDefendantOnCaseID(defOnCaseId);

			schedHearingDefendantMaintainer.create(shdbv, sh, userDisplayName);
		}

		log.debug("createSHDefendants() - Exited OK");
	}

	/**
	 * Looks up a DefendanOnCase with given caseid and defendantid
	 * 
	 * @param caseId
	 * @param defendantId
	 * @return Integer the defOnCaseId
	 */
	private Integer getDefendantOnCaseId(Integer caseId, Integer defendantId) {
		return XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(defendantId, caseId).getDefendantOnCaseId();
	}

	/**
	 * Create Sitting Basic VO
	 * 
	 * @param courtRoomId
	 * @param courtSiteId
	 * @param sittingTime
	 * @param listId      Description of the Parameter
	 * @return SittingBasicValue
	 */
	public SittingBasicValue createSittingVO(Integer courtRoomId, Integer courtSiteId, Date sittingTime,
			Integer listId) {
		if (log.isDebugEnabled()) {
			log.debug("createSittingVO() - called" + " courtRoomId: " + courtRoomId + ", courtSiteId: " + courtSiteId
					+ ", sittingTime :" + sittingTime + ", listId: " + listId);
		}

		SittingBasicValue sbv = new SittingBasicValue();
		sbv.setCourtRoomID(courtRoomId);
		sbv.setCourtSiteID(courtSiteId);
		sbv.setSittingTime(sittingTime);
		sbv.setListID(listId);
		sbv.setIsFloating(NOT_FLOATING);

		return sbv;
	}

	/**
	 * @param scheduledHearingId
	 * @param hearingProgress
	 * @throws HearingScheduleException
	 */
	public void updateHearingProgress(Integer scheduledHearingId, Integer hearingProgress, String userDisplayName)
			throws HearingScheduleException {
		try {
			ScheduledHearing sh = scheduledHearingMaintainer.findByPK(scheduledHearingId);
			ScheduledHearingBasicValue shbv = scheduledHearingMaintainer.getScheduledHearingBasicValue(sh);
			shbv.setHearingProgress(hearingProgress);
			scheduledHearingMaintainer.update(shbv, userDisplayName);
			// tell the public displays that the hearing progress has
			// changed
			// notifyPublicDisplays(sh.getSitting().getCourtRoomId(),
			// sh.getSitting().getCourtSiteId());
			sh = scheduledHearingMaintainer.findByPK(scheduledHearingId);
			notifyNewPublicDisplays(sh, userDisplayName);

			sendHearingStatusDataToPdda(scheduledHearingId, hearingProgress);

		} catch (ObjectNotFoundException onfe) {
			CSServices.getDefaultErrorHandler().handleError(onfe, getClass(), onfe.toString());
			throw new HearingScheduleException("", onfe.toString(), onfe);
		}
	}
	
	public void sendCurrentHearingStatusDataToPdda(Integer scheduledHearingId) {
		if (log.isDebugEnabled()) {
			log.debug("Entering method: sendCurrentHearingStatusDataToPdda");
		}
		
		// Now send to PDDA if needed
		if (getPddaHelper().isSendToPDDA()) {
			try {
				ScheduledHearing scheduledHearing = scheduledHearingMaintainer.findByPK(scheduledHearingId);
				
				if (scheduledHearing != null) {
					Integer hearingProgress = scheduledHearing.getHearingProgress();
					if (log.isDebugEnabled()) {
						String logStr = "Calling sendHearingStatusDataToPdda with scheduledHearingId:"
								+ scheduledHearingId + " and hearingProgress: "+ hearingProgress; 
						log.debug(logStr);
					}
					sendHearingStatusDataToPdda(scheduledHearingId, hearingProgress);
				}
			} catch (ObjectNotFoundException onfe) {
				log.error(
						"Unable to find an instance of an object needed to send hearing progress notification to PDDA: "
								+ onfe);
				onfe.printStackTrace();
			}
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Leaving method: sendHearingStatusDataToPdda");
		}
	}

	/**
	 * 
	 * @param scheduledHearingId
	 * @param hearingProgress
	 */
	private void sendHearingStatusDataToPdda(Integer scheduledHearingId, Integer hearingProgress) {
		if (log.isDebugEnabled()) {
			log.debug("Entering method: sendHearingStatusDataToPdda");
		}

		// Now send to PDDA if needed
		if (getPddaHelper().isSendToPDDA()) {
			PddaHearingProgressEvent phpe = new PddaHearingProgressEvent();

			boolean foundCourt = false;
			boolean foundCourtRoom = false;
			String caseType = "";
			String courtName = "";
			String courtRoomName = "";
			Integer caseNumber = 0;
			String isCaseActive = "";
			Integer courtId = 0;

			try {
				// Use the scheduled hearing id to get the hearing id from XHB_SCHEDULED_HEARING
				ScheduledHearing scheduledHearing = scheduledHearingMaintainer.findByPK(scheduledHearingId);
				if (scheduledHearing != null) {
					// Use the hearing id to get the case id from XHB_HEARING
					Integer hearingId = scheduledHearing.getHearingId();
					Integer sittingId = scheduledHearing.getSittingId();
					isCaseActive = scheduledHearing.getIsCaseActive();

					if (hearingId != null) {
						// Use the case id to get the case number and court id from XHB_CASE
						HearingMaintainer hearingMaintainer = new HearingMaintainer();
						Hearing hearing = hearingMaintainer.findByPK(hearingId);

						if (hearing != null) {
							Integer caseId = hearing.getCaseId();

							// Use the caseid to get the case details
							if (caseId != null) {
								Case caze = caseMaintainer.findByPrimaryKey(caseId);

								if (caze != null) {
									caseNumber = caze.getCaseNumber();
									caseType = caze.getCaseType();
									courtId = caze.getCourtId();

									// Use the courtid to get the court name from XHB_COURT
									if (courtId != null) {
										CourtMaintainer courtMaintainer = new CourtMaintainer();
										Court court = courtMaintainer.findByPrimaryKey(courtId);

										if (court != null) {
											courtName = court.getCourtName();
											foundCourt = true;
										}
									}
								}
							}
						}
					}

					// Now use the sittingId to get the courtroomid from xhb_sitting
					if (sittingId != null) {
						Sitting sitting = sittingMaintainer.findByPK(sittingId);

						if (sitting != null) {
							Integer courtRoomId = sitting.getCourtRoomId();

							if (courtRoomId != null) {
								CourtRoomMaintainer courtRoomMaintainer = new CourtRoomMaintainer();
								CourtRoom courtRoom = courtRoomMaintainer.findByPrimaryKey(courtRoomId);

								if (courtRoom != null) {
									courtRoomName = courtRoom.getCourtRoomName();
									foundCourtRoom = true;
								}
							}
						}
					}
				}
			} catch (ObjectNotFoundException onfe) {
				log.error(
						"Unable to find an instance of an object needed to send hearing progress notification to PDDA: "
								+ onfe);
			}

			// Only if we've found the details we need shall we send it to PDDA
			if (foundCourt && foundCourtRoom) {
				phpe.setCaseType(caseType);
				phpe.setCaseNumber(caseNumber);
				phpe.setCourtName(courtName);
				phpe.setCourtId(courtId);
				phpe.setHearingProgressIndicator(hearingProgress);
				phpe.setIsCaseActive(isCaseActive);
				phpe.setCourtRoomName(courtRoomName);
				getPddaHelper().sendMessage(phpe, "Pdda", true);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("Leaving method: sendHearingStatusDataToPdda");
		}
	}

	/**
	 * Gets the courtClerkId attribute of the ScheduleHelper object
	 * 
	 * @param mCValue Description of the Parameter
	 * @return The courtClerkId value
	 * @exception ObjectNotFoundException Description of the Exception
	 */
	public ShStaff getCourtClerkId(MoveCaseValue mCValue) throws ObjectNotFoundException {
		log.debug("start ----------getCourtClerkId params = " + mCValue.toString());

		ShStaff local = staffMaintainer.findByPrimaryKey(mCValue.getExistingCourtClerkId());
		// check staff role to make sure we got correct ID
		// String staffRole = local.getStaffRole();
		local.getStaffRole();

		return local;
	}

	/**
	 * Gets the refCrtRep attribute of the ScheduleHelper object
	 * 
	 * @param mCValue Description of the Parameter
	 * @return The refCrtRep value
	 * @exception HearingScheduleException Description of the Exception
	 */
	public RefCourtReporterBasicValue getRefCrtRep(MoveCaseValue mCValue) throws HearingScheduleException {
		log.debug("getRefCrtRep----------start");
		log.debug("params = findByPK = (" + mCValue.getExisitingSHWriterId() + ")");
		try {
			RefCourtReporterCriteria criteria = new RefCourtReporterCriteria();
			criteria.setPrimaryKey(mCValue.getExisitingSHWriterId());
			log.debug("doing a findByPK");
			Collection coll = bisRefControllerLocal.findCourtReporters(criteria);
			log.debug("got a collection back of size = " + coll.size());

			// Search by PK should only return 1
			Iterator itr = coll.iterator();
			RefCourtReporterBasicValue value = (RefCourtReporterBasicValue) itr.next();
			log.debug("value object = " + value.toString());

			return value;
		} catch (BisRefControllerException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new HearingScheduleException("", e.toString(), e);
		}
	}

	/**
	 * populateAttendeeValue
	 * 
	 * @param SchedHearingId
	 * @param id             Description of the Parameter
	 * @param attendeeType   Description of the Parameter
	 * @return SchedHearingAttendeeBasicValue
	 * @throws HearingScheduleException
	 */
	public SchedHearingAttendeeBasicValue populateAttendeeValue(Integer id, Integer SchedHearingId,
			String attendeeType) {
		final SchedHearingAttendeeBasicValue value = new SchedHearingAttendeeBasicValue();

		if (attendeeType.equals(PersonValue.JUDGE)) {
			value.setAttendeeType(PersonValue.JUDGE);
			value.setRefJudgeID(id);
		} else if (attendeeType.equals(PersonValue.COURT_CLERK)) {
			value.setAttendeeType(PersonValue.COURT_CLERK);
			// value.setShStaffID(id);
		} else if (attendeeType.equals(PersonValue.COURT_REPORTER)) {
			value.setAttendeeType(PersonValue.COURT_REPORTER);
			value.setRefCourtReporterID(id);
		}

		return value;
	}

	/**
	 * Builds the full name of a person from the first, middle and surnames. The
	 * first,middle and surnames names will not be added to the full name if null
	 * 
	 * @param title
	 * @param firstName
	 * @param surname
	 * @param middleName Description of the Parameter
	 * @return full name of person.
	 */
	private String buildPersonsFullName(String title, String firstName, String middleName, String surname) {
		StringBuffer fullName = new StringBuffer();

		// Title
		if (title != null) {
			fullName.append(title);
			fullName.append(' ');
		}

		// First name
		if (firstName != null) {
			fullName.append(firstName);
			fullName.append(' ');
		}

		// Middle name
		if (middleName != null) {
			fullName.append(middleName);
			fullName.append(' ');
		}

		// Surname
		if (surname != null) {
			fullName.append(surname);
		}

		return fullName.toString();
	}

	/**
	 * Finds the latest CourtClerk that has bee an Attendee to this scheduled
	 * hearing Finding the last attending judge. Don't like using the max(sequence
	 * number) but am forced into it.
	 * 
	 * @param scheduledHearingId Description of the Parameter
	 * @return The existingCourtClerk value
	 * @exception ObjectNotFoundException Description of the Exception
	 */
	public ShStaff getExistingCourtClerk(Integer scheduledHearingId) throws ObjectNotFoundException {
		return getExistingCourtClerk(scheduledHearingMaintainer.findByPK(scheduledHearingId));
	}

	/**
	 * Finds the latest CourtClerk that has bee an Attendee to this scheduled
	 * hearing Finding the last attending judge. Don't like using the max(sequence
	 * number) but am forced into it.
	 * 
	 * @param scheduledHearing Description of the Parameter
	 * @return The existingCourtClerk value
	 * @exception ObjectNotFoundException Description of the Exception
	 */
	public ShStaff getExistingCourtClerk(ScheduledHearing scheduledHearing) throws ObjectNotFoundException {
		if (log.isDebugEnabled()) {
			log.debug("start : getExistingCourtClerk for scheduled hearing id = " + scheduledHearing.getHearingId());
		}

		// list for court Clerks
		Collection courtClerkList = new ArrayList();

		// Get attendees
		Collection theAttendees = scheduledHearing.getScheduledHearingAttendee();
		Iterator attendeesIterator = theAttendees.iterator();
		log.debug("Number of Attendees is " + theAttendees.size());

		// iterate through the attendees
		while (attendeesIterator.hasNext()) {
			SchedHearingAttendee schedHearingAttendee = (SchedHearingAttendee) attendeesIterator.next();

			// if a court reporter return the associated ShStaff
			log.debug("*** ScheduledHearingAttendee Type = " + schedHearingAttendee.getAttendeeType()
					+ " - comparing to - " + PersonValue.COURT_CLERK + " ***");

			if (schedHearingAttendee.getAttendeeType().equals(PersonValue.COURT_CLERK)) {
				if (log.isDebugEnabled()) {
					log.debug(" Returning Attendee" + schedHearingAttendee.getPrimaryKey());
				}

				// this attendee is a Clerk Add to List
				courtClerkList.add(schedHearingAttendee.getPrimaryKey());
			}
		}

		if (courtClerkList.size() != 0) {
			// sort them by the primary keys to get the last court clerk
			// assigned to the
			// court.
			Integer latestCourtClerkAttendeeId = (Integer) Collections.max(courtClerkList);

			if (log.isDebugEnabled()) {
				log.debug(" The latest Court Reporter is  attendee id => " + latestCourtClerkAttendeeId);
			}

			if (latestCourtClerkAttendeeId != null) {
				SchedHearingAttendee latestCourtClerkAttendee = attendeeMaintainer
						.findByPrimaryKey(latestCourtClerkAttendeeId);
				return latestCourtClerkAttendee.getShStaff();
			}
		}

		// if no Court Clerk found then return nothing.
		if (log.isDebugEnabled()) {
			log.debug(" Returning no Court Report");
		}

		return null;
	}

	/**
	 * PFOX: This is a bugfix 52407 that finde the existing refjudgeId from the
	 * hearing schedule. Searching the Attendee table for a particular schedule
	 * hearing. Finding the last attending judge. Don't like using the max(sequence
	 * number) but am forced into it.
	 * 
	 * @param scheduledHearing Description of the Parameter
	 * @return The existingJudge value
	 */
	Integer getExistingJudgeFromAttendees(ScheduledHearing scheduledHearing) {
		if (log.isDebugEnabled()) {
			log.debug("Entered method getExistingJudgeFromAttendees");
		}

		// arraylist of judges
		Collection judgeList = new ArrayList();
		// get the collection of attendees for this trial.
		Collection attendees = scheduledHearing.getScheduledHearingAttendee();

		if (log.isDebugEnabled()) {
			log.debug("Number of Attendees are :" + attendees.size());
		}

		Iterator attendeesIterator = attendees.iterator();

		// iterate through the collection of attendees
		while (attendeesIterator.hasNext()) {
			SchedHearingAttendee schedHearingAttendee = (SchedHearingAttendee) attendeesIterator.next();

			if (log.isDebugEnabled()) {
				log.debug(" schedHearingAttendee id :" + schedHearingAttendee.getShAttendeeId() + " is "
						+ schedHearingAttendee.getAttendeeType());
			}

			// if attendee type is judge then return the refjudgeId
			// and end the method.
			if (schedHearingAttendee.getAttendeeType().equals(PersonValue.JUDGE)) {
				if (log.isDebugEnabled()) {
					log.debug(" adding judgeId " + schedHearingAttendee.getRefJudgeId());
				}

				// Or add it to an Array list
				judgeList.add(schedHearingAttendee.getRefJudgeId());
			}
		}

		if (log.isDebugEnabled()) {
			log.debug(" Number of Attendee judges found => " + judgeList.size());
		}

		// sort in descending order
		Integer latestJudge = null;
		if (judgeList.size() != 0) {
			latestJudge = (Integer) Collections.max(judgeList);
		}

		if (log.isDebugEnabled()) {
			log.debug("returning last attending judge : " + latestJudge);
		}

		// return to the largest.
		return latestJudge;
	}

	/**
	 * Retrieve only the "date" part of a Date.... stripping off hours, minutes,
	 * seconds.
	 * 
	 * @param date
	 * @return
	 */
	public static Date stripTime(Date date) {
		final Calendar old = new GregorianCalendar();
		old.setTime(date);
		return stripTime(old).getTime();
	}

	public static Timestamp getStartOfDay() {
		return new Timestamp(stripTime(Calendar.getInstance()).getTime().getTime());
	}

	private static Calendar stripTime(Calendar calendar) {
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		final int dayOfMonth = calendar.get(Calendar.DATE);

		return new GregorianCalendar(year, month, dayOfMonth);
	}

	/*
	 * private void notifyPublicDisplays(Integer courtRoomId, Integer courtSiteId)
	 * throws HearingScheduleException { log.debug("notifyPublicDisplays() with: \n"
	 * + "\ncourtRoomId: " + courtRoomId + "\ncourtSiteId: " + courtSiteId); // need
	 * courtRoomId and courtSiteId to build urn PDNotificationValue
	 * notificationValue = new PDNotificationValue();
	 * notificationValue.setNotificationId(PDNotificationInterface.
	 * HEARING_PROGRESS_UPDATE); // get the court urn for this scheduled hearing try
	 * { CourtLogMessagingHelper clmHelper = new CourtLogMessagingHelper(); String
	 * urn = clmHelper.buildCourtURN(courtRoomId, courtSiteId);
	 * 
	 * notificationValue.addParameter(PDNotificationValue.URN, urn);
	 * subscriptionHelper.publishEvent("publicDisplayListener", notificationValue);
	 * } catch (CourtLogException e) {
	 * CSServices.getDefaultErrorHandler().handleError(e, this.getClass()); throw
	 * new HearingScheduleException(e.getUserMessageAsMessage().getKey(),
	 * e.getMessage(), e); } catch (SubscriptionException e) {
	 * CSServices.getDefaultErrorHandler().handleError(e, this.getClass()); throw
	 * new HearingScheduleException(e.getUserMessageAsMessage().getKey(),
	 * e.getMessage(), e); } }
	 */

	private void notifyNewPublicDisplays(ScheduledHearing scheduledHearing, String userDisplayName) {
		try {
			Integer courtId = courtSiteMaintainer.findByPrimaryKey(scheduledHearing.getSitting().getCourtSiteId())
					.getCourtId();
			String courtName = getCourtName(courtId);
			Integer courtRoomNo = getCourtRoomNumber(scheduledHearing);
			DisplayablePublicNoticeValue[] publicNotices =
					PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(scheduledHearing.getSitting().getCourtRoomId());
			CourtRoomIdentifier cri = new CourtRoomIdentifier(courtId, scheduledHearing.getSitting().getCourtRoomId(),
					courtName, courtRoomNo, publicNotices);
			CaseChangeInformation cci = new CaseChangeInformation(scheduledHearing.getIsCaseActive().equals("Y"));
			HearingStatusEvent hse = new HearingStatusEvent(cri, cci);
			PddaHelper notifier = new PddaHelper();
			notifier.sendMessage(hse, userDisplayName);
			notifier.close();
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch (PublicNoticeCourtRoomUnknownException e) {
			log.error("Unable to find any public notices for either the court room id.");
			e.printStackTrace();
		}
	}

	public String getCourtName(Integer courtId) {
		String courtName = "Unknown";
		try {
			CourtMaintainer courtMaintainer = new CourtMaintainer();
			courtName = courtMaintainer.findByPrimaryKey(courtId).getCourtName();
		} catch (ObjectNotFoundException e) {
			log.error("Cannot find the court site name.");
			e.printStackTrace();
		}
		return courtName;
	}

	public Integer getCourtRoomNumber(ScheduledHearing sh) {
		Integer courtRoomNo = 0;
		if ((sh != null) && (sh.getSitting() != null) && (sh.getSitting().getCourtSiteId() != null)) {
			CourtRoomMaintainer courtRoomMaintainer = new CourtRoomMaintainer();
			try {
				courtRoomNo = courtRoomMaintainer.findByPrimaryKey(sh.getSitting().getCourtRoomId())
						.getCrestCourtRoomNo();
			} catch (ObjectNotFoundException e) {
				log.error("Cannot find the court room number.");
				e.printStackTrace();
			}
		}
		return courtRoomNo;
	}

	/**
	 * 
	 * @return
	 */
	private PddaHelper getPddaHelper() {
		if (pddaHelper == null) {
			pddaHelper = new PddaHelper();
		}
		return pddaHelper;
	}
}

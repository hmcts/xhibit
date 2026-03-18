package uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordValidationHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;

/**
 * <p>
 * Title: HearingEndHelper
 * </p>
 * <p>
 * Description: The helper class for End Hearing functionality.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version $Revision: 1.27 $
 */
public class HearingEndHelper {
	private static final Logger log = CSServices.getLogger(HearingEndHelper.class);

	private static final HearingRecordValidationHelper validationHelper = new HearingRecordValidationHelper();

	/**
	 * Private constructor as all methods are static and should be accessed in a
	 * static manner
	 */
	private HearingEndHelper() {
		// empty
	}

	/**
	 * Method to calculate a hearings duration time.
	 * 
	 * @param scheduledHearingsBasic
	 *            Collection of ScheduledHearingBasicValue
	 * @param docId
	 *            The id of the defendant on case we are calculating for
	 * @return Long
	 * @throws HearingEndedDurationCalculationException
	 */
	public static Long calculateDuration(Collection scheduledHearingsBasic, Integer docId)
			throws HearingEndedDurationCalculationException {
		log.debug("HearingEndHelper.calculateDuration(Collection scheduledHearingsBasic) called");

		// if passed in value is null, or empty, then the duration is 0
		if ((scheduledHearingsBasic == null) || (scheduledHearingsBasic.isEmpty())) {
			return new Long(0);
		}

		long totalDurationTime = EndHearingConstants.NULL_VALUE;
		long durationPerSchedHearing = EndHearingConstants.NULL_VALUE;

		final Iterator it = scheduledHearingsBasic.iterator();

		while (it.hasNext()) {
			// get the scheduled hearing basic vo.
			ScheduledHearingBasicValue scheduledHearing = (ScheduledHearingBasicValue) it.next();

			// calculate the time per scheduled hearing by using the
			durationPerSchedHearing = TimeCalculationHelper.calculateScheduledHearingTime(scheduledHearing.getId(),
					docId);

			// add the time to the total hearing time.
			totalDurationTime += durationPerSchedHearing;
		} // end of loop

		// BUG-FIX: 53380
		// check if the time is a negative number - if so reset it to 0.
		if (totalDurationTime < 0) {
			log.debug("The total duration was less than 0 : " + totalDurationTime + ", reset it to 0.");
			totalDurationTime = 0;
		}

		log.debug("Return calculated durationTime : " + totalDurationTime);

		return new Long(totalDurationTime);
	}

	/**
	 * Method to acquire all of the hearings associated with the passed in
	 * hearing. The returned array of <code>Hearing</code> objects will contain
	 * the passed in hearing and all of the linked hearings (via the linked
	 * hearing id).
	 * <p>
	 * Note no <i>null</i> check will be performed on the passed in parameter as
	 * this is package private we can guarantee the input parameter elsewhere
	 * </p>
	 * 
	 * @param hearing
	 *            The hearing we want to find all associated hearings of
	 * @return An array of <code>Hearing</code>'s
	 * @throws ObjectNotFoundException
	 *             if the call to findByLinkedHearingId throws the exception
	 * @see uk.gov.courtservice.xhibit.business.entities.hearing
	 *      .HearingMaintainer#findByLinkedHearingId(Integer)
	 */
	public static Hearing[] getAllLinkedHearings(Hearing hearing) {
		// Get all linked hearing entities
		if (hearing.getLinkedHearingId() != null) {
			log.debug("Have a linked id : " + hearing.getLinkedHearingId());
			final Collection<Hearing> hearings = HearingMaintainer.getInstance()
					.findByLinkedHearingId(hearing.getLinkedHearingId());
			Hearing[] hearingsArray = new Hearing[hearings.size()];
			return hearings.toArray(hearingsArray);
		} else {
			// hearing is not linked and we only have the one passed in.
			log.debug("Hearing is not linked");
			// MH 2003-04-10: bug-fix to enable single hearings as well as
			// linked hearings in case there are no linked hearings we need
			// to
			// set the size to one.
			return new Hearing[] { hearing };
		}
	}

	/**
	 * Utility method used to convert a <code>java.util.Date</code> Object into
	 * a <code>java.util.Calendar</code> Object, with the time portion of the
	 * data (hours, minutes, seconds and milliseconds) all set to 0.
	 * 
	 * @param inDate
	 *            The <code>java.util.Date</code> Object to convert
	 * @return The resultant <code>java.util.Calendar</code>, or <i>null</i> if
	 *         the passed in <code>java.util.Date</code> is <i>null</i>
	 */
	public static Calendar convertDateToCalendarDay(Date inDate) {
		if (inDate == null) {
			return null;
		}

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(inDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

	/**
	 * Private utility method used to convert a <code>java.util.Date</code>
	 * Object into a <code>java.sql.Timestamp</code> Object, with the time
	 * portion of the data (hours, minutes, seconds and milliseconds) all set to
	 * 0.
	 * 
	 * @param inDate
	 *            The <code>java.util.Date</code> Object to convert
	 * @return The resultant <code>java.sql.Timestamp</code>, or <i>null</i> if
	 *         the passed in <code>java.util.Date</code> is <i>null</i>
	 */
	public static Timestamp convertDateToTimestamp(Date inDate) {
		return ((inDate != null) ? (new Timestamp(convertDateToCalendarDay(inDate).getTime().getTime())) : null);
	}

	/**
	 * Find or create def hearing record for all listed defendants
	 * 
	 * @param hearing
	 * @return array of DefHearingRecord
	 * @throws HearingEndedValidationException
	 */
	public static DefHearingRecord[] getDefHearingRecords(Hearing hearing, String userDisplayName)
			throws HearingEndedValidationException {
		final Collection<DefHearingRecord> returnCollection = new ArrayList<DefHearingRecord>();
		final HashSet<Integer> defOnCases = new HashSet<Integer>();

		final Collection scheduledHearings = hearing.getScheduledHearings();
		final Iterator scheduledHearingsIt = scheduledHearings.iterator();

		// for each scheduled hearing find the listed defendants from the
		// ScheduledHearingDefendant entity and create a unique list of
		// defendants on cases.
		while (scheduledHearingsIt.hasNext()) {
			ScheduledHearing sh = (ScheduledHearing) scheduledHearingsIt.next();
			Collection shDefs = sh.getSchedHearingDefendant();
			Iterator shDefsIt = shDefs.iterator();
			while (shDefsIt.hasNext()) {
				SchedHearingDefendant shDef = (SchedHearingDefendant) shDefsIt.next();
				defOnCases.add(shDef.getDefOnCaseID());
			}
		}

		final Iterator<Integer> it = defOnCases.iterator();
		while (it.hasNext()) {
			final Integer docId = it.next();
			final DefHearingRecord dhr = findOrCreateDefHearingRecord(hearing, docId, userDisplayName);

			if (dhr != null) {
				returnCollection.add(dhr);
			}
		}

		DefHearingRecord[] records = new DefHearingRecord[returnCollection.size()];
		return returnCollection.toArray(records);
	}

	/**
	 * Utility method used to determine if the specified defendant (on case) is
	 * actually on the passed in scheduled hearing, if either of the passed
	 * parameters is <i>null</i>, then <i>false</i> will be returned.
	 * 
	 * @param scheduledHearing
	 *            the scheduled hearing we want to check for the defendant
	 * @param defendantOnCaseId
	 *            the defendant on case id
	 * @return <i>true</i> if the defendantOnCaseId is on the scheduled hearing,
	 *         <i>false</i> otherwise
	 */
	public static boolean isDefendantOnScheduledHearing(ScheduledHearing scheduledHearing, Integer defendantOnCaseId) {
		if ((defendantOnCaseId != null) && (scheduledHearing != null)) {
			final Iterator it = scheduledHearing.getSchedHearingDefendant().iterator();

			while (it.hasNext()) {
				SchedHearingDefendant defendant = (SchedHearingDefendant) it.next();
				if (defendantOnCaseId.equals(defendant.getDefOnCaseID())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Utility method used to acquire the most recent schedule hearing's id
	 * 
	 * @param hearing
	 *            the hearing whose scheduled hearings we want to check
	 * @return An <code>Integer</code> value representing the id of the most
	 *         recent scheduled hearing for the passed in hearing, or
	 *         <i>null</i> if the passed in hearing is <i>null</i>, or there are
	 *         no scheduled hearings
	 */
	public static Integer getMostRecentScheduledHearingId(Hearing hearing) {
		Integer scheduledHearingId = null;

		if (hearing != null) {
			Collection scheduledHearings = hearing.getScheduledHearings();
			if (scheduledHearings != null) {
				Iterator it = scheduledHearings.iterator();
				Date latest = null;

				while (it.hasNext()) {
					ScheduledHearing sh = (ScheduledHearing) it.next();
					Date shDate = getScheduledHearingDate(sh);

					if ((latest == null) || ((shDate != null) && (shDate.after(latest)))) {
						latest = shDate;
						scheduledHearingId = sh.getScheduledHearingId();
					}
				}
			}
		}

		return scheduledHearingId;
	}

	/**
	 * Acquire the date of the passed in scheduled hearing
	 * 
	 * @param scheduledHearing
	 * @return
	 */
	public static Date getScheduledHearingDate(ScheduledHearing scheduledHearing) {
		return ((scheduledHearing.getOriginalTime() != null) ? scheduledHearing.getOriginalTime()
				: scheduledHearing.getNotBeforeTime());
	}

	/**
	 * Method to attempt to find the DefHearingRecord for the specified hearing
	 * and defendant on case id. If it does not find one, then it will check to
	 * see if one can be created, and if so, will create one.
	 * 
	 * @param hearing
	 *            The assiocated hearing
	 * @param defendantOnCaseId
	 * @return
	 * @throws HearingEndedValidationException
	 */
	private static DefHearingRecord findOrCreateDefHearingRecord(Hearing hearing, Integer defendantOnCaseId,
			String userDisplayName) throws HearingEndedValidationException {
		DefHearingRecord defHearingRecord = null;

		if ((defendantOnCaseId != null) && (hearing != null)) {
			try {
				defHearingRecord = DefHearingRecordMaintainer.getInstance()
						.findByDefendantOnCaseIDAndHearingID(defendantOnCaseId, hearing.getHearingId());
			} catch (ObjectNotFoundException e) {
				try {
					if (validationHelper.validateCaseTypes(hearing.getCaseId()).booleanValue()) {
						DefHearingRecordBasicValue dhrv = new DefHearingRecordBasicValue();
						DefendantOnCaseMaintainer defOCMaintainer = new DefendantOnCaseMaintainer();
						DefendantOnCase doc = defOCMaintainer.findByPrimaryKey(defendantOnCaseId);
						dhrv.setDefendantOnCaseID(defendantOnCaseId);
						dhrv.setHearingID(hearing.getHearingId());

						// HRA application is not required, will always be set
						// to 'N'
						dhrv.setIsHraApplication("N");

						defHearingRecord = (DefHearingRecord) DefHearingRecordMaintainer.getInstance().create(dhrv,
								userDisplayName);
					}
				} catch (HearingRecordException ex) {
					handleWrapAndRethrowException(ex);
				} catch (ObjectNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}

		return defHearingRecord;
	}

	/**
	 * Utility method to deal with handling Exceptions from other controllers,
	 * and wrapping and rethrowing them as HearingEndedValidationException
	 * 
	 * @param ex
	 *            The Exception to handle and rethrow, must be a
	 *            <code>CSRecoverableException</code> or child of
	 * @throws HearingEndedValidationException
	 *             the new Exception to throw
	 */
	private static void handleWrapAndRethrowException(CSRecoverableException ex)
			throws HearingEndedValidationException {
		CSServices.getDefaultErrorHandler().handleError(ex, HearingEndHelper.class);
		if (ex.getUserMessageAsMessage().getParameters().length > 0) {
			throw new HearingEndedValidationException(ex.getUserMessageAsMessage().getKey(),
					ex.getUserMessageAsMessage().getParameters(), ex.getMessage(), ex);
		} else {
			throw new HearingEndedValidationException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
		}
	}
}
